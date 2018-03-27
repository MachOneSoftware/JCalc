package com.machone.jcalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TipCalcActivity extends AppCompatActivity {
    private String subtotal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        registerClickListeners();
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

    private void registerClickListeners() {
        View.OnClickListener numberListener = new View.OnClickListener() {
            public void onClick(View v) {
                String buttonText = ((Button) v).getText().toString();

                // Ignore leading zeros and only support up to 10 digits
                if ((buttonText.equals("0") && subtotal.length() == 0) ||
                        subtotal.length() == 10)
                    return;

                subtotal += buttonText;
                outToTextView();
            }
        };

        findViewById(R.id.zero).setOnClickListener(numberListener);
        findViewById(R.id.one).setOnClickListener(numberListener);
        findViewById(R.id.two).setOnClickListener(numberListener);
        findViewById(R.id.three).setOnClickListener(numberListener);
        findViewById(R.id.four).setOnClickListener(numberListener);
        findViewById(R.id.five).setOnClickListener(numberListener);
        findViewById(R.id.six).setOnClickListener(numberListener);
        findViewById(R.id.seven).setOnClickListener(numberListener);
        findViewById(R.id.eight).setOnClickListener(numberListener);
        findViewById(R.id.nine).setOnClickListener(numberListener);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setElementVisibilities();
                String dec = getDecimal() + Operators.MULTIPLY;

                TextView text15 = (TextView)findViewById(R.id.text_tip15);
                text15.setText("$" + Calculator.evaluateExpression(dec + "0.15", 2));

                TextView text175 = (TextView)findViewById(R.id.text_tip175);
                text175.setText("$" + Calculator.evaluateExpression(dec + "0.175", 2));

                TextView text20 = (TextView)findViewById(R.id.text_tip20);
                text20.setText("$" + Calculator.evaluateExpression(dec + "0.2", 2));

                TextView text25 = (TextView)findViewById(R.id.text_tip25);
                text25.setText("$" + Calculator.evaluateExpression(dec + "0.25", 2));
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                subtotal = "";
                outToTextView();
            }
        });

        findViewById(R.id.resetBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                subtotal = "";
                outToTextView();
                setElementVisibilities();
            }
        });
    }

    private String getDecimal() {
        String out;
        if (subtotal.length() == 0)
            out = "000";
        else if (subtotal.length() == 1)
            out = "00" + subtotal;
        else if (subtotal.length() == 2)
            out = "0" + subtotal;
        else
            out = subtotal;

        String dollars = out.substring(0, out.length() - 2);
        String cents = out.substring(out.length() - 2);

        return dollars + "." + cents;
    }

    private void outToTextView() {
        ((TextView) findViewById(R.id.subtotal)).setText("$" + getDecimal());
    }

    private void setElementVisibilities() {
        int a = findViewById(R.id.zero).getVisibility();
        int b = findViewById(R.id.suggestedTip).getVisibility();

        findViewById(R.id.zero).setVisibility(b);
        findViewById(R.id.one).setVisibility(b);
        findViewById(R.id.two).setVisibility(b);
        findViewById(R.id.three).setVisibility(b);
        findViewById(R.id.four).setVisibility(b);
        findViewById(R.id.five).setVisibility(b);
        findViewById(R.id.six).setVisibility(b);
        findViewById(R.id.seven).setVisibility(b);
        findViewById(R.id.eight).setVisibility(b);
        findViewById(R.id.nine).setVisibility(b);
        findViewById(R.id.cancel).setVisibility(b);
        findViewById(R.id.submit).setVisibility(b);

        findViewById(R.id.suggestedTip).setVisibility(a);
        findViewById(R.id.text_percent15).setVisibility(a);
        findViewById(R.id.text_percent175).setVisibility(a);
        findViewById(R.id.text_percent20).setVisibility(a);
        findViewById(R.id.text_percent25).setVisibility(a);
        findViewById(R.id.resetBtn).setVisibility(a);
        findViewById(R.id.text_tip15).setVisibility(a);
        findViewById(R.id.text_tip175).setVisibility(a);
        findViewById(R.id.text_tip20).setVisibility(a);
        findViewById(R.id.text_tip25).setVisibility(a);
    }
}
