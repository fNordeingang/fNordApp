package org.fNordeingang;

// java
import java.io.*;

// android
import android.app.*;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.widget.TextView;
import android.widget.EditText;
import android.text.method.PasswordTransformationMethod;
import android.text.InputType;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;

// json
import org.json.*;

// http
import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import de.mastacode.http.Http;

//SimpleCrypto
import net.sf.andhsli.SimpleCrypto;

public class fNordeingangActivity extends Activity implements OnClickListener {
	public static final String fNordSettingsFilename = "fNordAppSettingsTesting321";
	public static final String fNordCryptoKey = "fNordAppTesting";
	int requestCode;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		// check for updates
		(new CheckForUpdates(this)).check();
		
		// update label of fNordStatus
		updatefNordStatusLabel();
        
        ImageButton tweetButton = (ImageButton)findViewById(R.id.fNordTweet);
        ImageButton doorButton = (ImageButton)findViewById(R.id.fNordDoor);
		ImageButton statusButton = (ImageButton)findViewById(R.id.fNordStatus);
		ImageButton settingsButton = (ImageButton)findViewById(R.id.fNordSettings);
        tweetButton.setOnClickListener(this);
        doorButton.setOnClickListener(this);
		statusButton.setOnClickListener(this);
		settingsButton.setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the request went well (OK) and the request was PICK_CONTACT_REQUEST
    	Log.v("requestCode: ", Integer.toString(requestCode));
    	Log.v("resultCode: ", Integer.toString(resultCode));
    	super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == 1) {
            
        	updatefNordStatusLabel();
            
        }
    }
    
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fNordTweet) {
            // start fNordTweet
            this.startActivity(new Intent(this, fNordTweetActivity.class));
		} else if (id == R.id.fNordStatus) {
			// Status Action
			togglefNordStatusDialog();
			
        } else if (id == R.id.fNordDoor) {
            // maybe in future
            print("not yet implemented!");
        } else if (id == R.id.fNordSettings) {
        	startActivityForResult(new Intent(this, fNordSettingsActivity.class), requestCode);
        } else {
            // Error here
            print("Error: Unknown Button pressed!");
        }
    }

	public static int getfNordStatus() {
		try {
			// get json string
			HttpClient client = new DefaultHttpClient();
			String jsonstring = Http.get("http://services.fnordeingang.de/services/api/status").use(client).asString();
			
			Log.v("Jsonstring",jsonstring);
			// get status
			JSONObject status = (JSONObject) new JSONTokener(jsonstring).nextValue();
			status = status.getJSONObject("status");
			
			if (status.getBoolean("open")) {
				return 1; // open
			} else {
				return 0; // closed
			}

		} catch (IOException ioe) {
			Log.v("IOE",ioe.toString());
			return -1;
		} catch (JSONException jsone) {
			Log.v("Json",jsone.toString());
			return -1;
		}
	}
	
	public static int setfNordStatus(final String username, final String password) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost("http://services.fnordeingang.de/services/api/status");
			
			JSONObject userdata = new JSONObject().put("username", username).put("password", password);
			Log.v("Data:",userdata.toString());	
			StringEntity se = new StringEntity(userdata.toString());
			
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");

			ResponseHandler responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httpost, responseHandler);
			Log.v("Response:",response);
			
			// if this throws a JSONException - no json object returned
			// => maybe wrong password
			JSONObject status = new JSONObject(response);
			
		} catch (IOException ioe) {
			Log.v("IOE: ", ioe.toString());
			return -1;
		} catch (JSONException jsone) {
			Log.v("JSONe: ", jsone.toString());
			return 0;
		} catch (Exception e) {
			Log.v("e: ", e.toString());
			return -2;
		}
		return 1;
	}
	
	// updates the fNordStatus label
	public void updatefNordStatusLabel() {
		updatefNordStatusLabelThread ufslt = new updatefNordStatusLabelThread(updatefNordStatusLabelHandler);
		ufslt.start();
	}
	
    final Handler updatefNordStatusLabelHandler = new Handler() {
		public void handleMessage(Message msg) {
			int status = msg.getData().getInt("status");
			TextView statusView = (TextView)findViewById(R.id.fNordStatusLabel);
			ImageView imageView = (ImageView)findViewById(R.id.fNordStatusIcon);
			switch (status) {
				case 0:
					statusView.setText(R.string.fNordStatusClosed);
					imageView.setImageResource(R.drawable.closed_icon);
					break;
				case 1:
					statusView.setText(R.string.fNordStatusOpen);
					imageView.setImageResource(R.drawable.open_icon);
					break;
				default: // on error (f.e. no internet connection) just display the label
					statusView.setText(R.string.fNordStatusUnknown);
					imageView.setImageResource(R.drawable.unknownstatus_icon);
					break;
			}
		}
	};

	private class updatefNordStatusLabelThread extends Thread {
		Handler handler;
		
		updatefNordStatusLabelThread(Handler h) {
			handler = h;
		}
		
		public void run() {
			// get status
			int status = getfNordStatus();
			
			// send status to main thread
			Message msg = handler.obtainMessage();
			Bundle b = new Bundle();
			b.putInt("status", status);
			msg.setData(b);
			handler.sendMessage(msg);
			
		}
	}
	
	
	public void togglefNordStatusDialog() {
		
		int status = getfNordStatus();
		updatefNordStatusLabel();
		Log.v("Status:",Integer.toString(status));
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (status == 1) { // open
			builder.setMessage("Do you want to close?");
		} else if (status == 0) { // closed
			builder.setMessage("Do you want to open?");
		} else if (status == -1) {
			print("Error: IO or JSON Exception!");
			return;
		} else {
			print("Error: couldn't get fNordStatus");
			return;
		}
		
		builder.setCancelable(false);
		
		// toggle fNordStatus at yes
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				SharedPreferences settings = getSharedPreferences(fNordSettingsFilename, 0);
		        String username = null;
		        String password = null;
		        try {
					username = SimpleCrypto.decrypt(fNordCryptoKey, settings.getString("username", null));
					password = SimpleCrypto.decrypt(fNordCryptoKey, settings.getString("password", null));
				    } catch (Exception e) {
				    	Log.v("Exception e", "Oh noes!");
				    	print("Please enter username & password in settings!");
				    	return;
				    }
		        if (username != null & username.length() != 0 & password.length() != 0 ) {
					String tosend = "http://services.fnordeingang.de/services/api/status";
					int Status;
					// send toggle command to webserver
					Status = setfNordStatus(username,password);
					if (Status == 0) {
						print("Wrong Password?");
					} else if  (Status == -1) {
						print("IO Exception!");
					} else if (Status == -2) {
						print("General Exception!");
					} else {
						print("fNordStatus successfully changed");
						updatefNordStatusLabel();
					}
		        } else {
					print("Please enter username & password in settings!");
					return;
					}
		        }
		});
		
		// cancel dialog at no
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog dialog = builder.create();
		if (status == 1) { // open
			dialog.setTitle("fnord is open");
		} else if (status == 0) { // closed
			dialog.setTitle("fnord is closed");
		}
		
		dialog.show();
	}
	
	
	
	// helper function
	void print(String input) {
        Context context = getApplicationContext();
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
		
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}