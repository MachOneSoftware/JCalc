package com.machone.jcalc.view.tipcalc;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.machone.jcalc.R;
import com.machone.jcalc.helper.Calculator;
import com.machone.jcalc.helper.Operators;

public class TipOutputFragment extends Fragment {
    private final String TAG = "TipOutputFragment";

    private String subtotal;
    private EditText customPercentage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tip_output, container, false);
        registerClickListeners(view);

        // TODO future maybe
//        view.post(new Runnable() {
//            @Override
//            public void run() {
//                setOutputHeight(view);
//            }
//        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public ViewTooltip getCustomTipTooltip() {
        return ViewTooltip
                .on(customPercentage)
                .autoHide(true, 10000)
                .clickToHide(true)
                .position(ViewTooltip.Position.LEFT)
                .text(getString(R.string.tooltip_new_feature_tipcalc_custom_percentage))
                .color(Color.BLACK)
                .textColor(Color.WHITE);
    }

    private void registerClickListeners(final View view) {
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

    // TODO future maybe
//    private void setOutputHeight(View view) {
//        Log.d(TAG, "setOutputHeight");
//
//        final Activity activity = getActivity();
//
//        View suggestedTip = view.findViewById(R.id.textview_suggestedtip_string);
//        View view15 = view.findViewById(R.id.textview_tip15_output);
//        View view175 = view.findViewById(R.id.textview_tip175_output);
//        View view20 = view.findViewById(R.id.textview_tip20_output);
//        View viewCust = view.findViewById(R.id.textview_custom_output);
//        View input = activity.findViewById(R.id.textview_subtotal_input);
//        View subtotalText = activity.findViewById(R.id.textview_subtotal_string);
//        ViewGroup.MarginLayoutParams inputLayout = (ViewGroup.MarginLayoutParams) input.getLayoutParams();
//        ViewGroup.MarginLayoutParams subtotalTextLayout = (ViewGroup.MarginLayoutParams) subtotalText.getLayoutParams();
//        ViewGroup.MarginLayoutParams suggestedTipLayout = (ViewGroup.MarginLayoutParams) suggestedTip.getLayoutParams();
//        ViewGroup.MarginLayoutParams view15Layout = (ViewGroup.MarginLayoutParams) view15.getLayoutParams();
//        ViewGroup.MarginLayoutParams viewCustLayout = (ViewGroup.MarginLayoutParams) viewCust.getLayoutParams();
//
//        // Get View heights to subtract from screen height
//        final int INPUT_HEIGHT = input.getHeight() + subtotalText.getHeight();
//        final int INPUT_MARGIN = inputLayout.topMargin + inputLayout.bottomMargin + subtotalTextLayout.topMargin + subtotalTextLayout.bottomMargin;
//        final int BANNER_AD_HEIGHT = getResources().getDimensionPixelOffset(R.dimen.banner_ad_height);
//        final int TEXT_HEIGHT = view15.getHeight() + view175.getHeight() + view20.getHeight() + viewCust.getHeight();
//        final int TEXT_MARGIN = suggestedTipLayout.bottomMargin + suggestedTipLayout.topMargin + view15Layout.topMargin + viewCustLayout.bottomMargin;
//
//        // ActionBar height
//        int actionbarHeight = 0;
//        int usableHeight;
//        TypedValue typedValue = new TypedValue();
//        if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true))
//            actionbarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
//
//        // Soft key height
//        // getRealMetrics is only available with API 17+
//        int softKeyHeight = 0;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            DisplayMetrics metrics = new DisplayMetrics();
//            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            usableHeight = metrics.heightPixels;
//            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
//            int realHeight = metrics.heightPixels;
//            if (realHeight > usableHeight)
//                softKeyHeight = realHeight - usableHeight;
//        } else {
//            // Get usable screen height
//            Rect displayRect = new Rect();
//            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRect);
//            usableHeight = displayRect.height();
//        }
//
//        // Get status bar height to add
//        int statusBarHeight = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0)
//            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//
//        // Calculate usable space for margins
//        usableHeight = usableHeight - actionbarHeight - INPUT_HEIGHT - INPUT_MARGIN - TEXT_HEIGHT - TEXT_MARGIN - BANNER_AD_HEIGHT - softKeyHeight + statusBarHeight;
//
//        int margin = usableHeight / 6;
//
//        // Get and set LayoutParams
//        ViewGroup.MarginLayoutParams middleOutputParams = (ViewGroup.MarginLayoutParams) view175.getLayoutParams();
//
//        view15Layout.bottomMargin = margin;
//        middleOutputParams.topMargin = margin;
//        middleOutputParams.bottomMargin = margin;
//        viewCustLayout.topMargin = margin;
//
//        view15.setLayoutParams(view15Layout);
//        view175.setLayoutParams(middleOutputParams);
//        view.findViewById(R.id.textview_percent20_string).setLayoutParams(middleOutputParams);
//        customPercentage.setLayoutParams(viewCustLayout);
//    }
}
