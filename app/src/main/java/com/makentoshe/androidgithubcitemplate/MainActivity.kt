package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView

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



        var layout = findViewById<FrameLayout>(R.id.frameLayout)
        var arrayDrawer = ArrayDrawer(this)

        arrayDrawer.setPosition(0.1f, 0.1f)
        arrayDrawer.setSize(0.8f)
        val array = Array<Array<Int>>(10) {Array(12) {0} }
        array[4][3] = 1
        arrayDrawer.setArrayToDraw(array)
        layout.addView(arrayDrawer)

        fun doSomething(arr : Array<Array<Int>>) : Array<Array<Int>>
        {
            return arr
        }


        fun doFrame(){
            Handler(Looper.getMainLooper()).postDelayed({
                doSomething(array)
                arrayDrawer.setArrayToDraw(array)
                arrayDrawer.invalidate()
                doFrame()
            }, (100f).toLong())
        }
        doFrame()


    }
}
