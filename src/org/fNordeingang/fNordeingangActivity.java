package org.fNordeingang;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Context;
import android.view.View.OnClickListener;
import android.content.Intent;


public class fNordeingangActivity extends Activity implements OnClickListener {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageButton tweetButton = (ImageButton)findViewById(R.id.fNordTweet);
        ImageButton doorButton = (ImageButton)findViewById(R.id.fNordDoor);
        tweetButton.setOnClickListener(this);
        doorButton.setOnClickListener(this);
    }
    
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fNordTweet) {
            // start fNordTweet
            this.startActivity(new Intent(this, fNordTweetActivity.class));
        } else if (id == R.id.fNordDoor) {
            // Door Action here
            Context context = getApplicationContext();
            CharSequence text = "not yet implemented!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            // Error here
            Context context = getApplicationContext();
            CharSequence text = "Unknown Button!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

}