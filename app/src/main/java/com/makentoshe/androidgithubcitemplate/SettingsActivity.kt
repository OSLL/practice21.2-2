package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shPr = getSharedPreferences("Settings", Context.MODE_PRIVATE)

////////////////////////////////////////////////////////////////////////////////////////////////////
        val editor = shPr.edit()

        //Количество хищников
            //Находим нужный editText
        val predTxt = findViewById<EditText>(R.id.editText)
            //Обеспечиваем вывод в editText'ах актуальных настроек
        predTxt.setText(shPr.getInt("PredNum", 5).toString())
            //  При нажатии на 'Btn': в SharedPreferences остаются строки с числовыми параметрами со своими Ключами(тэгами). Например: количество хищников
        findViewById<Button>(R.id.PrNumBtn).setOnClickListener{
            editor.apply{
                putInt("PredNum", predTxt.text.toString().toInt())
            }.apply()
        }

        //Количество травоядных
        val herbTxt = findViewById<EditText>(R.id.editText2)
        herbTxt.setText(shPr.getInt("HerbNum", 5).toString())
        findViewById<Button>(R.id.HNBtn).setOnClickListener{
            editor.apply{
                putInt("HerbNum", herbTxt.text.toString().toInt())
            }.apply()
        }

        //Отношение изменения скорости к соответствующему изменению эффективности
        val velEffTxt = findViewById<EditText>(R.id.editText3)
        velEffTxt.setText(shPr.getFloat("VelEff", 1f).toString())
        findViewById<Button>(R.id.VEBtn).setOnClickListener{
            editor.apply{
                putFloat("VelEff", velEffTxt.text.toString().toFloat())
            }.apply()
        }

        //Очки за съедение растения
        val plNumTxt= findViewById<EditText>(R.id.editText4)
        plNumTxt.setText(shPr.getInt("PlNum", 20).toString())
        findViewById<Button>(R.id.PlNumBtn).setOnClickListener{
            editor.apply{
                putInt("PlNum", plNumTxt.text.toString().toInt())
            }.apply()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////
    // Выходы с экрана настроек с перезапуском MainActivity с новыми параметрами отменой действия
        findViewById<Button>(R.id.BackBtn1).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.AcceptBtn1).setOnClickListener {
            fieldData.clearAll()
            fieldData.fillLists(shPr.getInt("PredNum", 5)-1, shPr.getInt("HerbNum", 5)-1, shPr.getInt("PlNum", 5)-1)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////
        //Пример доступа к полученным значениям
        //Примечания: Даже после закрытия приложения данные, сохранённые нажатием Submit, остаются в памяти до следующего изменения или перезагрузки телефона.
        //            В ЛЮБОМ МЕСТЕ в программе можно реализовать следующий код и получить значения!
        // Важная строка в onCreate в начале: [   val shPr = getSharedPreferences("Settings", Context.MODE_PRIVATE)   ]
        // Важная строка+ в начале: [   val shPr = context.getSharedPreferences("Settings", MODE_PRIVATE)   ]
        Toast.makeText(this,
            "Predators:" + shPr.getInt("PredNum", 5).toString() +
            "\nHerbivores:" + shPr.getInt("HerbNum", 5).toString() +
            "\nVelocity/Efficiency:" + shPr.getFloat("VelEff", 1f).toString() +
            "\nPlants number:" + shPr.getInt("PlNum", 20).toString() +
            "\nDefault check:" + shPr.getString("DeCh", "default text"),
            Toast.LENGTH_SHORT).show() //null - параметр по умолчанию

    }
}