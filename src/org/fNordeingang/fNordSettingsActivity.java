package org.fNordeingang;

// android stuff

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import net.sf.andhsli.SimpleCrypto;

//SimpleCrypto

public class fNordSettingsActivity extends Activity {
	private EditText username;
	private EditText password;
	private String fNordCryptoKey = fNordeingangActivity.fNordCryptoKey;
	private String fNordSettingsFilename = fNordeingangActivity.fNordSettingsFilename;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        username = (EditText) findViewById(R.id.fNordSettingsUserfield);
        password = (EditText) findViewById(R.id.fNordSettingsPassfield);
        SharedPreferences settings = getSharedPreferences(fNordSettingsFilename, 0);
        String test = null;
        try {
			test = SimpleCrypto.decrypt(fNordCryptoKey, settings.getString("username", null));
		    } catch (Exception e) {
		    	Log.v("Exception e", "Oh noes!");
		    	return;
		    }
        if (test != null) {
        	username.setText(test);
        }
	}
	public void savefNordSettings(View view) {
		switch (view.getId()) {
		case R.id.fNordSettingsSaveButton:
			if (username.getText().length() == 0 | password.getText().length() == 0 ) {
				Toast.makeText(this, "Please enter Username and Password",
						Toast.LENGTH_LONG).show();
				return;
			}
		    SharedPreferences settings = getSharedPreferences(fNordSettingsFilename, 0);
		    SharedPreferences.Editor editor = settings.edit();
			    
		    try {
		    	editor.putString("username",SimpleCrypto.encrypt(fNordCryptoKey, username.getText().toString()));
			    editor.putString("password",SimpleCrypto.encrypt(fNordCryptoKey, password.getText().toString()));
			    editor.commit();
			} catch (Exception e) {
			   	Log.v("Exception e", "Oh noes!");
			}

		    Toast.makeText(this, "Settings saved.", Toast.LENGTH_LONG).show();
		    
			setResult(1,new Intent());
			finish();
		    }
		}
}		

