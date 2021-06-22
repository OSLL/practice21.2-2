package com.makentoshe.androidgithubcitemplate

class FieldData {

    val predatorsList = mutableListOf<PredatorV>()
    val herbivoresList = mutableListOf<HerbivoreV>()
    val plantsList = mutableListOf<PlantV>()

    private val deathPlantsIndices = mutableListOf<Int>()
    private val deathHerbivoresIndices = mutableListOf<Int>()

    private val breedingIndicesPredator = mutableListOf<Int>()
    private val breedingIndicesHerbivore = mutableListOf<Int>()

    private val deathFromHungerIndicesPredators = mutableListOf<Int>()

    var timeStats = TimeStatistic()

    fun fillLists(predatorsCount: Int, herbivoresCount: Int, plantsCount: Int) {
        for (i in 0 until predatorsCount)
            predatorsList.add(
                PredatorV(
                    Point((0..99).random().toFloat(), (0..99).random().toFloat()),
                    (100..200).random().toFloat() / 10,
                    (50..60).random().toFloat() / 10,
                    (20..30).random().toFloat() / 20,
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
                    (100..200).random().toFloat() / 10,
                    (50..60).random().toFloat() / 10,
                    (20..30).random().toFloat() / 20,
                    0F,
                    2F
                )
            )
        }
        for (i in 0 until plantsCount)
            plantsList.add(
                PlantV(
                    Point((0..99).random().toFloat(), (0..99).random().toFloat()),
                    (20..30).random().toFloat() / 20,
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
            if (predatorsList[i].currentPoints <= -5)
                deathFromHungerIndicesPredators += i

        for (i in herbivoresList.indices)
            if (herbivoresList[i].currentPoints <= -5)
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
                Point((0..99).random().toFloat(), (0..99).random().toFloat()),
                (100..200).random().toFloat() / 10,
                (100..160).random().toFloat() / 10,
                (20..30).random().toFloat() / 20,
                0F,
                2F
            )

        for (i in breedingIndicesHerbivore)
            herbivoresList += HerbivoreV(
                Point((0..99).random().toFloat(), (0..99).random().toFloat()),
                (100..200).random().toFloat() / 10,
                (100..160).random().toFloat() / 10,
                (20..30).random().toFloat() / 20,
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