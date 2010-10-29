package org.fNordeingang;

// android
import android.content.Context;
import android.widget.Toast;

// http
import de.mastacode.http.Http;

public class CheckForUpdates {
	
	Context context;
	
	CheckForUpdates(Context context) {
		this.context = context;
	}
	
	public void check() {
		
	}
	
	// helper function
	void print(String input) {
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;
		
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}