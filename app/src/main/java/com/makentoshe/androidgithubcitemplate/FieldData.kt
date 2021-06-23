package com.makentoshe.androidgithubcitemplate

class FieldData {
    var fieldSizeW = 100
    var fieldSizeH = 130

    val predatorsList = mutableListOf<PredatorV>()
    val herbivoresList = mutableListOf<HerbivoreV>()
    val plantsList = mutableListOf<PlantV>()

    private val deathPlantsIndices = mutableListOf<Int>()
    private val deathHerbivoresIndices = mutableListOf<Int>()

    private val breedingIndicesPredator = mutableListOf<Int>()
    private val breedingIndicesHerbivore = mutableListOf<Int>()

    private val deathFromHungerIndicesPredators = mutableListOf<Int>()

    var timeStats = TimeStatistic()

    fun setFieldSize(width: Int, height: Int) {
        fieldSizeW = width
        fieldSizeH = height
    }

    fun fillLists(predatorsCount: Int, herbivoresCount: Int, plantsCount: Int) {
        for (i in 0 until predatorsCount)
            predatorsList.add(
                PredatorV(
                    Point((2..fieldSizeW - 3).random().toFloat(), (2..fieldSizeH - 3).random().toFloat()),
                    (100..200).random().toFloat() / 10,
                    (50..70).random().toFloat() / 10,
                    (40..60).random().toFloat() / 10,
                    (20..35).random().toFloat() / 20,
                    0F,
                    2F
                )
            )
        for (i in 0 until herbivoresCount) {
            herbivoresList.add(
                HerbivoreV(
                    Point((2..fieldSizeW - 3).random().toFloat(), (2..fieldSizeH - 3).random().toFloat()),
                    (100..200).random().toFloat() / 10,
                    (50..70).random().toFloat() / 10,
                    (40..60).random().toFloat() / 10,
                    (20..35).random().toFloat() / 20,
                    0F,
                    2F
                )
            )
        }
        for (i in 0 until plantsCount)
            plantsList.add(
                PlantV(
                    Point((2..fieldSizeW - 2).random().toFloat(), (2..fieldSizeH - 2).random().toFloat()),
                    (10..30).random().toFloat() / 20,
                    (5..30).random().toFloat() / 10
                )
            )
    }

    fun update(deltaTime: Long) {
        for (herbivore in herbivoresList) {
            val index = herbivore.move(herbivoresList, predatorsList, plantsList, deltaTime / 1000f)
            if (index != -1)
                deathPlantsIndices.add(index)
        }
        for (predator in predatorsList) {
            val index = predator.move(herbivoresList, predatorsList, plantsList, deltaTime / 1000f)
            if (index != -1)
                deathHerbivoresIndices.add(index)
        }

        for (i in predatorsList.indices)
            if (predatorsList[i].currentPoints <= -5 * predatorsList[i].size)
                deathFromHungerIndicesPredators += i

        for (i in herbivoresList.indices)
            if (herbivoresList[i].currentPoints <= -5 * herbivoresList[i].size)
                deathHerbivoresIndices += i

        deathPlantsIndices.sortDescending()
        deathHerbivoresIndices.sortDescending()
        deathFromHungerIndicesPredators.sortDescending()

        for (i in deathPlantsIndices)
            if (plantsList.size > i)
                plantsList.removeAt(i)
        for (i in deathHerbivoresIndices)
            if (herbivoresList.size > i)
                herbivoresList.removeAt(i)
        for (i in deathFromHungerIndicesPredators)
            if (predatorsList.size > i)
                predatorsList.removeAt(i)

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
            predatorsList += PredatorV(
                Point((2..fieldSizeW - 3).random().toFloat(), (2..fieldSizeH - 3).random().toFloat()),
                (100..200).random().toFloat() / 10,
                (50..70).random().toFloat() / 10,
                (40..60).random().toFloat() / 10,
                (20..35).random().toFloat() / 20,
                0F,
                2F
            )

        for (i in breedingIndicesHerbivore)
            herbivoresList += HerbivoreV(
                Point((2..fieldSizeW - 3).random().toFloat(), (2..fieldSizeH - 3).random().toFloat()),
                (100..200).random().toFloat() / 10,
                (50..70).random().toFloat() / 10,
                (40..60).random().toFloat() / 10,
                (20..35).random().toFloat() / 20,
                0F,
                2F
            )

        deathHerbivoresIndices.clear()
        deathPlantsIndices.clear()
        breedingIndicesPredator.clear()
        breedingIndicesHerbivore.clear()

        timeStats.addTo(timeStats.herbivoresAmount, herbivoresList.size)
        timeStats.addTo(timeStats.predatorsAmount, predatorsList.size)
        timeStats.addTo(timeStats.plantsAmount, plantsList.size)

    }
}