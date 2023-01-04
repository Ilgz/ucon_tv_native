package com.techno.player2.auxi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.techno.player2.main_windows.Logo_Activity;

public class Xhelper extends AppCompatActivity {
    public  void  DialogBuild(Context context){
        new AlertDialog.Builder(context).setTitle("Недостаточно средств на балансе!").setMessage("Если вы уже оплатили подписку, пожалуйста, перезапустите приложение.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(context, Logo_Activity.class);
                context.startActivity(intent);
                ((Activity)(context)).finishAffinity();

            }
        }).create().show();
    }
}
