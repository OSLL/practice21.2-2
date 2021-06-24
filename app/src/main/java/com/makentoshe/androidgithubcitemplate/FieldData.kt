package com.makentoshe.androidgithubcitemplate

class FieldData {
    // Размеры поля
    private var w = 100
    private var h = 130
    var fieldSizeW = w
    var fieldSizeH = h

    var ratio = 1f

    fun initBaseSize(w: Int, h: Int) {
        this.w = w
        this.h = h
        fieldSizeW = w
        fieldSizeH = h
    }

    fun setFieldSize(ratio: Float) {
        fieldSizeW = (w * ratio.coerceAtLeast(0.2f)).toInt()
        fieldSizeH = (h * ratio.coerceAtLeast(0.2f)).toInt()
        this.ratio = ratio
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
    fun fillLists(predatorsCount: Int, herbivoresCount: Int, plantsCount: Int) {
        if (!constastParametersAreSet) {
            for (i in 0 until predatorsCount)
                predatorsList += PredatorV(
                    Point(
                        (2..fieldSizeW - 3).random().toFloat(),
                        (2..fieldSizeH - 3).random().toFloat()
                    ),
                    (100..200).random().toFloat() / 10,
                    (50..70).random().toFloat() / 10,
                    (40..60).random().toFloat() / 10,
                    ((-314159265..314159265).random() / 200000000).toFloat(),
                    2F
                )
            for (i in 0 until herbivoresCount)
                herbivoresList += HerbivoreV(
                    Point(
                        (2..fieldSizeW - 3).random().toFloat(),
                        (2..fieldSizeH - 3).random().toFloat()
                    ),
                    (100..200).random().toFloat() / 10,
                    (50..70).random().toFloat() / 10,
                    (40..60).random().toFloat() / 10,
                    ((-314159265..314159265).random() / 200000000).toFloat(),
                    2F
                )
        } else {
            val fieldOfView = (100..200).random().toFloat() / 10
            val speed = (50..70).random().toFloat() / 10
            val baseRotationSpeed = (40..60).random().toFloat() / 10

            for (i in 0 until predatorsCount)
                predatorsList += PredatorV(
                    Point(
                        (2..fieldSizeW - 3).random().toFloat(),
                        (2..fieldSizeH - 3).random().toFloat()
                    ),
                    (100..200).random().toFloat() / 10,
                    (50..70).random().toFloat() / 10,
                    (40..60).random().toFloat() / 10,
                    ((-314159265..314159265).random() / 200000000).toFloat(),
                    2F
                )
            for (i in 0 until herbivoresCount)
                herbivoresList += HerbivoreV(
                    Point(
                        (2..fieldSizeW - 3).random().toFloat(),
                        (2..fieldSizeH - 3).random().toFloat()
                    ),
                    (100..200).random().toFloat() / 10,
                    (50..70).random().toFloat() / 10,
                    (40..60).random().toFloat() / 10,
                    ((-314159265..314159265).random() / 200000000).toFloat(),
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

    /* -----------------------------Настраиваемые переменные------------------------------- */
    // Время до подсчёта новго пути
    var minStraightWalkTime = 1000
    var maxStraightWalkTime = 3000

    fun setStraightMovement(min_in_ms: Int, max_in_ms: Int) {
        var min = min_in_ms.coerceIn(100..20000)
        val max = max_in_ms.coerceIn(100..20000)

        if (min > max)
            min = max

        minStraightWalkTime = min
        maxStraightWalkTime = max
    }

    // Размножение растений
    private var time = System.currentTimeMillis()
    private var spawnTime = 1500
    private var maxPlantsAmount = 200
    private var spawnPlantsPerSpawnTime = 1

    fun getMaxPlantAmount(): Int {
        return maxPlantsAmount
    }

    fun getSpawnTime(): Int {
        return spawnTime
    }

    fun getSpawnPlantsPerSpawnTime(): Int {
        return spawnPlantsPerSpawnTime
    }

    fun setMaxPlantsAmount(value: Int) {
        maxPlantsAmount = value.coerceIn(0..1000)
    }

    fun setSpawnTime(newTime: Int) {
        spawnTime = newTime.coerceAtLeast(100)
    }

    fun setPlantsPerSpawnTime(amount: Int) {
        spawnPlantsPerSpawnTime = amount.coerceIn(0..maxPlantsAmount)
    }

    // Все ли будут начинать с одинаковыми параметрами
    var constastParametersAreSet = false

    // Эволюционный коэффициент
    private var evoRatioForFOV = 1f
    private var evoRatioForSpeed = 1f
    private var evoRatioForRSpeed = 1f

    fun getEvolutionRatio(): Float {
        return (evoRatioForSpeed + evoRatioForFOV + evoRatioForRSpeed) / 3
    }

    fun setEvolutionRatio(ratio: Float) {
        setEvolutionRatioForFOV(ratio)
        setEvolutionRatioForSpeed(ratio)
        setEvolutionRatioForRSpeed(ratio)
    }

    fun getEvolutionRatioForFOV(): Float {
        return evoRatioForFOV
    }

    fun setEvolutionRatioForFOV(ratio: Float) {
        evoRatioForFOV = ratio.coerceIn(0f..10f)
    }

    fun getEvolutionRatioForSpeed(): Float {
        return evoRatioForSpeed
    }

    fun setEvolutionRatioForSpeed(ratio: Float) {
        evoRatioForSpeed = ratio.coerceIn(0f..10f)
    }

    fun getEvolutionRatioForRSpeed(): Float {
        return evoRatioForRSpeed
    }

    fun setEvolutionRatioForRSpeed(ratio: Float) {
        evoRatioForRSpeed = ratio.coerceIn(0f..10f)
    }

    // Коэффициент голода
    var hungerRatio = 1f

    fun setHungerRatiof(value: Float) {
        hungerRatio = value.coerceAtLeast(0f)
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

        for (i in breedingIndicesPredator) {
            val newFieldOfView =
                (predatorsList[i].fieldOfView + (-20..20).random() / 10f * evoRatioForFOV).coerceAtLeast(
                    0f
                )
            val newSpeed =
                (predatorsList[i].speed + (-10..10).random() / 10f * evoRatioForSpeed).coerceAtLeast(
                    0f
                )
            val newBaseRotationSpeed =
                (predatorsList[i].baseRotationSpeed + (-9..9).random() / 10f * evoRatioForRSpeed).coerceAtLeast(
                    0f
                )
            val newOrientation = ((-314159265..314159265).random() / 200000000).toFloat()

            predatorsList += PredatorV(
                predatorsList[i].pos,
                newFieldOfView,
                newSpeed,
                newBaseRotationSpeed,
                newOrientation,
                2F
            )
        }

        for (i in breedingIndicesHerbivore) {
            val newFieldOfView =
                (predatorsList[i].fieldOfView + (-20..20).random() / 10f * evoRatioForFOV).coerceAtLeast(
                    0f
                )
            val newSpeed =
                (predatorsList[i].speed + (-10..10).random() / 10f * evoRatioForSpeed).coerceAtLeast(
                    0f
                )
            val newBaseRotationSpeed =
                (predatorsList[i].baseRotationSpeed + (-9..9).random() / 10f * evoRatioForRSpeed).coerceAtLeast(
                    0f
                )
            val newOrientation = ((-314159265..314159265).random() / 200000000).toFloat()

            herbivoresList += HerbivoreV(
                herbivoresList[i].pos,
                newFieldOfView,
                newSpeed,
                newBaseRotationSpeed,
                newOrientation,
                2F
            )
        }

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

        if (System.currentTimeMillis() - time > spawnTime) {
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
        } else
            ticksPassed++
    }
}