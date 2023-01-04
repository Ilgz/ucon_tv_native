package com.techno.player2.main_windows;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.techno.player2.R;

import org.w3c.dom.Text;

public class ProfilePage extends AppCompatActivity {
    boolean isVlc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
       isVlc= sharedPreferences.getBoolean("isVlc",false);
        TextView playerButton=findViewById(R.id.change_player);
        playerButton.setText("Текущий плеер:"+(isVlc?"VLC":"NATIVE"));
        playerButton.setOnClickListener(v ->{
            new AlertDialog.Builder(this).setTitle("Подтверждение").setMessage("Вы действительно хотите сменить ваш плеер?").setPositiveButton("Да", (dialog1, which) -> changePlayer()).setNegativeButton("Нет", (dialog12, which) -> dialog12.dismiss()).show();
        });
        TextView profileTvPhone=findViewById(R.id.profile_tv_phone);
        TextView profileTvBalance=findViewById(R.id.profile_tv_balance);
        profileTvPhone.setText(sharedPreferences.getString("phone","0709872197").replace("996","0"));
        profileTvBalance.setText("Ваш баланс "+sharedPreferences.getString("balance","0")+" сом");
        ImageView imageButton = findViewById(R.id.back_button);
        imageButton.setOnClickListener(v -> onBackPressed());
        imageButton.requestFocus();
        TextView exit=findViewById(R.id.exit_button);
        exit.setOnClickListener(v -> new AlertDialog.Builder(this).setTitle("Подтверждение").setMessage("Вы действительно хотите выйти из учетной записи?").setPositiveButton("Да", (dialog1, which) -> exit()).setNegativeButton("Нет", (dialog12, which) -> dialog12.dismiss()).show());
    }
    private void changePlayer(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.k_shared), MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(isVlc){
            editor.putBoolean("isVlc",false);
        }else{
            editor.putBoolean("isVlc",true);
        }
        editor.apply();
        onBackPressed();
    }
    private void exit() {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.k_shared), MODE_PRIVATE);
        preferences.edit().clear().apply();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finishAffinity();
    }
}
