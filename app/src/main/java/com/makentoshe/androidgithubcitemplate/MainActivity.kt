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



        val layout = findViewById<FrameLayout>(R.id.frameLayout)
        val arrayDrawer = ArrayDrawer(this)
        arrayDrawer.setPosition(0.1f, 0.1f)
        arrayDrawer.setSize(0.8f)
        layout.addView(arrayDrawer)


        val plantsList : MutableList<EdiblePlant> = mutableListOf()
        val animalsList : MutableList<Animal> = mutableListOf()
        val occupiedCoord : MutableList<Point> = mutableListOf()

        for (i in (0..19)) {
            var x = (0..99).random()
            var y = (0..99).random()
            while (occupiedCoord.any { it == Point(x,y) }) {
                x = (0..99).random()
                y = (0..99).random()
            }
            occupiedCoord.add(Point(x, y))
            animalsList.add(Herbivore(Point(x, y), 3, 1))
        }
        for (i in (0..9)) {
            var x = (0..99).random()
            var y = (0..99).random()
            while (occupiedCoord.any { it == Point(x,y) }) {
                x = (0..99).random()
                y = (0..99).random()
            }
            occupiedCoord.add(Point(x, y))
            animalsList.add(Predator(Point(x, y), 3, 1))
        }
        for (i in (0..29)) {
            var x = (0..99).random()
            var y = (0..99).random()
            while (occupiedCoord.any { it == Point(x,y) }) {
                x = (0..99).random()
                y = (0..99).random()
            }
            occupiedCoord.add(Point(x, y))
            plantsList.add(EdiblePlant(Point(x, y)))
        }

        fun listIntoArray() : Array<Array<Int>>
        {
            val field = Array(100) {Array(100) {0} }
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
                val deathPositions: MutableList<Point> = mutableListOf()
                for (animal in animalsList) {
                    val dPos = animal.move(field)
                    if (dPos.posX != -1 && dPos.posY != -1)
                        deathPositions.add(dPos)
                }

                for (deathPos in deathPositions) {
                    var isDeleted = false
                    for (i in plantsList.indices)
                        if (plantsList[i].position.posX == deathPos.posX && plantsList[i].position.posY == deathPos.posY) {
                            plantsList.removeAt(i)
                            isDeleted = true
                            break
                        }
                    if (!isDeleted) {
                        for (i in animalsList.indices)
                            if (animalsList[i].position.posX == deathPos.posX && animalsList[i].position.posY == deathPos.posY && !animalsList[i].isPredator()) {
                                animalsList.removeAt(i)
                                break
                            }
                    }
                }

                val breedingIndices = mutableListOf<Int>()
                for (i in animalsList.indices)
                    if (animalsList[i].currentAmountOfFood >= animalsList[i].amountFoodForBreeding) {
                        animalsList[i].currentAmountOfFood -= animalsList[i].amountFoodForBreeding.toInt()
                        breedingIndices.add(i)
                    }

                for (i in breedingIndices)
                    if (animalsList[i].isPredator())
                        animalsList.add(Predator(Point(animalsList[i].position.posX, animalsList[i].position.posY), 3, 1))
                    else
                        animalsList.add(Herbivore(Point(animalsList[i].position.posX, animalsList[i].position.posY), 3, 1))

                arrayDrawer.setArrayToDraw(field)
                arrayDrawer.invalidate()
                doFrame()
            }, (1000f).toLong())
        }
        doFrame()


    }
}
