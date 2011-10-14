/*
fNordeingang Status API Interface
by lcb01
*/
package org.fNordeingang;

//http
import java.io.IOException;


import org.apache.http.impl.client.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
			JSONObject userdata = new JSONObject().put("username", username).put("password", password);
			Log.v("Data:",userdata.toString());		
			
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpost = new HttpPost("http://services.fnordeingang.de/services/api/status");
					
			StringEntity se = new StringEntity(userdata.toString());
			httpost.setEntity(se);
			httpost.setHeader("Accept", "application/json");
			httpost.setHeader("Content-type", "application/json");

			ResponseHandler responseHandler = new BasicResponseHandler();
			String response = httpclient.execute(httpost, responseHandler);

			Log.v("Response:",response);
			// get status
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
			return -2;
		}
		return 1;
		
	}
}