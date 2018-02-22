package com.machone.jcalc;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTests {
    @Test
    public void negativeQuantity() throws Exception{
        String result = Calculator.evaluateExpression("-(6+5)");
        assertEquals("-11", result);
    }

    @Test
    public void understoodMult1() throws Exception{
        String result = Calculator.evaluateExpression("6(5)");
        assertEquals("30", result);
    }

    @Test
    public void understoodMult2() throws Exception{
        String result = Calculator.evaluateExpression("(6)5");
        assertEquals("30", result);
    }

    @Test
    public void understoodMult3() throws Exception{
        String result = Calculator.evaluateExpression("(6)(5)");
        assertEquals("30", result);
    }

    @Test
    public void simpleAdd() throws Exception {
        String result = Calculator.evaluateExpression("10+5");
        assertEquals("15", result);
    }

    @Test
    public void simpleSub() throws Exception {
        String result = Calculator.evaluateExpression("10" + Operators.MINUS + "5");
        assertEquals("5", result);
    }

    @Test
    public void simpleMult() throws Exception {
        String result = Calculator.evaluateExpression("10" + Operators.MULTIPLY + "5");
        assertEquals("50", result);
    }

    @Test
    public void simpleDiv() throws Exception {
        String result = Calculator.evaluateExpression("10" + Operators.DIVIDE + "5");
        assertEquals("2", result);
    }

    @Test
    public void divideByZero_throwsArithmeticException() throws Exception {
        try {
            Calculator.evaluateExpression("1" + Operators.DIVIDE + "0");
            assertFalse(true);
        }catch (ArithmeticException ex){
            assertTrue(true);
        }
    }

    @Test
    public void simpleAdd_dec1() throws Exception {
        String result = Calculator.evaluateExpression("2.2+2");
        assertEquals("4.2", result);
    }

    @Test
    public void simpleAdd_dec2() throws Exception {
        String result = Calculator.evaluateExpression("2+2.2");
        assertEquals("4.2", result);
    }

    @Test
    public void simpleAdd_dec3() throws Exception {
        String result = Calculator.evaluateExpression("2.2+2.2");
        assertEquals("4.4", result);
    }

    @Test
    public void simpleSub_dec1() throws Exception {
        String result = Calculator.evaluateExpression("2.2" + Operators.MINUS + "2");
        assertEquals("0.2", result);
    }

    @Test
    public void simpleSub_dec2() throws Exception {
        String result = Calculator.evaluateExpression("2" + Operators.MINUS + "2.2");
        assertEquals("-0.2", result);
    }

    @Test
    public void simpleSub_dec3() throws Exception {
        String result = Calculator.evaluateExpression("2.2" + Operators.MINUS + "2.2");
        assertEquals("0", result);
    }

    @Test
    public void simpleMult_dec1() throws Exception {
        String result = Calculator.evaluateExpression("2.2" + Operators.MULTIPLY + "2");
        assertEquals("4.4", result);
    }

    @Test
    public void simpleMult_dec2() throws Exception {
        String result = Calculator.evaluateExpression("2" + Operators.MULTIPLY + "2.2");
        assertEquals("4.4", result);
    }

    @Test
    public void simpleMult_dec3() throws Exception {
        String result = Calculator.evaluateExpression("2.2" + Operators.MULTIPLY + "2.2");
        assertEquals("4.84", result);
    }

    @Test
    public void simpleDiv_dec1() throws Exception {
        String result = Calculator.evaluateExpression("2.2" + Operators.DIVIDE + "2");
        assertEquals("1.1", result);
    }

    @Test
    public void simpleDiv_dec2() throws Exception {
        String result = Calculator.evaluateExpression("2" + Operators.DIVIDE + "2.5");
        assertEquals("0.8", result);
    }

    @Test
    public void simpleDiv_dec3() throws Exception {
        String result = Calculator.evaluateExpression("2.2" + Operators.DIVIDE + "2.2");
        assertEquals("1", result);
    }
    
    @Test
    public void simpleAdd_neg1() throws Exception {
        String result = Calculator.evaluateExpression("-2+2");
        assertEquals("0", result);
    }

    @Test
    public void simpleAdd_neg2() throws Exception {
        String result = Calculator.evaluateExpression("2+-2");
        assertEquals("0", result);
    }

    @Test
    public void simpleAdd_neg3() throws Exception {
        String result = Calculator.evaluateExpression("-2+-2");
        assertEquals("-4", result);
    }

    @Test
    public void simpleSub_neg1() throws Exception {
        String result = Calculator.evaluateExpression("-2" + Operators.MINUS + "2");
        assertEquals("-4", result);
    }

    @Test
    public void simpleSub_neg2() throws Exception {
        String result = Calculator.evaluateExpression("2" + Operators.MINUS + "-2");
        assertEquals("4", result);
    }

    @Test
    public void simpleSub_neg3() throws Exception {
        String result = Calculator.evaluateExpression("-2" + Operators.MINUS + "-2");
        assertEquals("0", result);
    }

    @Test
    public void simpleMult_neg1() throws Exception {
        String result = Calculator.evaluateExpression("-2" + Operators.MULTIPLY + "2");
        assertEquals("-4", result);
    }

    @Test
    public void simpleMult_neg2() throws Exception {
        String result = Calculator.evaluateExpression("2" + Operators.MULTIPLY + "-2");
        assertEquals("-4", result);
    }

    @Test
    public void simpleMult_neg3() throws Exception {
        String result = Calculator.evaluateExpression("-2" + Operators.MULTIPLY + "-2");
        assertEquals("4", result);
    }

    @Test
    public void simpleDiv_neg1() throws Exception {
        String result = Calculator.evaluateExpression("-2" + Operators.DIVIDE + "2");
        assertEquals("-1", result);
    }

    @Test
    public void simpleDiv_neg2() throws Exception {
        String result = Calculator.evaluateExpression("2" + Operators.DIVIDE + "-2");
        assertEquals("-1", result);
    }

    @Test
    public void simpleDiv_neg3() throws Exception {
        String result = Calculator.evaluateExpression("-2" + Operators.DIVIDE + "-2");
        assertEquals("1", result);
    }

    @Test
    public void simpleAdd_mix1() throws Exception {
        String result = Calculator.evaluateExpression("-1.1+1");
        assertEquals("-0.1", result);
    }

    @Test
    public void simpleAdd_mix2() throws Exception {
        String result = Calculator.evaluateExpression("-1+1.1");
        assertEquals("0.1", result);
    }

    @Test
    public void simpleAdd_mix3() throws Exception {
        String result = Calculator.evaluateExpression("-1.1+1.1");
        assertEquals("0", result);
    }
    @Test
    public void simpleAdd_mix4() throws Exception {
        String result = Calculator.evaluateExpression("1.1+-1");
        assertEquals("0.1", result);
    }

    @Test
    public void simpleAdd_mix5() throws Exception {
        String result = Calculator.evaluateExpression("1+-1.1");
        assertEquals("-0.1", result);
    }

    @Test
    public void simpleAdd_mix6() throws Exception {
        String result = Calculator.evaluateExpression("1.1+-1.1");
        assertEquals("0", result);
    }

    @Test
    public void simpleAdd_mix7() throws Exception {
        String result = Calculator.evaluateExpression("-1.1+-1");
        assertEquals("-2.1", result);
    }

    @Test
    public void simpleAdd_mix8() throws Exception {
        String result = Calculator.evaluateExpression("-1+-1.1");
        assertEquals("-2.1", result);
    }

    @Test
    public void simpleAdd_mix9() throws Exception {
        String result = Calculator.evaluateExpression("-1.1+-1.1");
        assertEquals("-2.2", result);
    }

    @Test
    public void simpleSub_mix1() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.MINUS + "1");
        assertEquals("-2.1", result);
    }

    @Test
    public void simpleSub_mix2() throws Exception {
        String result = Calculator.evaluateExpression("-1" + Operators.MINUS + "1.1");
        assertEquals("-2.1", result);
    }

    @Test
    public void simpleSub_mix3() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.MINUS + "1.1");
        assertEquals("-2.2", result);
    }

    @Test
    public void simpleSub_mix4() throws Exception {
        String result = Calculator.evaluateExpression("1.1" + Operators.MINUS + "-1");
        assertEquals("2.1", result);
    }

    @Test
    public void simpleSub_mix5() throws Exception {
        String result = Calculator.evaluateExpression("1" + Operators.MINUS + "-1.1");
        assertEquals("2.1", result);
    }

    @Test
    public void simpleSub_mix6() throws Exception {
        String result = Calculator.evaluateExpression("1.1" + Operators.MINUS + "-1.1");
        assertEquals("2.2", result);
    }

    @Test
    public void simpleSub_mix7() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.MINUS + "-1");
        assertEquals("-0.1", result);
    }

    @Test
    public void simpleSub_mix8() throws Exception {
        String result = Calculator.evaluateExpression("-1" + Operators.MINUS + "-1.1");
        assertEquals("0.1", result);
    }

    @Test
    public void simpleSub_mix9() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.MINUS + "-1.1");
        assertEquals("0", result);
    }

    @Test
    public void simpleMult_mix1() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.MULTIPLY + "1");
        assertEquals("-1.1", result);
    }

    @Test
    public void simpleMult_mix2() throws Exception {
        String result = Calculator.evaluateExpression("-1" + Operators.MULTIPLY + "1.1");
        assertEquals("-1.1", result);
    }

    @Test
    public void simpleMult_mix3() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.MULTIPLY + "1.1");
        assertEquals("-1.21", result);
    }

    @Test
    public void simpleMult_mix4() throws Exception {
        String result = Calculator.evaluateExpression("1.1" + Operators.MULTIPLY + "-1");
        assertEquals("-1.1", result);
    }

    @Test
    public void simpleMult_mix5() throws Exception {
        String result = Calculator.evaluateExpression("1" + Operators.MULTIPLY + "-1.1");
        assertEquals("-1.1", result);
    }

    @Test
    public void simpleMult_mix6() throws Exception {
        String result = Calculator.evaluateExpression("1.1" + Operators.MULTIPLY + "-1.1");
        assertEquals("-1.21", result);
    }

    @Test
    public void simpleMult_mix7() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.MULTIPLY + "-1");
        assertEquals("1.1", result);
    }

    @Test
    public void simpleMult_mix8() throws Exception {
        String result = Calculator.evaluateExpression("-1" + Operators.MULTIPLY + "-1.1");
        assertEquals("1.1", result);
    }

    @Test
    public void simpleMult_mix9() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.MULTIPLY + "-1.1");
        assertEquals("1.21", result);
    }

    @Test
    public void simpleDiv_mix1() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.DIVIDE + "1");
        assertEquals("-1.1", result);
    }

    @Test
    public void simpleDiv_mix2() throws Exception {
        String result = Calculator.evaluateExpression("-1" + Operators.DIVIDE + "1.6");
        assertEquals("-0.625", result);
    }

    @Test
    public void simpleDiv_mix3() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.DIVIDE + "1.1");
        assertEquals("-1", result);
    }

    @Test
    public void simpleDiv_mix4() throws Exception {
        String result = Calculator.evaluateExpression("1.1" + Operators.DIVIDE + "-1");
        assertEquals("-1.1", result);
    }

    @Test
    public void simpleDiv_mix5() throws Exception {
        String result = Calculator.evaluateExpression("1" + Operators.DIVIDE + "-1.6");
        assertEquals("-0.625", result);
    }

    @Test
    public void simpleDiv_mix6() throws Exception {
        String result = Calculator.evaluateExpression("1.1" + Operators.DIVIDE + "-1.1");
        assertEquals("-1", result);
    }

    @Test
    public void simpleDiv_mix7() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.DIVIDE + "-1");
        assertEquals("1.1", result);
    }

    @Test
    public void simpleDiv_mix8() throws Exception {
        String result = Calculator.evaluateExpression("-1" + Operators.DIVIDE + "-1.6");
        assertEquals("0.625", result);
    }

    @Test
    public void simpleDiv_mix9() throws Exception {
        String result = Calculator.evaluateExpression("-1.1" + Operators.DIVIDE + "-1.1");
        assertEquals("1", result);
    }
}