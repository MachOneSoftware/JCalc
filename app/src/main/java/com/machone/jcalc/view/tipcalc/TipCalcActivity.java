package com.machone.jcalc.view.tipcalc;

import android.content.pm.ShortcutManager;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.machone.jcalc.BuildConfig;
import com.machone.jcalc.R;
import com.machone.jcalc.helper.PreferenceHelper;
import com.machone.jcalc.view.extension.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.Random;

public class TipCalcActivity extends AppCompatActivity implements TipInputFragment.OnTipInputListener {
    private final String TAG = "TipCalcActivity";
    private final int TIP_INPUT_FRAGMENT = 0;
    private final int TIP_OUTPUT_FRAGMENT = 1;

    private ArrayList<ViewTooltip> tooltips;
    private TextView subtotalTextView;
    private NonSwipeableViewPager viewPager;
    private AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 25)
            this.getSystemService(ShortcutManager.class).reportShortcutUsed(getString(R.string.shortcut_tipcalc_id));

        setContentView(R.layout.activity_tip_calc);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subtotalTextView = findViewById(R.id.textview_subtotal_input);
        viewPager = findViewById(R.id.container);

        initializeBannerAd();
        setupViewPager(viewPager);

        subtotalTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == TIP_OUTPUT_FRAGMENT)
                    reset();
            }
        });
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
            TipOutputFragment fragment = ((TipOutputFragment) ((FragmentStatePagerAdapter) viewPager.getAdapter()).getItem(TIP_OUTPUT_FRAGMENT));
            fragment.setTipOutput(subtotalText);

            tooltips = new ArrayList<>();
            PreferenceHelper preferenceHelper = PreferenceHelper.getInstance(this);
            if (!preferenceHelper.getCustomTipTooltipShown()) {
                ViewTooltip t = fragment.getCustomTipTooltip();
                t.show();
                tooltips.add(t);
                preferenceHelper.setCustomTipTooltipShown();
            }
            if (!preferenceHelper.getTapToResetTooltipShown()) {
                ViewTooltip t = getTapToResetTooltip();
                t.show();
                tooltips.add(t);
                preferenceHelper.setTapToResetTooltipShown();
            }
        }
    }

    @Override
    public void setContainerWidth(int width) {
        NonSwipeableViewPager container = findViewById(R.id.container);
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.width = width;
        container.setLayoutParams(params);
    }

    private void reset() {
        for (int i = 0; i < tooltips.size(); i++)
            tooltips.get(i).close();
        viewPager.setCurrentItem(TIP_INPUT_FRAGMENT);
        resetDisplay();
    }

    private void initializeBannerAd() {
        banner = new AdView(this);
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

    public ViewTooltip getTapToResetTooltip() {
        return ViewTooltip
                .on(findViewById(R.id.textview_subtotal_input))
                .autoHide(true, 10000)
                .clickToHide(true)
                .position(ViewTooltip.Position.RIGHT)
                .text(getString(R.string.tooltip_new_feature_tipcalc_tap_to_reset))
                .color(Color.BLACK)
                .textColor(Color.WHITE);
    }
}
