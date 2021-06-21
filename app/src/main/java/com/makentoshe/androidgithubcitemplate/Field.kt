package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.graphics.PointF
import android.os.Handler
import android.os.Looper

class Field(
    context: Context,
    fieldViewSet: FieldView
) {
    public var fieldView = fieldViewSet

    val predatorsList = mutableListOf<PredatorV>()
    val herbivoresList = mutableListOf<HerbivoreV>()
    val plantsList = mutableListOf<PlantV>()


    val deathPositions = mutableListOf<Point>()
    val breedingIndicesPredator = mutableListOf<Int>()
    val breedingIndicesHerbivore = mutableListOf<Int>()



    fun fillLists(predatorsCount : Int, herbivoresCount : Int, plantsCount : Int) {
        for (i in 0 until predatorsCount)
            predatorsList.add(
                PredatorV(
                    Point((0..99).random().toFloat(), (0..99).random().toFloat()),
                    (30..100).random().toFloat() / 10,
                    (10..30).random().toFloat() / 10,
                    (5..20).random().toFloat() / 10,
                    0F,
                    2F
                )
            )
        for (i in 0 until herbivoresCount) {
            val rnd01 = (0..1).random()
            val afraidOfPredator = rnd01 == 1
            herbivoresList.add(
                HerbivoreV(
                    Point((0..99).random().toFloat(), (0..99).random().toFloat()),
                    (30..100).random().toFloat() / 10,
                    (10..30).random().toFloat() / 10,
                    (5..20).random().toFloat() / 10,
                    0F,
                    2F,
                    afraidOfPredator
                )
            )
        }
        for (i in 0 until plantsCount)
            plantsList.add(
                PlantV(
                    Point((0..99).random().toFloat(), (0..99).random().toFloat()),
                    (5..20).random().toFloat() / 10,
                    (5..30).random().toFloat() / 10
                )
            )
    }


    fun doFrame(tickLength : Float) {
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

            fieldView.setListsToDraw(predatorsList, herbivoresList, plantsList)
            fieldView.invalidate()
            doFrame(tickLength)
        }, (tickLength).toLong())
    }



}