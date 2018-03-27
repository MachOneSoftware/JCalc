package com.machone.jcalc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View.OnClickListener googleplay = new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.machone.jcalc")));
            }
        };

        View.OnClickListener facebook = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                try {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/175611059835415"));
                } catch(Exception ex){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MachOneSoftware"));
                }
                startActivity(intent);
            }
        };

        View.OnClickListener twitter = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;
                try{
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=MachOneSoftware"));
                } catch (Exception ex){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/MachOneSoftware"));
                }
                startActivity(intent);
            }
        };

        findViewById(R.id.image_googleplay).setOnClickListener(googleplay);
        findViewById(R.id.text_review).setOnClickListener(googleplay);
        findViewById(R.id.image_facebook).setOnClickListener(facebook);
        findViewById(R.id.text_facebook).setOnClickListener(facebook);
        findViewById(R.id.image_twitter).setOnClickListener(twitter);
        findViewById(R.id.text_twitter).setOnClickListener(twitter);
    }
}
