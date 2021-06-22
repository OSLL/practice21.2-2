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

        val shPrload = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        val backBtn = findViewById<Button>(R.id.BackBtn1)

        backBtn.setOnClickListener {
            finish()
        }

        val sbmtBtnPredNum = findViewById<Button>(R.id.PrNumBtn)
        val predTxt = findViewById<EditText>(R.id.editText)

        val sbmtBtnHerbNum = findViewById<Button>(R.id.HNBtn)
        val herbTxt = findViewById<EditText>(R.id.editText2)

        val sbmtBtnVelEff = findViewById<Button>(R.id.VEBtn)
        val velEffTxt = findViewById<EditText>(R.id.editText3)

        val sbmtBtnPlNum = findViewById<Button>(R.id.PlNumBtn)
        val plNumTxt= findViewById<EditText>(R.id.editText4)

        //  При нажатии на сохранялку "Submit ..."(SbmtBtn...): в SharedPreferences остаются строки с целыми числовыми параметрами со своими Ключами(тэгами)
        //   (например: количество хищников и травоядных, коэффициент связи увеличения СКОРОСТИ и увеличения РАСХОДА ЭНЕРГИИ)
        val shPrSave = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = shPrSave.edit()

        //Обеспечиваем вывод в editText'ах актуальных настроек
        predTxt.setText(shPrload.getInt("PredNum", 5).toString())
        herbTxt.setText(shPrload.getInt("HerbNum", 5).toString())
        velEffTxt.setText(shPrload.getFloat("VelEff", 1f).toString())
        plNumTxt.setText(shPrload.getInt("PlNum", 20).toString())



        //Количество хищников
        sbmtBtnPredNum.setOnClickListener{
                editor.apply{
                    putInt("PredNum", predTxt.text.toString().toInt())
                }.apply()
        }

        //Количество травоядных
        sbmtBtnHerbNum.setOnClickListener{
            editor.apply{
                putInt("HerbNum", herbTxt.text.toString().toInt())
            }.apply()
        }

        //Отношение изменения скорости к соответствующему изменению эффективности
        sbmtBtnVelEff.setOnClickListener{
            editor.apply{
                putFloat("VelEff", velEffTxt.text.toString().toFloat())
            }.apply()
        }
        //Очки за съедение растения
        sbmtBtnPlNum.setOnClickListener{
            editor.apply{
                putInt("PlNum", plNumTxt.text.toString().toInt())
            }.apply()
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Пример доступа к полученным значениям
        //Примечания: Даже после закрытия приложения данные, сохранённые нажатием Submit, остаются в памяти до следующего изменения или перезагрузки телефона.
        //            В ЛЮБОМ МЕСТЕ в программе можно реализовать следующий код и получить значения!
        // Важная строка в начале: [   val shPrload = getSharedPreferences("Settings", Context.MODE_PRIVATE)   ]
        Toast.makeText(this,
            "Predators:" + shPrload.getInt("PredNum", 5).toString() +
            "\nHerbivores:" + shPrload.getInt("HerbNum", 5).toString() +
            "\nVelocity/Efficiency:" + shPrload.getFloat("VelEff", 1f).toString() +
            "\nPlants number:" + shPrload.getInt("PlNum", 20).toString() +
            "\nDefault check:" + shPrload.getString("DeCh", "default text"),
            Toast.LENGTH_SHORT).show() //null - параметр по умолчанию

    }
}