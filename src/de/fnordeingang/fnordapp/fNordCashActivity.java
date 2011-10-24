package de.fnordeingang.fnordapp;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import de.fnordeingang.fnordapp.util.ServiceClient;
import net.sf.andhsli.SimpleCrypto;

/**
 * User: Linus
 * Date: 23.10.11
 * Time: 01:16
 */
public class fNordCashActivity extends TabActivity {
  private TabHost mTabHost;
  private ServiceClient client = new ServiceClient();
  String username = "";
  String password = "";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fnordcash);
    mTabHost = getTabHost();

    mTabHost.addTab(mTabHost.newTabSpec("AccountTab").setIndicator("Account").setContent(new Intent(this, fNordCashAccountActivity.class)));
    mTabHost.addTab(mTabHost.newTabSpec("ShoppingTab").setIndicator("Shop").setContent(new Intent(this, fNordCashShoppingActivity.class)));
    mTabHost.addTab(mTabHost.newTabSpec("ShopcartTab").setIndicator("Cart").setContent(new Intent(this, fNordCashShopcartActivity.class)));

    mTabHost.setCurrentTab(0);

    SharedPreferences settings = getSharedPreferences(fNordeingangActivity.fNordSettingsFilename, 0);
    try {
      username = SimpleCrypto.decrypt(fNordeingangActivity.fNordCryptoKey, settings.getString("username", null));
      password = SimpleCrypto.decrypt(fNordeingangActivity.fNordCryptoKey, settings.getString("password", null));
    } catch(Exception e) {
      e.printStackTrace();
    }

    TextView balanceLabel = (TextView) findViewById(R.id.fNordCashBalanceText);
    balanceLabel.setText(client.getProfile(username,password,"dd").getBalance().toString());
  }

}
