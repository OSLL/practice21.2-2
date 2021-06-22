package com.makentoshe.androidgithubcitemplate

import kotlin.math.*

class PredatorV(
    var pos: Point,                 // Положение животного относительно левого верхнего угла поля
    private val fieldOfView: Float, // Область, в которой животное видит объекты (размер поля - 100)
    private val speed: Float,       // Скорость, с которой двигается животное (единицы в установленный промежуток) (размер поля - 100)
    private val rotationSpeed: Float,      // Скорость поворота
    var size: Float,                // Размеры животного относительно базовой модельки
    var orientation: Float,         // Угол поворота животного относительно горизонтальной оси
    val pointsForBreeding: Float    // Количество очков, необходимых для размножения
) {
    var currentPoints = 0F          // Текущие очки
    private val const = 1           // Константа для подсчёта очков относительно веса

    private val enegryConsumptionPerUnit =
        0.0001f * size * size * speed * fieldOfView / pointsForBreeding

    private var dangle = orientation
    private var oldAngle = orientation
    private var needToRotate = false
    private val critAngle = PI / 45

    private var time = System.currentTimeMillis()

    /* Основная функция класса, отвечающая за поведение
     * Возвращает координату съеденного объекта или (-1, -1), если никто не был съеден
     */
    fun move(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>,         // Список всех растений
        dt: Float                            // Время после прошлого перемещения
    ): Int {
        var isMoved = false

        val indexList = mutableListOf<MinDistanceWithIndex>()

        if (!needToRotate) {
            for (herbivore in herbivores) {
                val lx = herbivore.pos.x - pos.x
                val ly = herbivore.pos.y - pos.y
                val len = length(lx, ly)

                if (len < fieldOfView) {
                    val minDst = MinDistanceWithIndex(-1f, -1f, -1)
                    minDst.len = len
                    minDst.angle = when {
                        lx > 0 -> atan(ly / lx)
                        lx < 0 -> atan(ly / lx) + PI.toFloat()
                        else -> sign(ly) * PI.toFloat() / 2
                    }
                    minDst.index = herbivores.indexOf(herbivore)
                    indexList.add(minDst)
                }
            }
            indexList.sortBy { it.len }

            for (minDst in indexList) {
                val dx = speed * cos(minDst.angle) * dt
                val dy = speed * sin(minDst.angle) * dt

                val herbivore = herbivores[minDst.index]

                if (minDst.len < speed * dt) {
                    val lx = minDst.len * cos(minDst.angle)
                    val ly = minDst.len * sin(minDst.angle)

                    pos = Point(pos.x + lx, pos.y + ly)

                    if (minDst.angle - orientation > PI)
                        dangle = 2 * PI.toFloat() - (minDst.angle - orientation)
                    else if (minDst.angle - orientation < -PI)
                        dangle = 2 * PI.toFloat() + minDst.angle - orientation
                    else
                        dangle = minDst.angle - orientation
                    oldAngle = orientation
                    if (dangle > -critAngle && dangle < critAngle)
                        orientation = minDst.angle
                    else
                        needToRotate = true

                    currentPoints -= enegryConsumptionPerUnit * speed * dt
                    currentPoints += herbivore.size * const
                    return minDst.index
                }

                if (dx + pos.x in 0f..99f && dy + pos.y in 0f..99f) {
                    var isPredatorFound = false
                    for (predator in predators)
                        if (length(
                                pos.x + dx - predator.pos.x,
                                pos.y + dy - predator.pos.y
                            ) < size + predator.size && predator != this
                        )
                            isPredatorFound = true
                    if (isPredatorFound)
                        continue

                    var isPlantFound = false
                    for (plant in plants)
                        if (length(
                                pos.x + dx - plant.pos.x,
                                pos.y + dy - plant.pos.y
                            ) < size + plant.size
                        )
                            isPlantFound = true
                    if (isPlantFound)
                        continue

                    if (minDst.angle - orientation > PI)
                        dangle = 2 * PI.toFloat() - (minDst.angle - orientation)
                    else if (minDst.angle - orientation < -PI)
                        dangle = 2 * PI.toFloat() + minDst.angle - orientation
                    else
                        dangle = minDst.angle - orientation
                    oldAngle = orientation
                    if (dangle > -critAngle && dangle < critAngle)
                        orientation = minDst.angle
                    else
                        needToRotate = true

                    pos = Point(pos.x + dx, pos.y + dy)

                    currentPoints -= enegryConsumptionPerUnit * speed * dt

                    if (length(
                            pos.x - herbivore.pos.x,
                            pos.y - herbivore.pos.y
                        ) < size + herbivore.size
                    ) {
                        currentPoints += herbivore.size * const
                        return minDst.index
                    }
                    isMoved = true

                    break
                }
            }

            if (!isMoved) {
                val dlen = speed * dt
                var angle = orientation
                val rndt = (900..10000).random()

                if (System.currentTimeMillis() - time > rndt) {
                    time = System.currentTimeMillis()
                    angle = (-180..180).random() / 180f * PI.toFloat()
                }

                var dx = dlen * cos(angle)
                var dy = dlen * sin(angle)

                var isPlantCollisionFound = false
                for (plant in plants)
                    if (length(
                            pos.x + dx - plant.pos.x,
                            pos.y + dy - plant.pos.y
                        ) < plant.size + size
                    )
                        isPlantCollisionFound = true

                if (!isPlantCollisionFound &&
                    pos.x + dx < 100 &&
                    pos.x + dx >= 0 &&
                    pos.y + dy < 100 &&
                    pos.y + dy >= 0
                ) {
                    if (angle - orientation > PI)
                        dangle = 2 * PI.toFloat() - (angle - orientation)
                    else if (angle - orientation < -PI)
                        dangle = 2 * PI.toFloat() + angle - orientation
                    else
                        dangle = angle - orientation
                    oldAngle = orientation
                    if (dangle > -critAngle && dangle < critAngle)
                        orientation = angle
                    else
                        needToRotate = true

                    pos = Point(pos.x + dx, pos.y + dy)

                    currentPoints -= enegryConsumptionPerUnit * speed * dt
                } else {
                    angle = -angle
                    dx = dlen * cos(angle)
                    dy = dlen * sin(angle)

                    if (pos.x + dx < 100 &&
                        pos.x + dx >= 0 &&
                        pos.y + dy < 100 &&
                        pos.y + dy >= 0
                    ) {
                        if (angle - orientation > PI)
                            dangle = 2 * PI.toFloat() - (angle - orientation)
                        else if (angle - orientation < -PI)
                            dangle = 2 * PI.toFloat() + angle - orientation
                        else
                            dangle = angle - orientation
                        oldAngle = orientation
                        if (dangle > -critAngle && dangle < critAngle)
                            orientation = angle
                        else
                            needToRotate = true
                        pos = Point(pos.x + dx, pos.y + dy)

                        currentPoints -= enegryConsumptionPerUnit * speed * dt
                    }
                }
            }
        } else
            rotate(dt)
        return -1
    }

    private class MinDistanceWithIndex(var len: Float, var angle: Float, var index: Int)


    private fun length(dX: Float, dY: Float): Float {
        return sqrt(dX * dX + dY * dY)
    }

    private fun rotate(dt: Float) {
        time = System.currentTimeMillis()
        
        for (i in 0..rotationSpeed.toInt())
            if (orientation !in (oldAngle + dangle - critAngle..oldAngle + dangle + critAngle) && needToRotate)
                orientation += dangle * dt
            else {
                orientation = oldAngle + dangle
                needToRotate = false
            }
    }
}