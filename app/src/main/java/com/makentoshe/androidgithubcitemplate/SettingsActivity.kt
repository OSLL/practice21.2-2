package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import kotlin.system.exitProcess

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backBtn = findViewById<Button>(R.id.BackBtn1)

        backBtn.setOnClickListener {
            onDestroy()
        }

        val predTxt = findViewById<TextView>(R.id.editText2)
        val saveBtn = findViewById<ImageButton>(R.id.imageButton)

        //  При нажатии на сохранялку (saveBtn): в SavedInstanceState остаётся строка с новыми числовыми параметрами
        //   (например: количество хищников и травоядных, коэффициент связи увеличения СКОРОСТИ и увеличения РАСХОДА ЭНЕРГИИ). Параметры разделены пробелами
        saveBtn.setOnClickListener{
            override fun onSaveInstanceState(outState: Bundle) {
                outState.putString("NewPredNum", predTxt.toString())
            }
        }

    }
}