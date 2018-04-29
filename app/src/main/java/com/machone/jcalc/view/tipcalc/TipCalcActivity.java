package com.machone.jcalc.view.tipcalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.machone.jcalc.R;
import com.machone.jcalc.view.AboutActivity;
import com.machone.jcalc.view.extension.NonSwipeableViewPager;

public class TipCalcActivity extends AppCompatActivity implements TipInputFragment.OnTipInputListener, TipOutputFragment.OnTipOutputListener {
    private final int TIP_INPUT_FRAGMENT = 0;
    private final int TIP_OUTPUT_FRAGMENT = 1;

    private TextView subtotalTextView;
    private NonSwipeableViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        subtotalTextView = (TextView) findViewById(R.id.textview_subtotal_input);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.container);

        setupViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem jcalc = menu.findItem(R.id.menuitem_jcalc);
        jcalc.setEnabled(true);
        jcalc.setVisible(true);

        MenuItem tipcalc = menu.findItem(R.id.menuitem_tipcalc);
        tipcalc.setEnabled(false);
        tipcalc.setVisible(false);

        MenuItem about = menu.findItem(R.id.menuitem_about);
        about.setEnabled(true);
        about.setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_jcalc:
                finish();
                break;
            case R.id.menuitem_about:
                Intent intent = new Intent(getBaseContext(), AboutActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onSubtotalChanged(String formattedSubtotal) {
        subtotalTextView.setText("$" + formattedSubtotal);
    }

    @Override
    public void onSubmitButtonPressed() {
        viewPager.setCurrentItem(TIP_OUTPUT_FRAGMENT);
        ((TipOutputFragment)((FragmentStatePagerAdapter)viewPager.getAdapter()).getItem(1)).setTipOutput(subtotalTextView.getText().toString());
    }

    @Override
    public void onResetButtonPressed() {
        viewPager.setCurrentItem(TIP_INPUT_FRAGMENT);
        resetDisplay();
    }

    private void resetDisplay(){
        subtotalTextView.setText(getResources().getString(R.string.tip_default_output));
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
