package com.example.credapptest;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.credapptest.customview.CardNumberWatcher;
import com.example.credapptest.customview.EditTextWithSuffix;
import com.example.credapptest.interfaces.OnTextChangeListener;

public class MainActivity extends AppCompatActivity implements OnTextChangeListener {

    private EditTextWithSuffix etCardNumber;
    private TextView tvError;
    private ImageView ivCardImage;
    private boolean lockEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineLayout();
    }

    public void defineLayout() {
        etCardNumber = findViewById(R.id.etCardNumber);
        tvError = findViewById(R.id.tvError);
        ivCardImage = findViewById(R.id.ivCardImage);

        etCardNumber.addTextChangedListener(new CardNumberWatcher(this));
    }

    @Override
    public void onTextChange(String cardNumber, String s) {
        etCardNumber.setSuffix(s);
    }

    @Override
    public void onValidationError() {
        tvError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideValidation() {
        tvError.setVisibility(View.GONE);
    }

    @Override
    public void setResource(int resourceId) {
        ivCardImage.setImageResource(resourceId);
    }
}
