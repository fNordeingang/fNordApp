package org.fNordeingang;

import java.io.*;
import java.util.*;

// android stuff
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.TableLayout;
import android.view.ViewGroup.LayoutParams;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.util.Linkify;

//SimpleCrypto
import net.sf.andhsli.SimpleCrypto;

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
		    }
        if (test != null) {
        	username.setText(test);
        	password.setText("deinemama");
        }
	}
	public void savefNordSettings(View view) {
		switch (view.getId()) {
		case R.id.fNordSettingsSaveButton:
			if (username.getText().length() == 0 | password.getText().length() == 0) {
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
		    	Log.v("Error!", "Exception e");
		    }
			// Debugging
	    	String userdebug = "foo";
	    	String passdebug = "bar";
		    try {
			userdebug = SimpleCrypto.decrypt(fNordCryptoKey, settings.getString("username", null));
			passdebug = SimpleCrypto.decrypt(fNordCryptoKey, settings.getString("password", null));
		    } catch (Exception e) {
		    	Log.v("Exception e", "Oh noes!");
		    }
			String usercryptodebug = settings.getString("username", null);
			String passcryptodebug = settings.getString("password", null);
			Log.v("crypto Username: ", usercryptodebug);
			Log.v("crypto Password: ", passcryptodebug);
			Log.v("cleartext Username: ", userdebug);
			Log.v("cleartext Password: ", passdebug);
			setResult(1,new Intent());
		    finish();
			break;
		}
		
	}
}