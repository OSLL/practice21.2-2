package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_settings.*
import vadiole.colorpicker.ColorModel
import vadiole.colorpicker.ColorPickerDialog

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        var prAmount = fieldData.startPredatorsAmount
        var plAmount = fieldData.startPlantsAmount
        var heAmount = fieldData.startHerbivoresAmount
        var fSize = fieldData.startRatio

        /*var prAmount = fieldData.predatorsList.size
        var plAmount = fieldData.plantsList.size
        var heAmountount = fieldData.herbivoresList.size
        var fSize = fieldData.ratio*/

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

////////////////////////////////////////////////////////////////////////////////////////////////////
        //Количество хищников
        //Находим нужный editText
        val predTxt = findViewById<EditText>(R.id.PrNumEdit)
        //Обеспечиваем вывод в editText'ах актуальных настроек
        predTxt.setText(prAmount.toString())

        //Количество травоядных
        val herbTxt = findViewById<EditText>(R.id.HeNumEdit)
        herbTxt.setText(heAmount.toString())

        //Отношение изменения скорости к соответствующему изменению эффективности
        val fSizeTxt = findViewById<EditText>(R.id.FSEdit)
        fSizeTxt.setText(fSize.toString())

        //Очки за съедение растения
        val plNumTxt = findViewById<EditText>(R.id.PlNumEdit)
        plNumTxt.setText(plAmount.toString())


//SeekBars' data
        var maxSWT = fieldData.maxStraightWalkTime
        var minSWT = fieldData.minStraightWalkTime

        val seekBarMaxSWT = findViewById<SeekBar>(R.id.SWTMaxSeekBar)
        seekBarMaxSWT.setProgress(((maxSWT-100f)/(20000-100f)*100).toInt())
        seekBarMaxSWT.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                maxSWT = (100 + (progress/100f)*(20000-100)).toInt()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        val seekBarMinSWT = findViewById<SeekBar>(R.id.SWTMinSeekBar)
        seekBarMinSWT.setProgress(((minSWT-100f)/(20000-100f)*100).toInt())
        seekBarMinSWT.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                minSWT = (100 + (progress/100f)*(20000-100)).toInt()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

////////////////////////////////////////////////////////////////////////////////////////////////////

        //TODO: 2. setStraightMovement(min, max) - устанавливает диапазон, из которого берётся случайное время, с которым животное будет двигаться непрерывно (так себе объяснил, проще просто увидеть). min и max в миллисекундах.
        //TODO: 3. setMaxPlantAmount(value) - максимальное количество растений на поле
        //TODO: 4. setSpawnTime(time) - на поле раз в какое-то время появляются в случайном месте растения. Функция позволяет регулировать это время
        //TODO: 5. setPlantsPerSpawnTime(amount) - количество случайно появляющихся растений за время из п.4
        //TODO: 6. (не функция) constantParameterAreSet - если true, то начальные животные (которые добавляются в настройках) будут иметь случайные, но одинаковые между собой характеристики
////////////////////////////////////////////////////////////////////////////////////////////////////
        // Выходы с экрана настроек с перезапуском MainActivity с новыми параметрами
        findViewById<Button>(R.id.BackBtn1).setOnClickListener {
            finish()
        }
        // Выходы с экрана настроек с перезапуском MainActivity с отменой действия
        findViewById<Button>(R.id.AcceptBtn1).setOnClickListener {
            prAmount = predTxt.text.toString().toInt()
            heAmount = herbTxt.text.toString().toInt()
            fSize = fSizeTxt.text.toString().toFloat()
            plAmount = plNumTxt.text.toString().toInt()

            fieldData.clearAll()
            fieldData.setFieldSize(fSize)
            fieldData.fillLists(prAmount, heAmount, plAmount)
            fieldData.startPredatorsAmount  = prAmount
            fieldData.startPlantsAmount = plAmount
            fieldData.startHerbivoresAmount = heAmount
            fieldData.startRatio = fSize
            fieldData.setStraightMovement(minSWT, maxSWT)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////
        // Настройки с только-кнопочными интерфейсами
        val shPr = getSharedPreferences("Settings", MODE_PRIVATE)
        val editor = shPr.edit()

        var defaultPlColor = shPr.getInt("PlColor", Color.GREEN)
        var defaultHeColor = shPr.getInt("HeColor", Color.BLACK)
        var defaultPrColor = shPr.getInt("PrColor", Color.RED)

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
////////////////////////////////////////////////////////////////////////////////////////////////////
    }
}