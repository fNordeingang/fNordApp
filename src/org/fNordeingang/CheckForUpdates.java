package org.fNordeingang;

// java
import java.io.IOException;
import java.io.File;
import java.io.BufferedOutputStream;
import java.net.*;

// android
import android.content.Context;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageInfo;
import android.content.Context;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.app.AlertDialog;

// http
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import de.mastacode.http.Http;

public class CheckForUpdates {
	
	Context context;
	
	CheckForUpdates(Context context) {
		this.context = context;
	}
	
	public void check() {
		
		int localVersion = 0;
		String serverVersion = "";
		
		// get local version code
		try {
			PackageInfo versionInfo = context.getPackageManager().getPackageInfo("org.fNordeingang", 0);
			localVersion = versionInfo.versionCode;
		
			// get version from server
			HttpClient client = new DefaultHttpClient();
			serverVersion = Http.get("http://dl.dropbox.com/u/1711476/fNordeingang/fNordApp/latest").use(client).asString();
		
			// compare versions
			if (localVersion > 0 && serverVersion != "") {
				if (Integer.parseInt(serverVersion.trim()) > localVersion) {
					// newer version available:
					downloadLatestVersion();
				}
			}
			
		} catch (PackageManager.NameNotFoundException e) {
			// nothing to be done
		} catch (IOException e) {
			// nothing to be done
		} catch (NumberFormatException e) {
			print("NumberFormatException");
		}
	}
	
	private void downloadLatestVersion() {
		
		// ask user for download
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("New Version Available");
		builder.setMessage("Do you want to download the latest version?");
		
		builder.setCancelable(false);
		
		// download at yes
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// download url of latest version
				try {
					HttpClient client = new DefaultHttpClient();
					String latestURL = Http.get("http://dl.dropbox.com/u/1711476/fNordeingang/fNordApp/latest_apk").use(client).asString();
					
					// download file
					java.io.BufferedInputStream in = new java.io.BufferedInputStream(new java.net.URL("http://dl.dropbox.com/u/1711476/fNordeingang/fNordApp/fNordeingang-Beta-0.14.apk").openStream());
					java.io.FileOutputStream fos = new java.io.FileOutputStream(context.getFilesDir().getPath() + "fNordApp.apk");
					java.io.BufferedOutputStream bout = new BufferedOutputStream(fos,1024);
					byte[] data = new byte[1024];
					int x=0;
					while((x=in.read(data,0,1024))>=0) {
						bout.write(data,0,x);
					}
					bout.close();
					in.close();
					
					// open file
					File f = new File(context.getFilesDir().getPath(), "fNordApp.apk");
					ContentResolver cr = context.getContentResolver();
					//cr.openFileDescriptor(f.toURI(), "rw");
					
				}
				catch (IOException e) {
					// nothing to do
				}
			}
		});
		
		// cancel dialog at no
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		// display dialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	// helper function
	void print(String input) {
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
		
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}