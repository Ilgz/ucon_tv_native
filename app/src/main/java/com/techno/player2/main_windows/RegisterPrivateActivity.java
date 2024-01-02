package com.techno.player2.main_windows;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.techno.player2.R;
import com.techno.player2.auxi.NumIden;
//import com.techno.player2.auxi.NumIden;

public class RegisterPrivateActivity extends AppCompatActivity {
    NumIden numIden = new NumIden();
    private EditText mPhoneNumber;
    private TextView mGenerateBtn;
    private String phone_number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_private_layout);
        mPhoneNumber = findViewById(R.id.text_phone);
        mGenerateBtn = findViewById(R.id.buttonContininue);
//        ProgressBar progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.INVISIBLE);
        clicked();
    }

    void clicked() {
        mGenerateBtn.setOnClickListener(v -> {
            phone_number = mPhoneNumber.getText().toString().trim();
            String complete = phone_number;
            if (phone_number.length() < 11) {
                Toast.makeText(this, "Заполните поле полностью", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(RegisterPrivateActivity.this, OtpActivity.class);
                intent.putExtra("phoneNumber", complete);
                startActivity(intent);
            }

        });
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        numIden.numberIden(keyCode, mPhoneNumber);
        return super.onKeyDown(keyCode, event);
    }

    public void onClickForNumbers(View view) {
        numIden.clickRegisterActivity(view, mPhoneNumber);

    }


    @Override
    public void onBackPressed() {
        if (mPhoneNumber.getText().toString().length() != 0) {
            mPhoneNumber.setText(mPhoneNumber.getText().toString().substring(0, mPhoneNumber.getText().toString().length() - 1));
        } else {
            super.onBackPressed();

        }
    }

}