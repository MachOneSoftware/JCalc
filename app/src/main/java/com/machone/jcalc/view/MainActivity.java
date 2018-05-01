package com.machone.jcalc.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.machone.jcalc.R;
import com.machone.jcalc.helper.Calculator;
import com.machone.jcalc.helper.Operators;
import com.machone.jcalc.helper.PreferenceHelper;
import com.machone.jcalc.view.tipcalc.TipCalcActivity;

public class MainActivity extends AppCompatActivity {
    private String currentOperand = "";
    private char lastChar = '\0';
    private boolean expressionIsEquals = false;

    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);

        // Disable soft keyboard
        if (Build.VERSION.SDK_INT >= 21)
            input.setShowSoftInputOnFocus(false);

        registerClickListeners();
        showWhatsNew();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    private void showWhatsNew() {
        PreferenceHelper preferences = PreferenceHelper.getInstance(this);
        int saved = preferences.getSavedVersionCode();
        int current = preferences.saveCurrentVersionCode(this);

        if (current > saved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.whats_new)
                    .setMessage(R.string.whats_new_text)
                    .setNeutralButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            builder.create().show();
        }
    }

    private void registerClickListeners() {
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
        input.setText("");
        lastChar = '\0';
        currentOperand = "";
        expressionIsEquals = false;
    }
}
