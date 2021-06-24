package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity


val fieldData = FieldData()
var isFirst = true

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsBtn = findViewById<Button>(R.id.SettingsBtn)
        val statsBtn = findViewById<Button>(R.id.StatsBtn)
        val speedPlusBtn = findViewById<Button>(R.id.SpeedBtn2)
        val speedMinusBtn = findViewById<Button>(R.id.SpeedBtn1)
        val stopButton = findViewById<ToggleButton>(R.id.StartBtn)
        val speedText = findViewById<TextView>(R.id.speedText)
        val zoomBar = findViewById<SeekBar>(R.id.seekBar)
        speedText.text = "1.0x"

        settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        statsBtn.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        if (isFirst) {
            val display = windowManager.defaultDisplay
            val metricsB = DisplayMetrics()
            display.getMetrics(metricsB)
            fieldData.initBaseSize(
                metricsB.widthPixels / 10 - 1,
                metricsB.heightPixels * 2 / 3 / 10
            )
        }

        val layout = findViewById<FrameLayout>(R.id.frameLayout)
        val fieldView = FieldView(this)

        fieldView.setPosition(0.01f, 0.01f)
        fieldView.setSize(0.98f)

        val field = Field(fieldView)

        stopButton.setOnClickListener {
            Log.d("aaa", "${stopButton.isActivated}")
            if (stopButton.text == "Start")
                field.stopProcess()
            else
                field.startProcess()
        }

        speedPlusBtn.setOnClickListener {
            field.speedIncrease()
            speedText.text = "${field.speed}x"
        }
        speedMinusBtn.setOnClickListener {
            field.speedDecrease()
            speedText.text = "${field.speed}x"
        }
        zoomBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                fieldView.setZoom(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        if (isFirst) {
            fieldData.fillLists(5, 5, 5)
            isFirst = false
        }
        layout.addView(fieldView)
        field.setTick(1f)
        field.startProcess()
    }
}
