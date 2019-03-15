package com.machone.jcalc.view.tipcalc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TableLayout;

import com.machone.jcalc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTipInputListener} interface
 * to handle interaction events.
 */
public class TipInputFragment extends Fragment {
    private final String TAG = "TipInputFragment";

    private OnTipInputListener listener;
    private String subtotal = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tip_input, container, false);
        registerClickListeners(view);

        view.post(new Runnable() {
            @Override
            public void run() {
                setButtonHeight(view);
            }
        });

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

    private void setButtonHeight(View view) {
        Log.d(TAG, "setButtonHeight");

        final Activity activity = getActivity();

        View input = activity.findViewById(R.id.textview_subtotal_input);
        View subtotalText = activity.findViewById(R.id.textview_subtotal_string);
        ViewGroup.MarginLayoutParams inputLayout = (ViewGroup.MarginLayoutParams) input.getLayoutParams();
        ViewGroup.MarginLayoutParams subtotalTextLayout = (ViewGroup.MarginLayoutParams) subtotalText.getLayoutParams();

        // Get View heights to subtract from screen height
        final int BUTTON_ROWS = ((TableLayout) view.findViewById(R.id.table_buttons)).getChildCount();
        final int BUTTON_MARGIN = getResources().getDimensionPixelOffset(R.dimen.tipCalcButtonVerticalMargin) * (BUTTON_ROWS + 2);
        final int INPUT_HEIGHT = input.getHeight() + subtotalText.getHeight();
        final int INPUT_MARGIN = inputLayout.topMargin + inputLayout.bottomMargin + subtotalTextLayout.topMargin + subtotalTextLayout.bottomMargin;
        final int BANNER_AD_HEIGHT = getResources().getDimensionPixelOffset(R.dimen.banner_ad_height);

        // ActionBar height
        int actionbarHeight = 0;
        int usableHeight;
        TypedValue typedValue = new TypedValue();
        if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true))
            actionbarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());

        // Soft key height
        // getRealMetrics is only available with API 17+
        int softKeyHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                softKeyHeight = realHeight - usableHeight;
        } else {
            // Get usable screen height
            Rect displayRect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRect);
            usableHeight = displayRect.height();
        }

        // Get status bar height to add
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);

        // Calculate usable space for buttons
        int usable = usableHeight - actionbarHeight - INPUT_HEIGHT - INPUT_MARGIN - BANNER_AD_HEIGHT - BUTTON_MARGIN - softKeyHeight + statusBarHeight;
        int buttonHeight = usable / BUTTON_ROWS;

        // Get and set LayoutParams
        Button zero = view.findViewById(R.id.zero);
        ViewGroup.LayoutParams params = zero.getLayoutParams();
        params.height = buttonHeight;

        // Set LayoutParams for each button
        zero.setLayoutParams(params);
        view.findViewById(R.id.one).setLayoutParams(params);
        view.findViewById(R.id.two).setLayoutParams(params);
        view.findViewById(R.id.three).setLayoutParams(params);
        view.findViewById(R.id.four).setLayoutParams(params);
        view.findViewById(R.id.five).setLayoutParams(params);
        view.findViewById(R.id.six).setLayoutParams(params);
        view.findViewById(R.id.seven).setLayoutParams(params);
        view.findViewById(R.id.eight).setLayoutParams(params);
        view.findViewById(R.id.nine).setLayoutParams(params);
        view.findViewById(R.id.cancel).setLayoutParams(params);
        view.findViewById(R.id.submit).setLayoutParams(params);
    }
}
