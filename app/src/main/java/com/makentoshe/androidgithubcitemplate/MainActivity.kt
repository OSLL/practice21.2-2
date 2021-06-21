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
        layout.addView(arrayDrawer)

        val predatorsList = mutableListOf<PredatorV>()
        val herbivoresList = mutableListOf<HerbivoreV>()
        val plantsList = mutableListOf<PlantV>()

        val deathPositions = mutableListOf<Point>()
        val breedingIndicesPredator = mutableListOf<Int>()
        val breedingIndicesHerbivore = mutableListOf<Int>()

        fun doFrame() {
            Handler(Looper.getMainLooper()).postDelayed({
                for (herbivore in herbivoresList) {
                    val dPos = herbivore.move(herbivoresList, predatorsList, plantsList)
                    if (dPos.x != -1F && dPos.y != -1F)
                        deathPositions.add(dPos)
                }
                for (predator in predatorsList) {
                    val dPos = predator.move(herbivoresList, predatorsList, plantsList)
                    if (dPos.x != -1F && dPos.y != -1F)
                        deathPositions.add(dPos)
                }

                for (deathPos in deathPositions) {
                    var isDeleted = false
                    for (i in plantsList.indices)
                        if (plantsList[i].position.x == deathPos.x &&
                            plantsList[i].position.y == deathPos.y
                        ) {
                            plantsList.removeAt(i)
                            isDeleted = true
                            break
                        }
                    if (!isDeleted) {
                        for (i in herbivoresList.indices)
                            if (herbivoresList[i].position.x == deathPos.x &&
                                herbivoresList[i].position.y == deathPos.y
                            ) {
                                herbivoresList.removeAt(i)
                                break
                            }
                    }
                }

                for (i in predatorsList.indices)
                    if (predatorsList[i].currentPoints >= predatorsList[i].pointsForBreeding) {
                        predatorsList[i].currentPoints -= predatorsList[i].pointsForBreeding
                        breedingIndicesPredator += i
                    }
                for (i in herbivoresList.indices)
                    if (herbivoresList[i].currentPoints >= herbivoresList[i].pointsForBreeding) {
                        herbivoresList[i].currentPoints -= herbivoresList[i].pointsForBreeding
                        breedingIndicesHerbivore += i
                    }

                for (i in breedingIndicesPredator)
                    predatorsList += PredatorV(Point((0..99).random().toFloat(), (0..99).random().toFloat()), 3F, 1F, 1F, 0F, 2F)
                for (i in breedingIndicesHerbivore)
                    herbivoresList += HerbivoreV(Point((0..99).random().toFloat(), (0..99).random().toFloat()), 3F, 1F, 1F, 0F, 2F, true)

                deathPositions.clear()
                breedingIndicesPredator.clear()
                breedingIndicesHerbivore.clear()

                arrayDrawer.setListsToDraw(predatorsList, herbivoresList, plantsList)
                arrayDrawer.invalidate()
                doFrame()
            }, (100f).toLong())
        }
        doFrame()

    }
}
