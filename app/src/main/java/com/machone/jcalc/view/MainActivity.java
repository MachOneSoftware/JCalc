package com.machone.jcalc.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.machone.jcalc.BuildConfig;
import com.machone.jcalc.R;
import com.machone.jcalc.helper.Calculator;
import com.machone.jcalc.helper.Operators;
import com.machone.jcalc.helper.PreferenceHelper;
import com.machone.jcalc.helper.VersionMap;
import com.machone.jcalc.view.tipcalc.TipCalcActivity;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private String currentOperand = "";
    private char lastChar = '\0';
    private boolean expressionIsEquals = false;

    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);
        // Disable soft keyboard
        if (Build.VERSION.SDK_INT >= 21)
            input.setShowSoftInputOnFocus(false);

        initializeBannerAd();
        setButtonHeight();
        registerClickListeners();

        showWhatsNew();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuitem_tipcalc:
                intent = new Intent(getBaseContext(), TipCalcActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                break;
            case R.id.menuitem_about:
                intent = new Intent(getBaseContext(), AboutActivity.class);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        startActivity(intent);
        return true;
    }

    private void initializeBannerAd() {
        Log.d(TAG, "initializeBannerAd");

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        AdView banner = new AdView(this);
        banner.setAdSize(AdSize.SMART_BANNER);
        banner.setAdUnitId(getString(BuildConfig.DEBUG ? R.string.admob_test_unit_id : R.string.admob_main_activity_unit_id));

        LinearLayout layout = findViewById(R.id.linearlayout);
        layout.addView(banner);
        banner.loadAd(new AdRequest.Builder().build());
    }

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
        findViewById(R.id.decimalBtn).setLayoutParams(params);
        findViewById(R.id.plusBtn).setLayoutParams(params);
        findViewById(R.id.minusBtn).setLayoutParams(params);
        findViewById(R.id.multBtn).setLayoutParams(params);
        findViewById(R.id.divBtn).setLayoutParams(params);
        findViewById(R.id.eqBtn).setLayoutParams(params);
        findViewById(R.id.leftParenthBtn).setLayoutParams(params);
        findViewById(R.id.rightParenthBtn).setLayoutParams(params);
        findViewById(R.id.clearBtn).setLayoutParams(params);
        findViewById(R.id.clearEntryBtn).setLayoutParams(params);
    }

    private void showWhatsNew() {
        Log.d(TAG, "showWhatsNew");

        PreferenceHelper preferences = PreferenceHelper.getInstance(this);
        int saved = preferences.getSavedVersionCode();
        int current = preferences.saveCurrentVersionCode(this);

        if (current > saved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(saved == 0 ? R.string.whats_new_title_first_install : R.string.whats_new_title_update)
                    .setMessage(getResources().getString(
                            R.string.whats_new_text,
                            VersionMap.getVersionName(current),
                            getResources().getString(
                                    saved == 0 ? R.string.whats_new_first_install : R.string.whats_new_update)))
                    .setNeutralButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            builder.create().show();
        }
    }

    private void registerClickListeners() {
        Log.d(TAG, "registerClickListeners");

        View.OnClickListener buttonListener = new View.OnClickListener() {
            public void onClick(View v) {
                String buttonText = ((Button) v).getText().toString();

                if (expressionIsEquals && buttonText.matches("[^0-9.()]+")) {
                    if (currentOperand.equals("UNDEF") && !buttonText.equals("C") && !buttonText.equals("CE"))
                        return;
                    expressionIsEquals = false;
                }

                // Handle numbers/decimal
                if (buttonText.matches("[0-9.]")) {
                    if (expressionIsEquals) clear();
                    if (currentOperand.equals("0") && !buttonText.equals(".")) {
                        currentOperand = "";
                        String currIn = input.getText().toString();
                        input.setText(currIn.substring(0, currIn.length() - 1));
                    }
                    if (buttonText.equals(".")) {
                        if (currentOperand.isEmpty() || currentOperand.equals(String.valueOf(Operators.NEGATIVE))) {
                            currentOperand += "0";
                            input.setText(input.getText() + "0");
                        } else if (currentOperand.contains(".")) return;
                    }
                    currentOperand += buttonText;
                }
                // Handle non-minus operators
                else if (buttonText.matches("[" + Operators.PLUS +
                        Operators.MULTIPLY +
                        Operators.DIVIDE + "]")) {
                    if (lastChar == '\0' || currentOperand.equals(String.valueOf(Operators.NEGATIVE)) || lastChar == '(')
                        return; // Must enter a number first
                    // Last character is already an operator, negative, or decimal
                    if ((lastChar < '0' || lastChar > '9') && lastChar != ')') {
                        String currIn = input.getText().toString();
                        input.setText(currIn.substring(0, currIn.length() - 1));
                    }

                    currentOperand = "";
                }
                // Handle minus/negative
                else if (buttonText.matches("[" + Operators.MINUS +
                        Operators.NEGATIVE + "]")) {
                    if (currentOperand.isEmpty() && lastChar != ')') { // negative intent
                        buttonText = String.valueOf(Operators.NEGATIVE);
                        currentOperand += buttonText;
                    } else { // minus intent
                        if (currentOperand.equals(String.valueOf(Operators.NEGATIVE))) return;
                        if (lastChar == '.') {
                            String currIn = input.getText().toString();
                            input.setText(currIn.substring(0, currIn.length() - 1));
                        }
                        buttonText = String.valueOf(Operators.MINUS);
                        currentOperand = "";
                    }
                }
                // Handle left parenthesis
                else if (buttonText.equals("(")) {
                    if (expressionIsEquals) clear();
                    if (lastChar == '.') {
                        String currIn = input.getText().toString();
                        input.setText(currIn.substring(0, currIn.length() - 1));
                    }
                    currentOperand = "";
                }
                // Handle right parenthesis
                else if (buttonText.equals(")")) {
                    if ((currentOperand.isEmpty() && lastChar != ')') ||
                            currentOperand.equals(String.valueOf(Operators.NEGATIVE)) ||
                            expressionIsEquals)
                        return;

                    if (lastChar == '.') {
                        String currIn = input.getText().toString();
                        input.setText(currIn.substring(0, currIn.length() - 1));
                    }
                    currentOperand = "";
                }
                // Handle clear all
                else if (buttonText.equals("C"))
                    clear();
                    // Handle clear entry
                else if (buttonText.equals("CE")) {
                    if (currentOperand.isEmpty()) return;
                    String currIn = input.getText().toString();
                    input.setText(currIn.substring(0, currIn.lastIndexOf(currentOperand)));
                    currentOperand = "";
                    try {
                        currIn = input.getText().toString();
                        lastChar = currIn.charAt(currIn.length() - 1);
                    } catch (Exception ex) {
                        lastChar = '\0';
                    }
                }
                // Handle equals
                else if (buttonText.equals("=")) {
                    String currIn = input.getText().toString();
                    if (!currIn.equals(currentOperand)) {
                        // Count parentheses
                        int left = 0, right = 0;
                        for (char c : currIn.toCharArray()) {
                            if (c == '(') left++;
                            else if (c == ')') right++;
                        }

                        if (((lastChar < '0' || lastChar > '9') && lastChar != ')' && lastChar != '.') ||
                                lastChar == '\0' || currentOperand.equals(String.valueOf(Operators.NEGATIVE)) ||
                                left != right) {
                            Toast.makeText(MainActivity.this, "Invalid expression", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            input.setText(Calculator.evaluateExpression(currIn));
                            currIn = input.getText().toString();
                            lastChar = currIn.charAt(currIn.length() - 1);
                        } catch (ArithmeticException ex) {
                            input.setText("UNDEF");
                            lastChar = '\0';
                            Toast.makeText(MainActivity.this, "Cannot divide by 0", Toast.LENGTH_SHORT).show();
                        }
                    } else if (currentOperand.equals("-0"))
                        input.setText("0");

                    currentOperand = input.getText().toString();
                    expressionIsEquals = true;
                }

                if (!buttonText.equals("=") && !buttonText.equals("C") && !buttonText.equals("CE")) {
                    lastChar = buttonText.charAt(0);
                    input.setText(input.getText() + buttonText);
                }

                // Set EditText cursor to end of input
                input.setSelection(input.length());
            }
        };

        findViewById(R.id.zero).setOnClickListener(buttonListener);
        findViewById(R.id.one).setOnClickListener(buttonListener);
        findViewById(R.id.two).setOnClickListener(buttonListener);
        findViewById(R.id.three).setOnClickListener(buttonListener);
        findViewById(R.id.four).setOnClickListener(buttonListener);
        findViewById(R.id.five).setOnClickListener(buttonListener);
        findViewById(R.id.six).setOnClickListener(buttonListener);
        findViewById(R.id.seven).setOnClickListener(buttonListener);
        findViewById(R.id.eight).setOnClickListener(buttonListener);
        findViewById(R.id.nine).setOnClickListener(buttonListener);
        findViewById(R.id.decimalBtn).setOnClickListener(buttonListener);

        findViewById(R.id.plusBtn).setOnClickListener(buttonListener);
        findViewById(R.id.minusBtn).setOnClickListener(buttonListener);
        findViewById(R.id.multBtn).setOnClickListener(buttonListener);
        findViewById(R.id.divBtn).setOnClickListener(buttonListener);
        findViewById(R.id.eqBtn).setOnClickListener(buttonListener);

        findViewById(R.id.leftParenthBtn).setOnClickListener(buttonListener);
        findViewById(R.id.rightParenthBtn).setOnClickListener(buttonListener);

        findViewById(R.id.clearBtn).setOnClickListener(buttonListener);
        findViewById(R.id.clearEntryBtn).setOnClickListener(buttonListener);
    }

    private void clear() {
        Log.d(TAG, "clear");

        input.setText("");
        lastChar = '\0';
        currentOperand = "";
        expressionIsEquals = false;
    }
}
