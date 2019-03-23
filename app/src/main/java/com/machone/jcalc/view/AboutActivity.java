package com.machone.jcalc.view;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.machone.jcalc.R;
import com.machone.jcalc.helper.PreferenceHelper;

public class AboutActivity extends AppCompatActivity {
    // Support images with TextView on older Android
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

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
                .setText(getString(
                        R.string.about_jcalc_version,
                        version
                ));

        View google = findViewById(R.id.text_review);
        View facebook = findViewById(R.id.text_facebook);
        View twitter = findViewById(R.id.text_twitter);
        View email = findViewById(R.id.text_email);

        setImageWidths(google,facebook,twitter,email);
        setImageLinks(google,facebook,twitter,email);

        PreferenceHelper preferenceHelper = PreferenceHelper.getInstance(this);
        if (!preferenceHelper.getAboutWebsiteTooltipShown()) {
            showWebsiteTooltip();
            preferenceHelper.setAboutWebsiteTooltipShown();
        }
    }

    private void setImageWidths(View google, View facebook, View twitter, View email) {
        final int SOCIAL_BUTTON_COUNT = 4;

        // Get usable screen width
        Rect displayRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRect);
        int buttonWidth = displayRect.width() / SOCIAL_BUTTON_COUNT;

        // Get and set LayoutParams
        ViewGroup.LayoutParams params = google.getLayoutParams();
        params.width = buttonWidth;

        // Set LayoutParams for each button
        google.setLayoutParams(params);
        facebook.setLayoutParams(params);
        twitter.setLayoutParams(params);
        email.setLayoutParams(params);
    }

    private void setImageLinks(View google, View facebook, View twitter, View email) {
        // JCalc icon => machonesoftware.com/projects.html
        findViewById(R.id.image_jcalc).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.machonesoftware.com/projects.html")));
            }
        });

        // Mach One Software icon => machonesoftware.com
        findViewById(R.id.image_machone).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.machonesoftware.com")));
            }
        });

        // Google Play => JCalc listing (Google Play app, website)
        google.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.machone.jcalc")));
                } catch (Exception ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.machone.jcalc")));
                }
            }
        });

        // Facebook => Mach One Software page (Facebook app, website)
        facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/175611059835415")));
                } catch (Exception ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MachOneSoftware")));
                }
            }
        });

        // Twitter => Mach One Software profile (Twitter app, website)
        twitter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=MachOneSoftware")));
                } catch (Exception ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MachOneSoftware")));
                }
            }
        });

        // Email => jcalc@machonesoftware.com
        email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:jcalc@machonesoftware.com?subject=JCalc%20Feedback")));
            }
        });
    }

    public void showWebsiteTooltip() {
        ViewTooltip
                .on(findViewById(R.id.image_machone))
                .autoHide(true, 5000)
                .clickToHide(true)
                .position(ViewTooltip.Position.BOTTOM)
                .text(getString(R.string.tooltip_new_feature_about_website))
                .color(Color.BLACK)
                .textColor(Color.WHITE)
                .show();
    }
}
