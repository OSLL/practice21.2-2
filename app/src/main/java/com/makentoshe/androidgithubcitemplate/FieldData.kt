package com.makentoshe.androidgithubcitemplate

class FieldData {
    // Размеры поля
    var fieldSizeW = 100
    var fieldSizeH = 130

    fun setFieldSize(width: Int, height: Int) {
        fieldSizeW = width
        fieldSizeH = height
    }

    // Списки с животными и растениями
    val predatorsList = mutableListOf<PredatorV>()
    val herbivoresList = mutableListOf<HerbivoreV>()
    val plantsList = mutableListOf<PlantV>()

    // Индексы мёртвых животных и растений в списках
    private val deathPlantsIndices = mutableListOf<Int>()
    private val deathHerbivoresIndices = mutableListOf<Int>()
    private val deathFromHungerIndicesPredators = mutableListOf<Int>()

    // Индексы размножившихся животных и растений в списках
    private val breedingIndicesPredator = mutableListOf<Int>()
    private val breedingIndicesHerbivore = mutableListOf<Int>()
    private val breedingIndicesPlant = mutableListOf<Int>()

    private var ticksPassed = 0
    var timeStats = TimeStatistic()

    /* Функции очистки списков */
    fun clearHerbivores() {
        herbivoresList.clear()
    }

    fun clearPlants() {
        plantsList.clear()
    }

    fun clearPredators() {
        predatorsList.clear()
    }

    fun clearAll() {
        clearHerbivores()
        clearPlants()
        clearPredators()
    }

    /* Функции добавления в списки */
    fun addHerbivores(amount: Int) {
        for (i in 0 until amount)
            herbivoresList += HerbivoreV(
                Point(
                    (2..fieldSizeW - 3).random().toFloat(),
                    (2..fieldSizeH - 3).random().toFloat()
                ),
                (100..200).random().toFloat() / 10,
                (50..70).random().toFloat() / 10,
                (40..60).random().toFloat() / 10,
                (20..35).random().toFloat() / 20,
                0F,
                2F
            )
    }

    fun addPredators(amount: Int) {
        for (i in 0 until amount)
            predatorsList += PredatorV(
                Point(
                    (2..fieldSizeW - 3).random().toFloat(),
                    (2..fieldSizeH - 3).random().toFloat()
                ),
                (100..200).random().toFloat() / 10,
                (50..70).random().toFloat() / 10,
                (40..60).random().toFloat() / 10,
                (20..35).random().toFloat() / 20,
                0F,
                2F
            )
    }

    fun addPlants(amount: Int) {
        for (i in 0 until amount)
            plantsList += PlantV(
                Point(
                    (2..fieldSizeW - 2).random().toFloat(),
                    (2..fieldSizeH - 2).random().toFloat()
                ),
                (10..30).random().toFloat() / 20,
                (5..30).random().toFloat() / 10
            )
    }

    fun fillLists(predatorsCount: Int, herbivoresCount: Int, plantsCount: Int) {
        for (i in 0 until predatorsCount)
            predatorsList += PredatorV(
                Point(
                    (2..fieldSizeW - 3).random().toFloat(),
                    (2..fieldSizeH - 3).random().toFloat()
                ),
                (100..200).random().toFloat() / 10,
                (50..70).random().toFloat() / 10,
                (40..60).random().toFloat() / 10,
                (20..35).random().toFloat() / 20,
                0F,
                2F
            )
        for (i in 0 until herbivoresCount) {
            herbivoresList += HerbivoreV(
                Point(
                    (2..fieldSizeW - 3).random().toFloat(),
                    (2..fieldSizeH - 3).random().toFloat()
                ),
                (100..200).random().toFloat() / 10,
                (50..70).random().toFloat() / 10,
                (40..60).random().toFloat() / 10,
                (20..35).random().toFloat() / 20,
                0F,
                2F
            )
        }
        for (i in 0 until plantsCount)
            plantsList += PlantV(
                Point(
                    (2..fieldSizeW - 2).random().toFloat(),
                    (2..fieldSizeH - 2).random().toFloat()
                ),
                (10..30).random().toFloat() / 20,
                (5..30).random().toFloat() / 10
            )
    }

    /* Настраиваемые переменные */
    var minStraightWalkTime = 1000
    var maxStraightWalkTime = 3000

    fun setStraightMovement(min_in_ms: Int, max_in_ms: Int) {
        var min = min_in_ms
        var max = max_in_ms

        if (min < 100)
            min = 100
        if (min > 20000)
            min = 20000
        if (max < 100)
            max = 100
        if (max > 20000)
            max = 20000
        if (min > max)
            min = max

        minStraightWalkTime = min
        maxStraightWalkTime = max
    }

