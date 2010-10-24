package org.fNordeingang;

// java stuff
import java.io.*;
import java.net.*;
import java.util.*;

// android stuff
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Context;
import android.view.View.OnClickListener;
import android.content.Intent;

public class fNordeingangActivity extends Activity implements OnClickListener {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageButton tweetButton = (ImageButton)findViewById(R.id.fNordTweet);
        ImageButton doorButton = (ImageButton)findViewById(R.id.fNordDoor);
		ImageButton statusButton = (ImageButton)findViewById(R.id.fNordStatus);
        tweetButton.setOnClickListener(this);
        doorButton.setOnClickListener(this);
		statusButton.setOnClickListener(this);
    }
    
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fNordTweet) {
            // start fNordTweet
            this.startActivity(new Intent(this, fNordTweetActivity.class));
		} else if (id == R.id.fNordStatus) {
			// Status Action
            Context context = getApplicationContext();
			CharSequence text = getfNordStatus();
            int duration = Toast.LENGTH_SHORT;
			
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (id == R.id.fNordDoor) {
            // Door Action here
            Context context = getApplicationContext();
            CharSequence text = "not yet implemented!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            // Error here
            Context context = getApplicationContext();
            CharSequence text = "Unknown Button!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
	
	public String getfNordStatus() {
		
		InetAddress fNordStatusAddr;
        try {
            fNordStatusAddr = InetAddress.getByName("fnordeingang.de");
        } catch (UnknownHostException uhe) {
            print("UnknownHostException");
            return "UnknownHostException";
        }
		try {
			// init some stuff
			String line = "";
			Socket sock = new Socket(fNordStatusAddr, 4242);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			OutputStream out = sock.getOutputStream();
			
			// http get rss feed
			String get_request = "GET /status HTTP/1.1\nHost: fnordeingang.de" + "\r\n\r\n";
			out.write(get_request.getBytes());
			
			// skip http header
			while ((line = in.readLine()) != null) {
				if ( line.equals("") ) { // empty line => end of http header - file following
					break;
				}
			}
			
			// get status line
			char[] arr = new char[100];
			int c; int i = 0;
			while ('}' != (char)(c = in.read())) {
				arr[i++] = (char)c;
			}
			arr[i++] = (char)c;
			line = new String(arr);
			
			// close streams and socket
			in.close();
			out.close();
			sock.close();
			
			// parse status
			for (i=0; i+4<line.length(); i++) {
				//"open":
				if (line.substring(i, i+4).equals("open")) {
					int j = i+6;
					while (line.charAt(j) != ',') {
						j++;
					}
					if (line.substring(i+6, j).equals("true")) {
						return "fNordeingang is open!";
					} else if (line.substring(i+6, j).equals("false")) {
						return "fNordeingang is closed.";
					}
				}
			}
			
			return "couldn't read status";
			
		} catch (IOException ioe) {
				print("IOException");
				return "IOException";
			}
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