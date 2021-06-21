package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        val fieldView = FieldView(this)

        fieldView.setPosition(0.1f, 0.1f)
        fieldView.setSize(0.8f)
        fieldView.setPixelWidth(100)

        val field = Field(fieldView)

        field.fillLists(5, 20, 20)
        layout.addView(fieldView)
        field.doFrame(10f)

    }
}
