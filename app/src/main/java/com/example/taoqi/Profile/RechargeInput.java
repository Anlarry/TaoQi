package com.example.taoqi.Profile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.taoqi.R;
import com.google.android.material.textfield.TextInputEditText;

public class RechargeInput extends ConstraintLayout {
    private TextInputEditText AmountInput;
    private View view;
    public RechargeInput(@NonNull Context context) {
        super(context);
    }

    public RechargeInput(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RechargeInput(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = (View) View.inflate(context, R.layout.recharge, this);
        AmountInput = view.findViewById(R.id.AmountInput);
    }
    public int GetInoutAmount() throws NumberFormatException {
        return Integer.parseInt(AmountInput.getText().toString());
    }
}
