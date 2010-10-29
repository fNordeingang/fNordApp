package org.fNordeingang;

// android
import android.content.Context;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageInfo;
import android.content.Context;

// http
import de.mastacode.http.Http;

public class CheckForUpdates {
	
	Context context;
	
	CheckForUpdates(Context context) {
		this.context = context;
	}
	
	public void check() {
		/* Todo:
		Context cont = getApplicationContext();
		
		PackageInfo version = getApplicationContext().getPackageManager().getPackageInfo("org.fNordeingang", 0);
		*/
		print("CheckForUpdates.check()");
	}
	
	// helper function
	void print(String input) {
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
		
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}