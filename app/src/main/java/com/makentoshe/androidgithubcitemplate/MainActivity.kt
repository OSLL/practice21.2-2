package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsBtn = findViewById<Button>(R.id.SettingsBtn)
        val statsBtn = findViewById<Button>(R.id.StatsBtn)

        settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        statsBtn.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }


        val layout = findViewById<FrameLayout>(R.id.frameLayout)
        val arrayDrawer = ArrayDrawer(this)

        arrayDrawer.setPosition(0.1f, 0.1f)
        arrayDrawer.setSize(0.8f)
        val array = Array(100) {Array(100) {1} }
        arrayDrawer.setArrayToDraw(array)
        layout.addView(arrayDrawer)

        val predatorsList = mutableListOf<PredatorV>()
        val herbivoresList = mutableListOf<HerbivoreV>()
        val plantsList = mutableListOf<PlantV>()

        fun doFrame(){
            Handler(Looper.getMainLooper()).postDelayed({
                arrayDrawer.setArrayToDraw(array)
                arrayDrawer.invalidate()
                doFrame()
            }, (100f).toLong())
        }
        doFrame()


    }
}
