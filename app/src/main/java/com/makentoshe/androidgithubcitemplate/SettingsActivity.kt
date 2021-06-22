package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import vadiole.colorpicker.ColorModel
import vadiole.colorpicker.ColorPickerDialog

//TODO: сделать настройки приятно воспринимаемыми

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<Button>(R.id.BackBtn1)

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val sbmtBtnPredNum = findViewById<Button>(R.id.PrNumBtn)
        val predTxt = findViewById<EditText>(R.id.editText)

        val sbmtBtnHerbNum = findViewById<Button>(R.id.HNBtn)
        val herbTxt = findViewById<EditText>(R.id.editText2)

        val sbmtBtnVelEff = findViewById<Button>(R.id.VEBtn)
        val velEffTxt = findViewById<EditText>(R.id.editText3)

        val sbmtBtnPlNum = findViewById<Button>(R.id.PlNumBtn)
        val plNumTxt= findViewById<EditText>(R.id.editText4)

        //  При нажатии на сохранялку "Submit ..."(SbmtBtn...): в SharedPreferences остаются строки с числовыми параметрами со своими Ключами(тэгами)
        //   (например: количество хищников и травоядных, коэффициент связи увеличения СКОРОСТИ и увеличения РАСХОДА ЭНЕРГИИ)
        val shPr = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = shPr.edit()

        //Обеспечиваем вывод в editText'ах актуальных настроек
        predTxt.setText(shPr.getInt("PredNum", 5).toString())
        herbTxt.setText(shPr.getInt("HerbNum", 5).toString())
        velEffTxt.setText(shPr.getFloat("VelEff", 1f).toString())
        plNumTxt.setText(shPr.getInt("PlNum", 20).toString())


////////////////////////////////////////////////////////////////////////////////////////////////////
        //Настройки с полем ввода
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

////////////////////////////////////////////////////////////////////////////////////////////////////
        //Настройки с только-кнопочными интерфейсами

        var DefaultPlColor = shPr.getInt("PlColor", Color.GREEN)
        var DefaultHeColor = shPr.getInt("HeColor", Color.BLACK)
        var DefaultPrColor = shPr.getInt("PrColor", Color.RED)

        findViewById<Button>(R.id.PlColorBtn).setOnClickListener{

            val plColorPicker = ColorPickerDialog.Builder()
            .setInitialColor(DefaultPlColor)
            .setColorModel(ColorModel.HSV)
            .setColorModelSwitchEnabled(false)
            .setButtonOkText(android.R.string.ok)
            .setButtonCancelText(android.R.string.cancel)
            .onColorSelected { color: Int -> DefaultPlColor = color}
            .create()
            plColorPicker.show(supportFragmentManager, "PlColorPicker")


            //plColorPicker.show(supportFragmentManager, "color_picker")
            editor.apply{
                putInt("PlColor", DefaultPlColor)
            }.apply()

            Toast.makeText(this, "OMG, color:"+DefaultPlColor.toString(), Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.HeColorBtn).setOnClickListener{

        }

        findViewById<Button>(R.id.PrColorBtn).setOnClickListener{

        }


////////////////////////////////////////////////////////////////////////////////////////////////////

        //Пример доступа к полученным значениям
        //Примечания: Даже после закрытия приложения данные, сохранённые нажатием Submit, остаются в памяти до следующего изменения или перезагрузки телефона.
        //            В ЛЮБОМ МЕСТЕ в программе можно реализовать следующий код и получить значения!
        // Важная строка в начале: [   val shPrload = getSharedPreferences("Settings", Context.MODE_PRIVATE)   ]
        Toast.makeText(this,
            "Predators:" + shPr.getInt("PredNum", 5).toString() +
            "\nHerbivores:" + shPr.getInt("HerbNum", 5).toString() +
            "\nVelocity/Efficiency:" + shPr.getFloat("VelEff", 1f).toString() +
            "\nPlants number:" + shPr.getInt("PlNum", 20).toString() +
            "\nDefault check:" + shPr.getString("DeCh", "default text"),
            Toast.LENGTH_SHORT).show() //null - параметр по умолчанию

    }
}
