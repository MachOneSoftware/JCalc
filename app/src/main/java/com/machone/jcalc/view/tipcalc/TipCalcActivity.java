package com.machone.jcalc.view.tipcalc;

import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.machone.jcalc.BuildConfig;
import com.machone.jcalc.R;
import com.machone.jcalc.view.extension.NonSwipeableViewPager;

import java.util.Random;

public class TipCalcActivity extends AppCompatActivity implements TipInputFragment.OnTipInputListener, TipOutputFragment.OnTipOutputListener {
    private final int TIP_INPUT_FRAGMENT = 0;
    private final int TIP_OUTPUT_FRAGMENT = 1;

    private TextView subtotalTextView;
    private NonSwipeableViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subtotalTextView = findViewById(R.id.textview_subtotal_input);
        viewPager = findViewById(R.id.container);

        // TODO set height of view pager
        // TODO calculate distance between bottom of subtotal and bottom of layout

        initializeBannerAd();
        setupViewPager(viewPager);
    }

    @Override
    public void onSubtotalChanged(String formattedSubtotal) {
        subtotalTextView.setText("$" + formattedSubtotal);
    }

    @Override
    public void onSubmitButtonPressed() {
        String subtotalText = subtotalTextView.getText().toString();
        if (subtotalText.equals(getString(R.string.tip_default_output)))
            Toast.makeText(this, getString(R.string.tip_enter_value), Toast.LENGTH_SHORT).show();
        else {
            viewPager.setCurrentItem(TIP_OUTPUT_FRAGMENT);
            ((TipOutputFragment) ((FragmentStatePagerAdapter) viewPager.getAdapter()).getItem(1)).setTipOutput(subtotalText);
        }
    }

    @Override
    public void onResetButtonPressed() {
        viewPager.setCurrentItem(TIP_INPUT_FRAGMENT);
        resetDisplay();
    }

    @Override
    public void onDoneButtonPressed() {
        finish();
    }

    private void initializeBannerAd(){
        AdView banner = new AdView(this);
        if (Build.VERSION.SDK_INT >= 17) {
            banner.setId(View.generateViewId());
        } else {
            banner.setId(new Random(System.currentTimeMillis()).nextInt());
        }
        banner.setAdSize(AdSize.SMART_BANNER);
        banner.setAdUnitId(getString(BuildConfig.DEBUG ? R.string.admob_test_unit_id : R.string.admob_tipcalc_activity_unit_id));

        ConstraintLayout layout = findViewById(R.id.layout);
        layout.addView(banner);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(banner.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        constraintSet.applyTo(layout);

        banner.loadAd(new AdRequest.Builder().build());
    }

    private void resetDisplay() {
        subtotalTextView.setText(getString(R.string.tip_default_output));
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            Fragment[] tipFragments = {new TipInputFragment(), new TipOutputFragment()};

            @Override
            public int getCount() {
                return tipFragments.length;
            }

            @Override
            public Fragment getItem(int position) {
                return tipFragments[position];
            }
        });
    }
}
