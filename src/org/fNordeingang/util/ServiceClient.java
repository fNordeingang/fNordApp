package org.fNordeingang.util;

import android.util.Log;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * User: vileda
 * Date: 21.10.11
 * Time: 17:34
 */
public class ServiceClient {
  DefaultHttpClient httpclient = new DefaultHttpClient();

  enum Service {
    STATUS,
    PROFILE
  }

  public String getUrl(Service service) {
    String serviceUrl = "http://services.fnordeingang.de/services/api/";
    return serviceUrl + service.toString().toLowerCase();
  }

  public JSONObject servicePostJSON(Service service, JSONObject data) throws IOException, JSONException {
    HttpPost httpost = new HttpPost(getUrl(service));
    Log.v("Data:", data.toString());
    StringEntity se = null;

    se = new StringEntity(data.toString());

    httpost.setEntity(se);
    httpost.setHeader("Accept", "application/json");
    httpost.setHeader("Content-type", "application/json");

    ResponseHandler<String> responseHandler = new BasicResponseHandler();
    String response = null;

    response = httpclient.execute(httpost, responseHandler);

    Log.v("Response:", response);

    // if this throws a JSONException - no json object returned
    // => maybe wrong password
    return new JSONObject(response);
  }

  public int toggleStatus(final String username, final String password) {
    try {
      JSONObject userdata = new JSONObject().put("username", username).put("password", password);
      Log.v("Data:", userdata.toString());
      servicePostJSON(Service.STATUS,userdata);
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
}
