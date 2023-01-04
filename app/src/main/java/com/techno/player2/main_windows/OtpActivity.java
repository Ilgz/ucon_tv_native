package com.techno.player2.main_windows;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.instacart.library.truetime.TrueTime;
import com.techno.player2.R;
import com.techno.player2.auxi.NumIden;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class OtpActivity extends AppCompatActivity {
    NumIden numIden = new NumIden();
    String otp;
    private EditText otpText;
    private TextView btnVerify;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);
        setUp();
    }


    private void verify() {
        otp = otpText.getText().toString().trim();
//        otpProgress.setVisibility(View.VISIBLE);
//        btnVerify.setEnabled(false);

        if (otp.length() < 7) {
            Toast.makeText(this, "Заполните поле полностью", Toast.LENGTH_SHORT).show();
            btnVerify.setEnabled(true);
        } else if (otp.equals("228 777")) {
            thisMonth();
        } else if (otp.equals("867 174")) {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("vps", 2);
            editor.putString("phone", "996999999999");
            editor.putString("status", "trial");
            editor.apply();
            startActivity(new Intent(OtpActivity.this, Logo_Activity.class));
            finish();
        } else if (otp.equals("298 839")) {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("vps", 3);
            editor.putString("phone", "996222222222");
            editor.putString("status", "trial");
            editor.apply();
            startActivity(new Intent(OtpActivity.this, Logo_Activity.class));
            finish();
        } else {
            Unpayed();
        }
    }

    private void thisMonth() {
        (new Thread(() -> {
            try {
                TrueTime.build().initialize();
                creatingProfile(0, String.valueOf(TrueTime.now().getMonth()));
            } catch (IOException var2) {
                var2.printStackTrace();
            }

        })).start();
    }

    private void creatingProfile(int balance, String payed_month) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://ucontv.com.kg/abon/?command=get_balance&phone_number=" + phone.replace("+", "").replace(" ", "");
        JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            try {
                if (response.length() == 1) {
                    successfulResult();
                } else {
                    RequestQueue requestQueue_new = Volley.newRequestQueue(this);
                    String creating_Url = "https://ucontv.com.kg/abon/?command=create&phone_number=" + phone.replace("+", "").replace(" ", "") + "&sum=" + balance + "&payed_month=" + payed_month;
                    JsonArrayRequest request_new = new JsonArrayRequest(Request.Method.GET, creating_Url, null, responsing -> {
                        try {
                            successfulResult();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {

                    });
                    requestQueue_new.add(request_new);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> {
        });

        requestQueue.add(requesting);

    }

    private void Unpayed() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, "https://ucontv.com.kg/info/promo/?cate=un_payed", null, response -> {
            try {
                boolean success = false;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String value = jsonObject.getString("value");
                    if (value.equals(otp)) {
                        success = true;
                        String url = "https://ucontv.com.kg/info/promo/?cate=un_payed&used=" + otp.replace(" ", "%20");
                        JsonArrayRequest request_setting_new_values_un_payed = new JsonArrayRequest(Request.Method.GET, url, null, responseForSet -> {
                        }, error -> {
                        });
                        requestQueue.add(request_setting_new_values_un_payed);
                        creatingProfile(0, "99");
                        break;
                    }
                }
                if (!success) {

                    Payed();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {

        });

        requestQueue.add(requesting);


    }

    public void Payed() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, "https://ucontv.com.kg/info/promo/?cate=payed", null, response -> {
            try {
                boolean success = false;
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String value = jsonObject.getString("value");
                    if (value.equals(otp)) {
                        success = true;
                        String url = "https://ucontv.com.kg/info/promo/?cate=payed&used=" + otp.replace(" ", "%20");
                        JsonArrayRequest request_setting_new_values_payed = new JsonArrayRequest(Request.Method.GET, url, null, responseForSet -> {
                        }, error -> {
                        });
                        requestQueue.add(request_setting_new_values_payed);
                        creatingProfile(200, "99");
                        break;
                    }
                }
                if (!success) {

                    Payed();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            Toast.makeText(OtpActivity.this, "Неверный код", Toast.LENGTH_LONG).show();
            btnVerify.setEnabled(true);
        });

        requestQueue.add(requesting);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        numIden.numberIden(keyCode, otpText);
        return super.onKeyDown(keyCode, event);
    }

    private void putModel(String whichModel, String modelName) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://ucontv.com.kg/abon/?command=model&phone_number=";
        url += phone.replace("+", "").replace(" ", "");
        url += "&which=" + whichModel + "&model_name=" + modelName;
        Log.d("Ilgiz", url);
        JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
        }, error -> {
        });

        requestQueue.add(requesting);
    }


    private void successfulResult() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phone", phone.replace("+","").replace(" ",""));
        editor.putString("status", "trial");
        editor.putInt("vps", 1);
        editor.apply();
        startActivity(new Intent(OtpActivity.this, Logo_Activity.class));
        finish();
    }


    @Override
    public void onBackPressed() {
        if (otpText.getText().toString().length() != 0) {
            int x;
            if (otpText.getText().toString().charAt(otpText.getText().toString().length()-1)==' '){
                x=2;
            }else{
              x=  1;
            }
            otpText.setText(otpText.getText().toString().substring(0, otpText.getText().toString().length() - x));
        } else {
            super.onBackPressed();

        }
    }


    public void onClickForNumbers(View view) {
        numIden.clickRegisterActivity(view, otpText);
    }

    private void setUp() {
        Bundle extras = getIntent().getExtras();
        phone = extras.getString("phoneNumber");
        otpText = findViewById(R.id.text_otp);
        btnVerify = findViewById(R.id.buttonContininue);
        btnVerify.setOnClickListener(v -> verify());
        otpText.setTransformationMethod(new MyPasswordTransformationMethod());
    }

    public class MyPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                if(mSource.charAt(index)==' '){
                    return ' ';
                }else{
                    return '*'; // This is the important part
                }
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    ;


}