package com.makentoshe.androidgithubcitemplate

import Animal
import EdiblePlant
import Herbivore
import Point
import Predator
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



        var layout = findViewById<FrameLayout>(R.id.frameLayout)
        var arrayDrawer = ArrayDrawer(this)

        arrayDrawer.setPosition(0.1f, 0.1f)
        arrayDrawer.setSize(0.8f)
        val array = Array<Array<Int>>(10) {Array(12) {0} }
        array[4][3] = 1
        arrayDrawer.setArrayToDraw(array)
        layout.addView(arrayDrawer)


        var plantsList : MutableList<EdiblePlant> = mutableListOf()
        var animalsList : MutableList<Animal> = mutableListOf()
        for (i in (0..9)) {
            var x = (0..99).random()
            var y = (0..99).random()
            animalsList.add(Herbivore(Point(x, y), 3, 1))
        }
        for (i in (0..4)) {
            var x = (0..99).random()
            var y = (0..99).random()
            animalsList.add(Predator(Point(x, y), 3, 1))
        }
        for (i in (0..19)) {
            var x = (0..99).random()
            var y = (0..99).random()
            plantsList.add(EdiblePlant(Point(x, y)))
        }

        fun listIntoArray() : Array<Array<Int>>
        {
            val field = Array<Array<Int>>(100) {Array(100) {0} }
            for (plant in plantsList){
                field[plant.position.posY][plant.position.posX] = 1
            }
            for (animal in animalsList){
                if (animal.isPredator())
                    field[animal.position.posY][animal.position.posX] = 3
                else
                    field[animal.position.posY][animal.position.posX] = 2
            }

            return field
        }



        fun doFrame(){
            Handler(Looper.getMainLooper()).postDelayed({
                val field = listIntoArray()
                for(animal in animalsList){
                    animal.move(field)
                }
                arrayDrawer.setArrayToDraw(field)
                arrayDrawer.invalidate()
                doFrame()
            }, (1000f).toLong())
        }
        doFrame()


    }
}
