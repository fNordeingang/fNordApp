package de.fnordeingang.fnordapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * User: vileda
 * Date: 21.10.11
 * Time: 22:21
 */
public class CommonUtils {
  public static String getDeviceUUID( Context context, ContentResolver contentResolver ) {
    final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    final String tmDevice, tmSerial, androidId;
    tmDevice = "" + tm.getDeviceId();
    tmSerial = "" + tm.getSimSerialNumber();
    androidId = "" + android.provider.Settings.Secure.getString(contentResolver, android.provider.Settings.Secure.ANDROID_ID);

    UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
    return deviceUuid.toString();
  }
}
