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

    var time = System.currentTimeMillis()

    var movesPerTick = 40    // Измеряется в количестве тиков
    var rotatesPerTick = 50  // Измеряется в количестве тиков

    private var moveTime = 200
    private var rotateTime = 300

    fun setTime(tickLength: Int) {
        moveTime = movesPerTick * tickLength
        rotateTime = rotatesPerTick * tickLength
    }

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
                    2F,
                    afraidOfPredator
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

    var isFirstMove = true
    var isFirstRotate = true
    fun update(deltaTime: Long, tickLength: Int) {
        if (deltaTime > moveTime + rotateTime && !isFirstMove) {
            time = System.currentTimeMillis()

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
                plantsList.removeAt(i)
            for (i in deathHerbivoresIndices)
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
                    (50..60).random().toFloat() / 10,
                    (20..30).random().toFloat() / 20,
                    0F,
                    2F
                )

            for (i in breedingIndicesHerbivore)
                herbivoresList += HerbivoreV(
                    Point((0..99).random().toFloat(), (0..99).random().toFloat()),
                    (100..200).random().toFloat() / 10,
                    (50..60).random().toFloat() / 10,
                    (20..30).random().toFloat() / 20,
                    0F,
                    2F,
                    (0..1).random() == 1
                )

            deathHerbivoresIndices.clear()
            deathPlantsIndices.clear()
            breedingIndicesPredator.clear()
            breedingIndicesHerbivore.clear()

            for (herbivore in herbivoresList) {
                val index = herbivore.setDirection(herbivoresList, predatorsList, plantsList)
                if (index != -1)
                    deathPlantsIndices.add(index)
            }
            for (predator in predatorsList) {
                val index = predator.setDirection(herbivoresList, predatorsList, plantsList)
                if (index != -1)
                    deathHerbivoresIndices.add(index)
            }

            for (herbivore in herbivoresList)
                herbivore.rollBack()
            for (predator in predatorsList)
                predator.rollBack()

            timeStats.addTo(timeStats.herbivoresAmount, herbivoresList.size)
            timeStats.addTo(timeStats.predatorsAmount, predatorsList.size)
            timeStats.addTo(timeStats.plantsAmount, plantsList.size)

            isFirstMove = true
            isFirstRotate = true

        } else {
            if (deltaTime <= rotateTime) {
                for (herbivore in herbivoresList)
                    herbivore.rotate(deltaTime, rotateTime)
                for (predator in predatorsList)
                    predator.rotate(deltaTime, rotateTime)
            } else {
                if (isFirstRotate) {
                    for (herbivore in herbivoresList)
                        herbivore.rotate(rotateTime.toLong(), rotateTime)
                    for (predator in predatorsList)
                        predator.rotate(rotateTime.toLong(), rotateTime)
                    isFirstRotate = false
                } else {
                    if (deltaTime <= rotateTime + moveTime) {
                        for (herbivore in herbivoresList)
                            herbivore.move(deltaTime - rotateTime, moveTime)
                        for (predator in predatorsList)
                            predator.move(deltaTime - rotateTime, moveTime)
                    } else {
                        for (herbivore in herbivoresList)
                            herbivore.move(moveTime.toLong(), moveTime)
                        for (predator in predatorsList)
                            predator.move(moveTime.toLong(), moveTime)
                        isFirstMove = false
                    }
                }
            }
        }
    }
}