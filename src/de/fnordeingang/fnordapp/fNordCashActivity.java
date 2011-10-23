package de.fnordeingang.fnordapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * User: Linus
 * Date: 23.10.11
 * Time: 01:16
 */
public class fNordCashActivity extends TabActivity {
    private TabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fnordcash);
        mTabHost = getTabHost();

        mTabHost.addTab(mTabHost.newTabSpec("AccountTab").setIndicator("Account").setContent(new Intent(this, fNordCashAccountActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("ShoppingTab").setIndicator("Shop").setContent(new Intent(this, fNordCashShoppingActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("ShopcartTab").setIndicator("Cart").setContent(new Intent(this, fNordCashShopcartActivity.class)));

        mTabHost.setCurrentTab(0);
    }

}
