package com.machone.jcalc.view.tipcalc;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private void registerClickListeners(View view) {
        view.findViewById(R.id.button_reset).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onResetButtonPressed();
            }
        });

        view.findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                listener.onDoneButtonPressed();
            }
        });
    }

    public void setTipOutput(String subtotal) {
        // Remove dollar sign
        String sub = subtotal.substring(1) + Operators.MULTIPLY;
        Activity activity = getActivity();

        ((TextView) activity.findViewById(R.id.textview_tip15_output))
                .setText(getResources().getString(
                        R.string.tip_output,
                        Calculator.evaluateExpression(sub + "0.15", 2)
                ));
        ((TextView) activity.findViewById(R.id.textview_tip175_output))
                .setText(getResources().getString(
                        R.string.tip_output,
                        Calculator.evaluateExpression(sub + "0.175", 2)
                ));
        ((TextView) activity.findViewById(R.id.textview_tip20_output))
                .setText(getResources().getString(
                        R.string.tip_output,
                        Calculator.evaluateExpression(sub + "0.20", 2)
                ));
        ((TextView) activity.findViewById(R.id.textview_tip25_output))
                .setText(getResources().getString(
                        R.string.tip_output,
                        Calculator.evaluateExpression(sub + "0.25", 2)
                ));
    }
}
