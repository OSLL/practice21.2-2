package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.ToggleButton

val fieldData = FieldData()

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
        speedText.text = "1.0x"

        settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        statsBtn.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        val layout = findViewById<FrameLayout>(R.id.frameLayout)
        val fieldView = FieldView(this)

        fieldView.setPosition(0.1f, 0.1f)
        fieldView.setSize(0.8f)

        val field = Field(fieldView)

        stopButton.setOnClickListener{
            Log.d("aaa", "${stopButton.isActivated}")
            if(stopButton.text == "Start")
                field.stopProcess()
            else
                field.startProcess()
        }

        speedPlusBtn.setOnClickListener{
            field.speedIncrease()
            speedText.text = "${field.speed}x"
        }
        speedMinusBtn.setOnClickListener{
            field.speedDecrease()
            speedText.text = "${field.speed}x"
        }

        val shPrLoad = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val prC = shPrLoad.getInt("PredNum", 5)
        val hC = shPrLoad.getInt("HerbNum", 20)
        val plC = shPrLoad.getInt("PlNum", 20)

        fieldData.fillLists(prC,hC,plC)
        layout.addView(fieldView)
        field.setTick(10f)
        field.startProcess()
    }
}
