package com.machone.jcalc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String expression = "";
    private String currentOperand = "";
    private char lastChar = '\0';
    private boolean expressionIsEquals = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerClickListeners();
    }

    private void registerClickListeners() {
        View.OnClickListener buttonListener = new View.OnClickListener(){
            public void onClick(View v){
                String buttonText = ((Button)v).getText().toString();
                expression = expression.trim();

                if (expressionIsEquals && buttonText.matches("[^0-9.()]+")) {
                    if (currentOperand.equals("UNDEF") && !buttonText.equals("C") && !buttonText.equals("CE")) return;
                    expressionIsEquals = false;
                }

                // Handle numbers/decimal
                if (buttonText.matches("[0-9.]")){
                    if (expressionIsEquals) clear();
                    if (currentOperand.equals("0") && !buttonText.equals(".")) {
                        currentOperand = "";
                        expression = expression.substring(0, expression.length() - 1);
                    }
                    if (buttonText.equals(".")){
                        if (currentOperand.isEmpty() || currentOperand.equals(String.valueOf(Operators.NEGATIVE))) {
                            currentOperand += "0";
                            expression += "0";
                        }
                        else if (currentOperand.contains(".")) return;
                    }
                    currentOperand += buttonText;
                }
                // Handle non-minus operators
                else if (buttonText.matches("[" + Operators.PLUS +
                        Operators.MULTIPLY +
                        Operators.DIVIDE + "]")){
                    if (lastChar == '\0' || currentOperand.equals(String.valueOf(Operators.NEGATIVE)) || lastChar == '(') return; // Must enter a number first
                    // Last character is already an operator, negative, or decimal
                    if ((lastChar < '0' || lastChar > '9') && lastChar != ')')
                        expression = expression.substring(0, expression.length() - 1);

                    currentOperand = "";
                }
                // Handle minus/negative
                else if (buttonText.matches("[" + Operators.MINUS +
                        Operators.NEGATIVE + "]")){
                    if (currentOperand.isEmpty() && lastChar != ')') { // negative intent
                        buttonText = String.valueOf(Operators.NEGATIVE);
                        currentOperand += buttonText;
                    }
                    else { // minus intent
                        if (currentOperand.equals(String.valueOf(Operators.NEGATIVE))) return;
                        if (lastChar == '.')
                            expression = expression.substring(0, expression.length() - 1);
                        buttonText = String.valueOf(Operators.MINUS);
                        currentOperand = "";
                    }
                }
                // Handle left parenthesis
                else if (buttonText.equals("(")){
                    if (expressionIsEquals) clear();
                    if (lastChar == '.')
                        expression = expression.substring(0, expression.length() - 1);
                    currentOperand = "";
                }
                // Handle right parenthesis
                else if (buttonText.equals(")")){
                    if ((currentOperand.isEmpty() && lastChar != ')') ||
                            currentOperand.equals(String.valueOf(Operators.NEGATIVE)) ||
                            expressionIsEquals)
                        return;

                    if (lastChar == '.')
                        expression = expression.substring(0, expression.length() - 1);
                    currentOperand = "";
                }
                // Handle clear all
                else if (buttonText.equals("C"))
                    clear();
                // Handle clear entry
                else if (buttonText.equals("CE")){
                    if (currentOperand.isEmpty()) return;
                    expression = expression.substring(0, expression.lastIndexOf(currentOperand));
                    currentOperand = "";
                    try {
                        lastChar = expression.charAt(expression.length() - 1);
                    } catch (Exception ex){
                        lastChar = '\0';
                    }
                }
                // Handle equals
                else if (buttonText.equals("=")){
                    if (!expression.equals(currentOperand)) {
                        // Count parentheses
                        int left = 0, right = 0;
                        for (char c : expression.toCharArray()) {
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
                            expression = Calculator.evaluateExpression(expression);
                            lastChar = expression.charAt(expression.length() - 1);
                        } catch (ArithmeticException ex) {
                            expression = "UNDEF";
                            lastChar = '\0';
                            Toast.makeText(MainActivity.this, "Cannot divide by 0", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (currentOperand.equals("-0"))
                        expression = "0";

                    currentOperand = expression;
                    expressionIsEquals = true;
                }

                if (!buttonText.equals("=") && !buttonText.equals("C") && !buttonText.equals("CE")) {
                    lastChar = buttonText.charAt(0);
                    expression += buttonText;
                }

                if (expression.length() > 8)
                    expression += "  ";

                ((TextView) findViewById(R.id.input)).setText(expression);
                ((HorizontalScrollView) findViewById(R.id.horizontalScrollView)).fullScroll(HorizontalScrollView.FOCUS_RIGHT);
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

    private void clear(){
        expression = "";
        lastChar = '\0';
        currentOperand = "";
        expressionIsEquals = false;
    }
}
