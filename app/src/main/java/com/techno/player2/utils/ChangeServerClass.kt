//package com.techno.player2.utils
//
//import android.content.SharedPreferences
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.view.KeyEvent
//import android.view.View
//import com.techno.player2.R
//import com.techno.player2.auxi.NumIden
//import android.text.method.PasswordTransformationMethod
//import android.widget.*
//
//
//class ChangeServerClass : AppCompatActivity() {
//    var numIden = NumIden()
//    private lateinit var mPhoneNumber:EditText
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_change_server_class)
//        var sharedPreferences=getSharedPreferences("hls", MODE_PRIVATE)
//        var a=sharedPreferences.getInt("hls",1)
//        findViewById<TextView>(R.id.textView13).text= "Ваш VPS-Сервер: $a"
//        findViewById<Button>(R.id.buttonContininue).setOnClickListener{verify()}
//
//        mPhoneNumber=findViewById(R.id.editTextPhone)
//        mPhoneNumber.transformationMethod = PasswordTransformationMethod()
//        val imageButton = findViewById<ImageButton>(R.id.imageButton3)
//        imageButton.setOnClickListener { onBackPressed() }
//    }
//    private fun verify(){
//           var  otp = mPhoneNumber.getText().toString().trim()
//        var sharedPreferences=getSharedPreferences("hls", MODE_PRIVATE)
//        var sharedPreferencesEditor=sharedPreferences.edit();
//
//            if (otp.length < 7) {
//                Toast.makeText(this, "Заполните поле полностью", Toast.LENGTH_SHORT).show()
//            } else if (otp == "579 190") {
//                Toast.makeText(this,"Вы успешно были переключены на 1 VPS-Сервер",Toast.LENGTH_LONG).show()
//                sharedPreferencesEditor.putInt("hls",1);
//                sharedPreferencesEditor.apply()
//                onBackPressed()
//            } else if (otp == "691 674") {
//                Toast.makeText(this,"Вы успешно были переключены на 2 VPS-Сервер",Toast.LENGTH_LONG).show()
//                sharedPreferencesEditor.putInt("hls",2);
//                sharedPreferencesEditor.apply()
//                onBackPressed()
//
//            }
//            else if (otp == "671 063") {
//                sharedPreferencesEditor.putInt("hls",3);
//                sharedPreferencesEditor.apply()
//                Toast.makeText(this,"Вы успешно были переключены на 3 VPS-Сервер",Toast.LENGTH_LONG).show()
//                onBackPressed()
//
//            }else {
//                Toast.makeText(this,"Неправильный ключ",Toast.LENGTH_SHORT).show()
//
//            }
//    }
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        numIden.numberIden(keyCode, mPhoneNumber)
//        return super.onKeyDown(keyCode, event)
//    }
//
//    fun onClickForNumbers(view: View?) {
//        if (view != null) {
//            numIden.clickRegisterActivity(view, mPhoneNumber)
//        }
//    }
//}