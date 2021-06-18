package com.makentoshe.androidgithubcitemplate

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backBtn = findViewById<Button>(R.id.BackBtn1)

        backBtn.setOnClickListener {
            finish()
        }

        val sbmtBtnPredNum = findViewById<Button>(R.id.PNBtn)
        val predTxt = findViewById<EditText>(R.id.editText)

        val sbmtBtnHerbNum = findViewById<Button>(R.id.HNBtn)
        val herbTxt = findViewById<EditText>(R.id.editText2)

        val sbmtBtnVelEff = findViewById<Button>(R.id.VEBtn)
        val velEffTxt = findViewById<EditText>(R.id.editText3)

        //  При нажатии на сохранялку "Submit ..."(SbmtBtn...): в SharedPreferences остаются строки с целыми числовыми параметрами со своими Ключами(тэгами)
        //   (например: количество хищников и травоядных, коэффициент связи увеличения СКОРОСТИ и увеличения РАСХОДА ЭНЕРГИИ)
        val shPrSave = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = shPrSave.edit()

        //Количество хищников

        sbmtBtnPredNum.setOnClickListener{
                editor.apply{
                    putString("PredNum", predTxt.text.toString())
                }.apply()
        }

        //Количество травоядных
        sbmtBtnHerbNum.setOnClickListener{
            editor.apply{
                putString("HerbNum", herbTxt.text.toString())
            }.apply()
        }

        //Отношение изменения скорости к соответствующему изменению эффективности
        sbmtBtnVelEff.setOnClickListener{
            editor.apply{
                putString("VelEff", velEffTxt.text.toString())
            }.apply()
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Пример доступа к полученным значениям (каждое в формате строки)
        //Примечания: Даже после закрытия приложения данные, сохранённые нажатием Submit, остаются в памяти до следующего изменения.
        //            В ЛЮБОМ МЕСТЕ в программе можно реализовать следующий код и получить значения!

        val shPrload = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        Toast.makeText(this,
            "Predators:" + shPrload.getString("PredNum", null) +
                "\nHerbivores:" + shPrload.getString("HerbNum", null) +
                "\nVelocity/Efficiency:" + shPrload.getString("VelEff", null),
                Toast.LENGTH_SHORT).show() //null - параметр по умолчанию

    }
}