package com.techno.player2.main_windows;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.instacart.library.truetime.TrueTime;
import com.techno.player2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Logo_Activity extends AppCompatActivity {
    private String phone;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_);
        sharedPreferences = getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", "null");
        phone = phone.replace(" ", "");
        preFireLoading();
    }

    private void preFireLoading() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        requestQueue = Volley.newRequestQueue(this);
        init();
        int hls = sharedPreferences.getInt("vps", 1);
        String url = "https://ucontv.com.kg/info/update_links/";
        if (hls == 2) {
            url = "https://ucontv.com.kg/info/update_links_2/";
        } else if (hls == 3) {
            url = "https://ucontv.com.kg/info/update_links_3/";
        }
        JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    String Name = jsonObject.getString("Name");
                    String Link = jsonObject.getString("Link");
                    switch (Name) {
                        case "TCall": {
                            getJson(Link, "All");
                            break;
                        }
                        case "TCchild": {
                            getJson(Link, "Child");

                            break;
                        }
                        case "TCfilm": {
                            getJson(Link, "Film");
                            break;
                        }
                        case "TCcog": {
                            getJson(Link, "Cognitive");

                            break;
                        }
                        case "TCnews": {
                            editor.putString("News", Link);
                            getJson(Link, "News");

                            break;
                        }
                        case "TCsport": {
                            getJson(Link, "Sport");
                            break;
                        }
                        case "TCmusic": {
                            getJson(Link, "Music");

                            break;
                        }
                        case "TCinter": {
                            getJson(Link, "International");
                            break;
                        }
                        case "FilmUpdate": {
                            editor.putString("FilmUpdate", Link);
                            break;
                        }
                        case "SliderUpdate": {
                            editor.putString("Slider", Link);

                            break;
                        }


                    }
                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, error -> {
        });
        requestQueue.add(requesting);
        if (sharedPreferences.getString("status", "bb").contains("trial")) {
            Intent intent = new Intent(Logo_Activity.this, MainActivity.class);
            intent.putExtra("Portal", "agree");
            startActivity(intent);
        } else {
            startActivity(new Intent(Logo_Activity.this, RegisterActivity.class));
        }
        finish();
    }

    private void getJson(String url, String sharedName) {
        String newUrl = (url.contains("www")) ? url : url.replace("https://", "https://www.");
        JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, newUrl, null, response -> {
            SharedPreferences.Editor sharedEditor = sharedPreferences.edit();
            sharedEditor.putString(sharedName, response.toString());
            sharedEditor.apply();
        }, error -> {
            System.out.println("ILGIZ" + "Errror");
        });
        requestQueue.add(requesting);
    }


    public void init() {
        (new Thread(() -> {
            try {
                TrueTime.build().initialize();
                int date = TrueTime.now().getDate();
                System.out.println(TrueTime.now());
                int month = TrueTime.now().getMonth();
                String MonthString = String.valueOf(month);
                Calendar mycal = new GregorianCalendar(TrueTime.now().getYear() + 1900, month, 1);
                int cal = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
                int LeftDays = cal - date;
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonArrayRequest requesting = new JsonArrayRequest(Request.Method.GET, "https://ucontv.com.kg/abon/?command=get_balance&phone_number=" + phone.replace("+", ""), null, response -> {
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        String my_balance = jsonObject.getString("balance");
                        String payed_month = jsonObject.getString("payed_month");
                        double int_my_balance = Double.parseDouble(my_balance);
                        if (payed_month != null && payed_month.equals(MonthString)) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("access", true);
                            editor.putString("payed_month", MonthString);
                            editor.apply();
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.putString("balance", my_balance);
                            editor1.apply();
                        } else {
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.putString("balance", my_balance);
                            editor1.apply();
                            double Remnant;
                            if (date == 1) {
                                Remnant = 200;

                            } else {
                                Remnant = LeftDays * 6.5;
                            }
                            int_my_balance = int_my_balance - Remnant;
                            if (int_my_balance >= 0) {
                                String url = "https://ucontv.com.kg/abon/?command=set_balance&phone_number=" + phone.replace("+", "") + "&" + "sum=" + int_my_balance + "&" + "payed_month=" + MonthString;
                                JsonArrayRequest request_setting_new_values = new JsonArrayRequest(Request.Method.GET, url, null, responseForSet -> {
                                }, error -> {
                                });
                                requestQueue.add(request_setting_new_values);
                                editor1.putString("balance", String.valueOf(int_my_balance));
                                editor1.apply();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("access", true);
                                editor.putString("payed_month", MonthString);
                                editor.apply();
                            } else {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("access", false);
                                editor.apply();
                            }


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {

                });

                requestQueue.add(requesting);


            } catch (IOException var2) {
                var2.printStackTrace();
            }

        })).start();
    }


}