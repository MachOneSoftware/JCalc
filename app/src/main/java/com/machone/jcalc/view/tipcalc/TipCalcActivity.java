package com.machone.jcalc.view.tipcalc;

import android.content.SharedPreferences;
import android.content.pm.ShortcutManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TableLayout;
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

import java.util.Random;

public class TipCalcActivity extends AppCompatActivity implements TipInputFragment.OnTipInputListener, TipOutputFragment.OnTipOutputListener {
    private final String TAG = "TipCalcActivity";
    private final int TIP_INPUT_FRAGMENT = 0;
    private final int TIP_OUTPUT_FRAGMENT = 1;

    private TextView subtotalTextView;
    private NonSwipeableViewPager viewPager;

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

        //setButtonHeight();

        // TODO set height of view pager
        // TODO calculate distance between bottom of subtotal and bottom of layout



        final ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.layout);
        final View s = findViewById(R.id.textview_subtotal_input);

        s.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"Height: " + s.getHeight());
            }
        });
//        ViewTreeObserver observer = layout.getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        Log.d(TAG, "Height: " + s.getHeight());
//                    }
//                }
//        );


        int h = findViewById(R.id.textview_subtotal_input).getHeight();
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
            TipOutputFragment fragment = ((TipOutputFragment)((FragmentStatePagerAdapter) viewPager.getAdapter()).getItem(TIP_OUTPUT_FRAGMENT));
            fragment.setTipOutput(subtotalText);

            PreferenceHelper preferenceHelper = PreferenceHelper.getInstance(this);
            if (!preferenceHelper.getCustomTipTooltipShown()) {
                fragment.showCustomTipTooltip();
                preferenceHelper.setCustomTipTooltipShown();
            }
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

    private void initializeBannerAd() {
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
/*
    private void setButtonHeight() {
        Log.d(TAG, "setButtonHeight");

        // Get View heights to subtract from screen height
        final int BUTTON_ROWS = ((TableLayout) findViewById(R.id.table_buttons)).getChildCount();
        final int INPUT_HEIGHT = getResources().getDimensionPixelOffset(R.dimen.mainInputHeight);
        final int INPUT_MARGIN = ((ViewGroup.MarginLayoutParams) input.getLayoutParams()).bottomMargin;
        final int BANNER_AD_HEIGHT = getResources().getDimensionPixelOffset(R.dimen.banner_ad_height);

        // ActionBar height
        int actionbarHeight = 0;
        int usableHeight;
        TypedValue typedValue = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true))
            actionbarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());

        // Soft key height
        // getRealMetrics is only available with API 17+
        int softKeyHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                softKeyHeight = realHeight - usableHeight;
        } else {
            // Get usable screen height
            Rect displayRect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRect);
            usableHeight = displayRect.height();
        }

        // Get status bar height to add
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        // Calculate usable space for buttons
        int usable = usableHeight - actionbarHeight - INPUT_HEIGHT - INPUT_MARGIN - BANNER_AD_HEIGHT - softKeyHeight + statusBarHeight;
        int buttonHeight = usable / BUTTON_ROWS;

        // Get and set LayoutParams
        Button zero = findViewById(R.id.zero);
        ViewGroup.LayoutParams params = zero.getLayoutParams();
        params.height = buttonHeight;

        // Set LayoutParams for each button
        zero.setLayoutParams(params);
        findViewById(R.id.one).setLayoutParams(params);
        findViewById(R.id.two).setLayoutParams(params);
        findViewById(R.id.three).setLayoutParams(params);
        findViewById(R.id.four).setLayoutParams(params);
        findViewById(R.id.five).setLayoutParams(params);
        findViewById(R.id.six).setLayoutParams(params);
        findViewById(R.id.seven).setLayoutParams(params);
        findViewById(R.id.eight).setLayoutParams(params);
        findViewById(R.id.nine).setLayoutParams(params);
        findViewById(R.id.cancel).setLayoutParams(params);
        findViewById(R.id.submit).setLayoutParams(params);
    }*/
}
