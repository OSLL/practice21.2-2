package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import vadiole.colorpicker.ColorModel
import vadiole.colorpicker.ColorPickerDialog

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shPr = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = shPr.edit()

////////////////////////////////////////////////////////////////////////////////////////////////////
        //Количество хищников
        //Находим нужный editText
        val predTxt = findViewById<EditText>(R.id.editText)
        //Обеспечиваем вывод в editText'ах актуальных настроек
        predTxt.setText(shPr.getInt("PredNum", 5).toString())
        //  При нажатии на 'Btn': в SharedPreferences остаются строки с числовыми параметрами со своими Ключами(тэгами). Например: количество хищников
        findViewById<Button>(R.id.PrNumBtn).setOnClickListener {
            editor.apply {
                putInt("PredNum", predTxt.text.toString().toInt())
            }.apply()
        }

        //Количество травоядных
        val herbTxt = findViewById<EditText>(R.id.editText2)
        herbTxt.setText(shPr.getInt("HerbNum", 5).toString())
        findViewById<Button>(R.id.HeNumBtn).setOnClickListener {
            editor.apply {
                putInt("HerbNum", herbTxt.text.toString().toInt())
            }.apply()
        }

        //Отношение изменения скорости к соответствующему изменению эффективности
        val velEffTxt = findViewById<EditText>(R.id.editText3)
        velEffTxt.setText(shPr.getFloat("VelEff", 1f).toString())
        findViewById<Button>(R.id.VEBtn).setOnClickListener {
            editor.apply {
                putFloat("VelEff", velEffTxt.text.toString().toFloat())
            }.apply()
        }

        //Очки за съедение растения
        val plNumTxt = findViewById<EditText>(R.id.editText4)
        plNumTxt.setText(shPr.getInt("PlNum", 20).toString())
        findViewById<Button>(R.id.PlNumBtn).setOnClickListener {
            editor.apply {
                putInt("PlNum", plNumTxt.text.toString().toInt())
            }.apply()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////
        // Выходы с экрана настроек с перезапуском MainActivity с новыми параметрами
        findViewById<Button>(R.id.BackBtn1).setOnClickListener {
            finish()
        }
        // Выходы с экрана настроек с перезапуском MainActivity с отменой действия
        findViewById<Button>(R.id.AcceptBtn1).setOnClickListener {
            fieldData.clearAll()
            fieldData.fillLists(
                shPr.getInt("PredNum", 5) - 1,
                shPr.getInt("HerbNum", 5) - 1,
                shPr.getInt("PlNum", 5) - 1
            )
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////
        // Настройки с только-кнопочными интерфейсами
        var defaultPlColor = shPr.getInt("PlColor", Color.GREEN)
        var defaultHeColor = shPr.getInt("HeColor", Color.BLACK)
        var defaultPrColor = shPr.getInt("PrColor", Color.RED)
        var tmpColor: Int

        //Пред становка цветов View
        findViewById<View>(R.id.PlView).setBackgroundColor(defaultPlColor)
        findViewById<View>(R.id.HeView).setBackgroundColor(defaultHeColor)
        findViewById<View>(R.id.PrView).setBackgroundColor(defaultPrColor)

        findViewById<Button>(R.id.PlColorBtn).setOnClickListener {
            ColorPickerDialog.Builder()
                .setInitialColor(defaultPlColor)
                .setColorModel(ColorModel.HSV)
                .setColorModelSwitchEnabled(false)
                .setButtonOkText(android.R.string.ok)
                .setButtonCancelText(android.R.string.cancel)
                .onColorSelected { color: Int ->
                    defaultPlColor = color
                    editor.apply { putInt("PlColor", defaultPlColor) }.apply()
                    findViewById<View>(R.id.PlView).setBackgroundColor(defaultPlColor)
                }
                .create()
                .show(supportFragmentManager, "PlColorPicker")
        }

        findViewById<Button>(R.id.HeColorBtn).setOnClickListener {
            ColorPickerDialog.Builder()
                .setInitialColor(defaultHeColor)
                .setColorModel(ColorModel.HSV)
                .setColorModelSwitchEnabled(false)
                .setButtonOkText(android.R.string.ok)
                .setButtonCancelText(android.R.string.cancel)
                .onColorSelected { color: Int ->
                    defaultHeColor = color
                    editor.apply { putInt("HeColor", defaultHeColor) }.apply()
                    findViewById<View>(R.id.HeView).setBackgroundColor(defaultHeColor)
                }
                .create()
                .show(supportFragmentManager, "HeColorPicker")
        }

        findViewById<Button>(R.id.PrColorBtn).setOnClickListener {
            ColorPickerDialog.Builder()
                .setInitialColor(defaultPrColor)
                .setColorModel(ColorModel.HSV)
                .setColorModelSwitchEnabled(false)
                .setButtonOkText(android.R.string.ok)
                .setButtonCancelText(android.R.string.cancel)
                .onColorSelected { color: Int ->
                    defaultPrColor = color
                    editor.apply { putInt("PrColor", defaultPrColor) }.apply()
                    findViewById<View>(R.id.PrView).setBackgroundColor(defaultPrColor)
                }
                .create()
                .show(supportFragmentManager, "PrColorPicker")
        }
    }
}