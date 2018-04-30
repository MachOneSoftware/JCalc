package com.machone.jcalc.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.machone.jcalc.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String version = "undefined";
        try {
            PackageInfo p = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = p.versionName;
        } catch (Exception ex) {
            Log.w("setVersionCode", "Error retrieving version code.", ex);
        }

        ((TextView) findViewById(R.id.textview_version))
                .setText(getResources().getString(
                        R.string.about_jcalc_version,
                        version
                ));

        setImageLinks();
    }

    private void setImageLinks() {
        // JCalc icon => jordanjudt.com/jcalc
        findViewById(R.id.image_jcalc).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jordanjudt.com/jcalc")));
            }
        });

        // Mach One Software icon => jordanjudt.com
        findViewById(R.id.image_machone).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jordanjudt.com")));
            }
        });

        // Google Play => JCalc listing (Google Play app, website)
        findViewById(R.id.text_review).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.machone.jcalc")));
                } catch (Exception ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.machone.jcalc")));
                }
            }
        });

        // Facebook => Mach One Software page (Facebook app, website)
        findViewById(R.id.text_facebook).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/175611059835415")));
                } catch (Exception ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MachOneSoftware")));
                }
            }
        });

        // Twitter => Mach One Software profile (Twitter app, website)
        findViewById(R.id.text_twitter).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=MachOneSoftware")));
                } catch (Exception ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MachOneSoftware")));
                }
            }
        });

        // Email => apps@jordanjudt.com
        findViewById(R.id.text_email).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:apps@jordanjudt.com?subject=JCalc%20Feedback")));
            }
        });
    }
}
