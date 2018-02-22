package com.machone.jcalc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Jordan on 2/1/2018.
 */

public class Calculator {
    public static String evaluateExpression(String expression){
        ArrayList<String> postfix = convertToPostfix(expression);
        Stack<String> calcStack = new Stack<String>();

        for (String token : postfix) {
            char charToken = token.charAt(0);
            if ((charToken >= '0' && charToken <= '9') || charToken == Operators.NEGATIVE)
                calcStack.push(token);
            else {
                BigDecimal num2 = BigDecimal.valueOf(Double.parseDouble(calcStack.pop()));
                BigDecimal num1 = BigDecimal.valueOf(Double.parseDouble(calcStack.pop()));
                BigDecimal result = BigDecimal.ZERO;
                if (charToken == '+')
                    result = num1.add(num2);
                else if (charToken == Operators.MINUS)
                    result = num1.subtract(num2);
                else if (charToken == Operators.MULTIPLY)
                    result = num1.multiply(num2);
                else if (charToken == Operators.DIVIDE)
                    result = num1.divide(num2, 7, BigDecimal.ROUND_HALF_UP);

                String push = result.stripTrailingZeros().toPlainString();
                if (push.equals("0.0")) push = "0";
                calcStack.push(push);
            }
        }
        return calcStack.pop();
    }

    private static ArrayList<String> convertToPostfix(String infixExpression){
        Stack<Character> opStack = new Stack<Character>();
        ArrayList<String> postfix = new ArrayList<String>();

        char[] split = infixExpression.toCharArray();
        char prevChar = '\0';
        for (char c : split) {
            if ((c >= '0' && c <= '9') || c == '.' || c == Operators.NEGATIVE) {
                // Handle understood multiplication ( (x)y )
                if (prevChar == ')') {
                    handleOperator(Operators.MULTIPLY, opStack, postfix);
                    prevChar = Operators.MULTIPLY;
                }
                String num = "";
                if ((prevChar >= '0' && prevChar <= '9') || prevChar == '.' || prevChar == Operators.NEGATIVE)
                    num = postfix.remove(postfix.size() - 1);
                postfix.add(num + String.valueOf(c));
            }
            else if (c == '(') {
                // Handle negative quantity -(x+y)
                if (prevChar == Operators.NEGATIVE) {
                    postfix.add(postfix.remove(postfix.size() - 1) + "1");
                    prevChar = '1';
                }
                // Handle understood multiplication ( x(y) or (x)(y) )
                if ((prevChar >= '0' && prevChar <= '9') || prevChar == '.' || prevChar == ')') {
                    handleOperator(Operators.MULTIPLY, opStack, postfix);
                    prevChar = Operators.MULTIPLY;
                }
                opStack.push(c);
            }
            else if (c == ')') {
                char pop = opStack.pop();
                while (pop != '(') {
                    postfix.add(String.valueOf(pop));
                    pop = opStack.pop();
                }
            }
            else
                handleOperator(c, opStack, postfix);

            prevChar = c;
        }
        while (!opStack.isEmpty())
            postfix.add(String.valueOf(opStack.pop()));

        return postfix;
    }

    private static void handleOperator(char token, Stack<Character> opStack, ArrayList<String> postfix){
        if (!opStack.isEmpty()) {
            char prev = opStack.peek();
            while (((token == Operators.PLUS || token == Operators.MINUS) && prev != '(')
                    || ((token == Operators.MULTIPLY || token == Operators.DIVIDE) && (prev == Operators.MULTIPLY || prev == Operators.DIVIDE))) {
                postfix.add(String.valueOf(opStack.pop()));
                if (opStack.isEmpty()) break;
                prev = opStack.peek();
            }
        }
        opStack.push(token);
    }
}
