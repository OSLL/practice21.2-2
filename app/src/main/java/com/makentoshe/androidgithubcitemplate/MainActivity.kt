package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Checkj", "onCreateMain")
        val SettingsBtn = findViewById<Button>(R.id.SettingsBtn)
        val StatsBtn = findViewById<Button>(R.id.StatsBtn)

        SettingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            Log.d("Checkj", "beforeIntent")
            startActivity(intent)
            Log.d("Checkj", "afterIntent")
        }
        StatsBtn.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

//vwonvwwninboneboe

    }
}
