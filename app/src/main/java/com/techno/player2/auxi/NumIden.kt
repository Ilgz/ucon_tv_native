package com.techno.player2.auxi

import android.view.View
import android.widget.EditText
import com.techno.player2.R

class NumIden {
    fun numberIden(key: Int, tv: EditText) {
        val keyCode: Int = key - 7
        val tWNmPhoneNumber: String = tv.text.toString()
        val lNGmPhoneNumber = tWNmPhoneNumber.length
        var inputDigit = ""
        when (keyCode) {
            1 -> inputDigit = "1"
            2 -> inputDigit = "2"
            3 -> inputDigit = "3"
            4 -> inputDigit = "4"
            5 -> inputDigit = "5"
            6 -> inputDigit = "6"
            7 -> inputDigit = "7"
            8 -> inputDigit = "8"
            9 -> inputDigit = "9"
            0 -> inputDigit = "0"
        }
        if (lNGmPhoneNumber == 3 || lNGmPhoneNumber == 7) {
            val setT = "$tWNmPhoneNumber $inputDigit"
            tv.setText(setT)
        } else {
            tv.append(inputDigit)
        }
    }


    fun clickRegisterActivity(v: View, tv: EditText) {
        val tWNmPhoneNumber: String = tv.text.toString()
        val lNGmPhoneNumber = tWNmPhoneNumber.length
        if (v.id == R.id.backSpace) {
            if (lNGmPhoneNumber != 0) {
                var dif: Int;
                dif = if (tWNmPhoneNumber.last() == ' '){
                    2;
                }else{
                    1
                }
                tv.setText(tWNmPhoneNumber.substring(0, lNGmPhoneNumber - dif));
            }
        } else {
            var inputDigit = ""
            when (v.id) {
                R.id.one -> inputDigit = "1"
                R.id.two -> inputDigit = "2"
                R.id.three -> inputDigit = "3"
                R.id.four -> inputDigit = "4"
                R.id.five -> inputDigit = "5"
                R.id.six -> inputDigit = "6"
                R.id.seven -> inputDigit = "7"
                R.id.eight -> inputDigit = "8"
                R.id.nine -> inputDigit = "9"
                R.id.zero -> inputDigit = "0"
            }
            if (lNGmPhoneNumber == 3 || lNGmPhoneNumber == 7) {
                val setT = "$tWNmPhoneNumber $inputDigit"
                tv.setText(setT)
            } else {
                tv.append(inputDigit)
            }
        }
    }
}