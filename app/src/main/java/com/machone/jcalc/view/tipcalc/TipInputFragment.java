package com.machone.jcalc.view.tipcalc;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.machone.jcalc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTipInputListener} interface
 * to handle interaction events.
 */
public class TipInputFragment extends Fragment {
    private OnTipInputListener listener;
    private String subtotal = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tip_input, container, false);
        registerClickListeners(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTipInputListener) {
            listener = (OnTipInputListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTipInputListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnTipInputListener {
        void onSubtotalChanged(String formattedSubtotal);
        void onSubmitButtonPressed();
    }

    private void registerClickListeners(View view) {
        View.OnClickListener numberListener = new View.OnClickListener() {
            public void onClick(View v) {
                String buttonText = ((Button) v).getText().toString();

                // Ignore leading zeros and only support up to 10 digits
                if ((buttonText.equals("0") && subtotal.length() == 0) ||
                        subtotal.length() == 10)
                    return;

                subtotal += buttonText;
                listener.onSubtotalChanged(getFormattedSubtotal(subtotal));
            }
        };

        view.findViewById(R.id.zero).setOnClickListener(numberListener);
        view.findViewById(R.id.one).setOnClickListener(numberListener);
        view.findViewById(R.id.two).setOnClickListener(numberListener);
        view.findViewById(R.id.three).setOnClickListener(numberListener);
        view.findViewById(R.id.four).setOnClickListener(numberListener);
        view.findViewById(R.id.five).setOnClickListener(numberListener);
        view.findViewById(R.id.six).setOnClickListener(numberListener);
        view.findViewById(R.id.seven).setOnClickListener(numberListener);
        view.findViewById(R.id.eight).setOnClickListener(numberListener);
        view.findViewById(R.id.nine).setOnClickListener(numberListener);

        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Reset subtotal in case the user hits reset to come back here.
                subtotal = "";
                listener.onSubmitButtonPressed();
            }
        });

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                subtotal = "";
                listener.onSubtotalChanged(getFormattedSubtotal(subtotal));
            }
        });
    }

    private String getFormattedSubtotal(String number) {
        String out;
        if (number.length() == 0)
            out = "000";
        else if (number.length() == 1)
            out = "00" + number;
        else if (number.length() == 2)
            out = "0" + number;
        else
            out = number;

        String dollars = out.substring(0, out.length() - 2);
        String cents = out.substring(out.length() - 2);

        return dollars + "." + cents;
    }
}
