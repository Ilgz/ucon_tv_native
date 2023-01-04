package com.techno.player2.utils;

import android.util.Log;

public class TimerFormatter {
    public String formatterTime(long timeInSeconds) {
        String formattedString = "";
        long seconds = timeInSeconds;
        long minutes = 0;
        long hours = 0;
        while (true) {
            if ((seconds - 60) >= 0) {
                seconds -= 60;
                minutes++;
            } else {
                break;
            }
        }
        while (true) {
            if ((minutes - 60) >= 0) {
                minutes -= 60;
                hours++;
            } else {
                break;
            }
        }
        formattedString += String.valueOf(seconds).length() == 1
                ? "0" + seconds
                : seconds;
        String minutesFormatted = String.valueOf(minutes).length() == 1
                ? "0" + minutes
                : String.valueOf(minutes);
        String hoursFormatted = "";
        if (!String.valueOf(hours).equals("0")) {
            Log.d("Ilgiz","hours"+ hours+"hours");
            hoursFormatted = (String.valueOf(hours).length() == 1
                    ? "0" + hours
                    : hours) +
                    ":";
        }
        formattedString = hoursFormatted + minutesFormatted + ":" + formattedString;
        return formattedString;
    }
}
