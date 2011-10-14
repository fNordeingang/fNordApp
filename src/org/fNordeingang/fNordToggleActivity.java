package org.fNordeingang;

import java.io.*;
import java.util.*;

// android stuff
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.TableLayout;
import android.view.ViewGroup.LayoutParams;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.util.Linkify;

//json
import org.json.*;

// http
//import org.apache.http.impl.client.*;
//import org.apache.http.client.*;

//import de.mastacode.http.Http;

// fNord Status API


public class fNordToggleActivity extends Activity {
	private EditText username;
	private EditText password;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.togglestatus);
        username = (EditText) findViewById(R.id.fNordtoggleuserfield);
        password = (EditText) findViewById(R.id.fNordtogglepassfield);
	}
	public void togglefNordStatus(View view) {
		switch (view.getId()) {
		case R.id.fNordtogglesendbutton:
			if (username.getText().length() == 0 | password.getText().length() == 0) {
				Toast.makeText(this, "Please enter Username and Password",
						Toast.LENGTH_LONG).show();
				return;
			}
			

				String tosend = "http://services.fnordeingang.de/services/api/status";
				int Status;
				// send toggle command to webserver
				String user = username.getText().toString();
				String pass = password.getText().toString();

				Status = org.fNordeingang.fNordStatusInterface.setfNordStatus(user,pass);

				if (Status == 0) {
					Toast.makeText(this, "Wrong Password?", Toast.LENGTH_LONG).show();
					return;
				} 
				else if  (Status == -1) {
					Toast.makeText(this, "IO Exception!", Toast.LENGTH_LONG).show();
					return;
				} else if (Status == -2) {
					Toast.makeText(this, "General Exception!", Toast.LENGTH_LONG).show();
					return;
				}
				// update label of fNordStatus
				//updatefNordStatusLabel();
				this.startActivity(new Intent(this, fNordeingangActivity.class));
				break;
		}
		
	}
}