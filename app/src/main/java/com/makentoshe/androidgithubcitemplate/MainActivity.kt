package com.makentoshe.androidgithubcitemplate

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

        val plantsList: MutableList<EdiblePlant> = mutableListOf()
        val predatorsList: MutableList<Predator> = mutableListOf()
        val herbivoreList: MutableList<Herbivore> = mutableListOf()

        val occupiedCoord: MutableList<Point> = mutableListOf()

        val field = Array(100) { Array(100) { 0 } }

        val deathPositions: MutableList<Point> = mutableListOf()
        val breedingIndicesPredator = mutableListOf<Int>()
        val breedingIndicesHerbivore = mutableListOf<Int>()

        // Adding herbivores
        for (i in (0..25)) {
            var x = (0..99).random()
            var y = (0..99).random()
            while (occupiedCoord.any { it.posX == x && it.posY == y }) {
                x = (0..99).random()
                y = (0..99).random()
            }
            occupiedCoord.add(Point(x, y))
            herbivoreList.add(Herbivore(Point(x, y), 3, 1))
        }
        // Adding predators
        for (i in (0..10)) {
            var x = (0..99).random()
            var y = (0..99).random()
            while (occupiedCoord.any { it.posX == x && it.posY == y }) {
                x = (0..99).random()
                y = (0..99).random()
            }
            occupiedCoord.add(Point(x, y))
            predatorsList.add(Predator(Point(x, y), 3, 1))
        }
        // adding plants
        for (i in (0..30)) {
            var x = (0..99).random()
            var y = (0..99).random()
            while (occupiedCoord.any { it.posX == x && it.posY == y }) {
                x = (0..99).random()
                y = (0..99).random()
            }
            occupiedCoord.add(Point(x, y))
            plantsList.add(EdiblePlant(Point(x, y)))
        }

        fun listIntoArray() {
            for (i in 0..99)
                for (j in 0..99)
                    field[i][j] = 0
            for (plant in plantsList) {
                field[plant.position.posY][plant.position.posX] = 1
            }
            for (predator in predatorsList)
                field[predator.position.posY][predator.position.posX] = 3
            for (herbivore in herbivoreList)
                field[herbivore.position.posY][herbivore.position.posX] = 2
        }

        fun doFrame() {
            Handler(Looper.getMainLooper()).postDelayed({
                for (herbivore in herbivoreList) {
                    val dPos = herbivore.move(herbivoreList, predatorsList, plantsList)
                    if (dPos.posX != -1 && dPos.posY != -1)
                        deathPositions.add(dPos)
                }
                for (predator in predatorsList) {
                    val dPos = predator.move(herbivoreList, predatorsList, plantsList)
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
                        for (i in herbivoreList.indices)
                            if (herbivoreList[i].position.posX == deathPos.posX && herbivoreList[i].position.posY == deathPos.posY) {
                                herbivoreList.removeAt(i)
                                break
                            }
                    }
                }

                for (i in predatorsList.indices)
                    if (predatorsList[i].currentAmountOfFood >= predatorsList[i].amountFoodForBreeding) {
                        predatorsList[i].currentAmountOfFood -= predatorsList[i].amountFoodForBreeding
                        breedingIndicesPredator += i
                    }
                for (i in herbivoreList.indices)
                    if (herbivoreList[i].currentAmountOfFood >= herbivoreList[i].amountFoodForBreeding){
                        herbivoreList[i].currentAmountOfFood -= herbivoreList[i].amountFoodForBreeding
                        breedingIndicesHerbivore += i
                    }

                for (i in breedingIndicesPredator)
                    predatorsList += Predator(Point((0..99).random(), (0..99).random()), 3, 1)
                for (i in breedingIndicesHerbivore)
                    herbivoreList += Herbivore(Point((0..99).random(), (0..99).random()), 3, 1)

                deathPositions.clear()
                breedingIndicesPredator.clear()
                breedingIndicesHerbivore.clear()

                listIntoArray()

                arrayDrawer.setArrayToDraw(field)
                arrayDrawer.invalidate()
                doFrame()
            }, (30f).toLong())
        }
        doFrame()

    }
}