    // Размножение растений
    private var time = System.currentTimeMillis()
    private var SpawnTime = 1500
    private var maxPlantsAmount = 200
    private var spawnPlantsPerSpawnTime = 1

    fun setMaxPlantsAmount(value: Int) {
        var valueTmp = value

        if (valueTmp < 0)
            valueTmp = 0
        if (valueTmp > 1000)
            valueTmp = 1000

        maxPlantsAmount = valueTmp
    }
    fun setSpawnTime(time_in_ms: Int) {
        var timeTmp = time_in_ms

        if (timeTmp < 100)
            timeTmp = 100

        SpawnTime = timeTmp
    }
    fun setPlantsPerSpawnTime(amount: Int) {
        var valueTmp = amount

        if (valueTmp < 0)
            valueTmp = 0
        if (valueTmp >= maxPlantsAmount)
            valueTmp = maxPlantsAmount - 1

        spawnPlantsPerSpawnTime = valueTmp
    }

    fun update(speed: Float) {

        // Перемещение с добавлением индексов мёртвых объектов
        for (herbivore in herbivoresList) {
            val index = herbivore.move(herbivoresList, predatorsList, plantsList, speed)
            if (index != -1 && !deathPlantsIndices.any { index == it })
                deathPlantsIndices.add(index)
        }
        for (predator in predatorsList) {
            val index = predator.move(herbivoresList, predatorsList, plantsList, speed)
            if (index != -1 && !deathHerbivoresIndices.any { index == it })
                deathHerbivoresIndices.add(index)
        }

        // "Убийство" объектов по сохранённым индексам
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


        // Размножение хищников и травоядных
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
                predatorsList[i].pos,
                (100..200).random().toFloat() / 10,
                (50..70).random().toFloat() / 10,
                (40..60).random().toFloat() / 10,
                (20..35).random().toFloat() / 20,
                0F,
                2F
            )

        for (i in breedingIndicesHerbivore)
            herbivoresList += HerbivoreV(
                herbivoresList[i].pos,
                (100..200).random().toFloat() / 10,
                (50..70).random().toFloat() / 10,
                (40..60).random().toFloat() / 10,
                (20..35).random().toFloat() / 20,
                0F,
                2F
            )

        // Рост и размножение растений
        for (i in plantsList.indices)
            if ((System.currentTimeMillis() - plantsList[i].time) * speed > plantsList[i].rndTime) {
                plantsList[i].size += (1..2).random() / 10f
                if (plantsList[i].size > 1.5) {
                    plantsList[i].size /= 2
                    breedingIndicesPlant += i
                }
                plantsList[i].recalculatePoints()

                plantsList[i].time = System.currentTimeMillis()
                plantsList[i].rndTime = (1000..5000).random()
            }

        for (i in breedingIndicesPlant) {
            var newX = plantsList[i].pos.x + (-2..2).random()
            if (newX > fieldSizeW - 2)
                newX -= 2
            if (newX < 2)
                newX += 2

            var newY = plantsList[i].pos.y + (-2..2).random()
            if (newY > fieldSizeH - 2)
                newY -= 2
            if (newY < 2)
                newY += 2

            plantsList += PlantV(
                Point(newX, newY),
                plantsList[i].size,
                (5..30).random().toFloat() / 10
            )
        }

        if (System.currentTimeMillis() - time > SpawnTime) {
            for (i in 0 until spawnPlantsPerSpawnTime) {
                plantsList += PlantV(
                    Point(
                        (2..fieldSizeW - 2).random().toFloat(),
                        (2..fieldSizeH - 2).random().toFloat()
                    ),
                    (10..20).random().toFloat() / 20,
                    (5..30).random().toFloat() / 10
                )
                time = System.currentTimeMillis()
            }
        }

        while (plantsList.size > maxPlantsAmount)
            plantsList.removeAt(0)

        deathHerbivoresIndices.clear()
        deathPlantsIndices.clear()
        deathFromHungerIndicesPredators.clear()
        
        breedingIndicesPredator.clear()
        breedingIndicesHerbivore.clear()
        breedingIndicesPlant.clear()

        if (ticksPassed == 10) {
            timeStats.addTo(timeStats.herbivoresAmount, herbivoresList.size)
            timeStats.addTo(timeStats.predatorsAmount, predatorsList.size)
            timeStats.addTo(timeStats.plantsAmount, plantsList.size)
            ticksPassed = 0
        }
        else
            ticksPassed++
    }
}