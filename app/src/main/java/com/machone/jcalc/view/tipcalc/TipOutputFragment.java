package com.machone.jcalc.view.tipcalc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.machone.jcalc.R;
import com.machone.jcalc.helper.Calculator;
import com.machone.jcalc.helper.Operators;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTipOutputListener} interface
 * to handle interaction events.
 */
public class TipOutputFragment extends Fragment {
    private OnTipOutputListener listener;
    private String subtotal;
    private EditText customPercentage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tip_output, container, false);
        registerClickListeners(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTipOutputListener) {
            listener = (OnTipOutputListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTipOutputListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnTipOutputListener {
        void onResetButtonPressed();

        void onDoneButtonPressed();
    }

    private void registerClickListeners(final View view) {
        view.findViewById(R.id.button_reset).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onResetButtonPressed();
                customPercentage.setText(getString(R.string.tip_custom_percent_default));
                View focus = view.findFocus();
                if (focus != null) focus.clearFocus();
            }
        });

        view.findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onDoneButtonPressed();
            }
        });

        customPercentage = view.findViewById(R.id.customPercentage);
        customPercentage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    setTipOutput();
                    return false;
                }
                // Ignore "Next" press (return true); otherwise return false
                else return actionId == EditorInfo.IME_ACTION_NEXT;
            }
        });
    }

    private void setTipOutput() {
        Activity activity = getActivity();

        ((TextView) activity.findViewById(R.id.textview_tip15_output))
                .setText(getResources().getString(
                        R.string.tip_output,
                        Calculator.evaluateExpression(this.subtotal + "0.15", 2)
                ));
        ((TextView) activity.findViewById(R.id.textview_tip175_output))
                .setText(getResources().getString(
                        R.string.tip_output,
                        Calculator.evaluateExpression(this.subtotal + "0.175", 2)
                ));
        ((TextView) activity.findViewById(R.id.textview_tip20_output))
                .setText(getResources().getString(
                        R.string.tip_output,
                        Calculator.evaluateExpression(this.subtotal + "0.20", 2)
                ));

        // Parse a percentage out of the entered value
        String custom = customPercentage.getText().toString();
        if (custom.equals("")) {
            custom = getString(R.string.tip_custom_percent_default);
        }

        if (custom.endsWith(".0") || custom.endsWith(".") || !custom.contains("."))
            custom = Calculator.evaluateExpression(custom, 0);
        else
            custom = Calculator.evaluateExpression(custom, 1);

        customPercentage.setText(custom);
        String percent = Calculator.evaluateExpression(custom + Operators.DIVIDE + "100");
        ((TextView) activity.findViewById(R.id.textview_custom_output))
                .setText(getResources().getString(
                        R.string.tip_output,
                        Calculator.evaluateExpression(this.subtotal + percent, 2)
                ));
    }

    public void setTipOutput(String subtotal) {
        // Remove dollar sign and add operator
        this.subtotal = subtotal.substring(1) + Operators.MULTIPLY;
        setTipOutput();
    }
}
