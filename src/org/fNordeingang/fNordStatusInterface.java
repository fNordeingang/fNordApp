/*
fNordeingang Status API Interface
by lcb01
*/
package org.fNordeingang;

//http
import java.io.IOException;

import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;
import de.mastacode.http.Http;

public final class fNordStatusInterface {
	
	fNordStatusInterface() { }
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
			JSONObject userdata = new JSONObject().put("username",username).put("password", password);
			String toggle = new JSONObject().put("user", userdata).toString();
			
			Log.v("String:",toggle);
			HttpClient client = new DefaultHttpClient();
			String response = Http.post("http://services.fnordeingang.de/services/api/status").data("jsondata", toggle).use(client).asString();
			
			// get status
			// if this throws a JSONException - no json object returned
			// => maybe wrong password
			JSONObject status = new JSONObject(response);
			
		} catch (IOException ioe) {
			//print(ioe.toString());
			return -1;
		} catch (JSONException jsone) {
			//print("Wrong Password?");
			return 0;
		}
		return 1;
		
	}
}