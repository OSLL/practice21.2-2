package com.makentoshe.androidgithubcitemplate

import kotlin.math.*

class HerbivoreV(
    var pos: Point,                         // Положение животного относительно левого верхнего угла поля
    private val fieldOfView: Float,         // Область, в которой животное видит объекты (размер поля - 100)
    private val speed: Float,               // Скорость, с которой двигается животное (единицы в установленный промежуток) (размер поля - 100)
    baseRotationSpeed: Float,   // Скорость поворота
    var size: Float,                        // Размеры животного относительно базовой модельки
    var orientation: Float,                 // Угол поворота животного относительно горизонтальной оси
    val pointsForBreeding: Float            // Количество очков, необходимых для размножения
) {
    var currentPoints = 0F                  // Текущие очки

    private val rotationSpeed = baseRotationSpeed / size / size

    private val energyConsumptionPerUnit =
        0.00002f * size * size * speed * fieldOfView / pointsForBreeding

    private var time = System.currentTimeMillis()

    private var dangle = orientation
    private var oldAngle = orientation
    private var needToRotate = false
    private val criticalAngle = PI / 45


    /* Основная функция класса, отвечающая за поведение
     * Возвращает координату съеденного объекта или (-1, -1), если никто не был съеден
     */
    fun move(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>,         // Список всех растений
        dt: Float                            // Время после прошлого перемещения
    ): Int {
        if (!needToRotate) {
            var isMoved = false
            val indexListPredator = mutableListOf<MinDistanceWithIndex>()
            val indexListPlant = mutableListOf<MinDistanceWithIndex>()

            for (plant in plants) {
                val lx = plant.pos.x - pos.x
                val ly = plant.pos.y - pos.y
                val len = length(lx, ly)

                if (len < fieldOfView) {
                    val minDst = MinDistanceWithIndex(-1f, -1f, -1)
                    minDst.len = len
                    minDst.angle = when {
                        lx > 0 -> atan(ly / lx)
                        lx < 0 -> atan(ly / lx) + PI.toFloat()
                        else -> sign(ly) * PI.toFloat() / 2
                    }
                    minDst.index = plants.indexOf(plant)
                    indexListPlant.add(minDst)
                }
            }

            for (predator in predators) {
                val lx = pos.x - predator.pos.x
                val ly = pos.y - predator.pos.y
                val len = length(lx, ly)

                if (len < fieldOfView) {
                    val minDst = MinDistanceWithIndex(-1f, -1f, -1)
                    minDst.len = len
                    minDst.angle = when {
                        lx > 0 -> atan(ly / lx)
                        lx < 0 -> atan(ly / lx) + PI.toFloat()
                        else -> sign(ly) * PI.toFloat() / 2
                    }
                    minDst.index = predators.indexOf(predator)
                    indexListPredator.add(minDst)
                }
            }

            indexListPlant.sortBy { it.len }
            indexListPredator.sortBy { it.len }

            val minDstToPredator = MinDistanceWithIndex(-1f, -1f, -1)

            for (minDst in indexListPredator) {
                val dx = speed * cos(minDst.angle) * dt
                val dy = speed * sin(minDst.angle) * dt

                if (dx + pos.x in (0f + size)..(99f - size) &&
                    dy + pos.y in (0f + size)..(99f - size)
                ) {
                    var isHerbivoreFound = false
                    for (herbivore in herbivores)
                        if (length(
                                pos.x + dx - herbivore.pos.x,
                                pos.y + dy - herbivore.pos.y
                            ) < size + herbivore.size && herbivore != this
                        )
                            isHerbivoreFound = true
                    if (isHerbivoreFound)
                        continue

                    minDstToPredator.len = minDst.len
                    minDstToPredator.index = minDst.index
                    minDstToPredator.angle = minDst.angle
                    break
                }
            }

            for (minDst in indexListPlant) {
                val dx = speed * cos(minDst.angle) * dt
                val dy = speed * sin(minDst.angle) * dt

                val plant = plants[minDst.index]

                if (minDst.len < speed * dt) {
                    val lx = minDst.len * cos(minDst.angle)
                    val ly = minDst.len * sin(minDst.angle)

                    dangle = when {
                        minDst.angle - orientation > PI -> 2 * PI.toFloat() - (minDst.angle - orientation)
                        minDst.angle - orientation < -PI -> 2 * PI.toFloat() + minDst.angle - orientation
                        else -> minDst.angle - orientation
                    }
                    oldAngle = orientation
                    if (dangle > -criticalAngle && dangle < criticalAngle)
                        orientation = minDst.angle
                    else
                        needToRotate = true

                    pos = Point(pos.x + lx, pos.y + ly)

                    currentPoints -= energyConsumptionPerUnit * speed * dt
                    currentPoints += plant.pointsForEating
                    return minDst.index
                }

                if (dx + pos.x in (0f + size)..(99f - size) &&
                    dy + pos.y in (0f + size)..(99f - size)
                ) {
                    var isHerbivoreFound = false
                    for (herbivore in herbivores)
                        if (length(
                                pos.x + dx - herbivore.pos.x,
                                pos.y + dy - herbivore.pos.y
                            ) < size + herbivore.size &&
                            herbivore != this
                        )
                            isHerbivoreFound = true
                    if (isHerbivoreFound)
                        continue

                    if (length(
                            pos.x + dx - plant.pos.x,
                            pos.y + dy - plant.pos.y
                        ) < size + plant.size
                    ) {
                        dangle = when {
                            minDst.angle - orientation > PI -> 2 * PI.toFloat() - (minDst.angle - orientation)
                            minDst.angle - orientation < -PI -> 2 * PI.toFloat() + minDst.angle - orientation
                            else -> minDst.angle - orientation
                        }
                        oldAngle = orientation
                        if (dangle > -criticalAngle && dangle < criticalAngle)
                            orientation = minDst.angle
                        else
                            needToRotate = true

                        pos = Point(pos.x + dx, pos.y + dy)

                        currentPoints -= energyConsumptionPerUnit * speed * dt
                        currentPoints += plant.pointsForEating
                        return minDst.index
                    }

                    if (minDstToPredator.index == -1 || minDstToPredator.len > minDst.len) {

                        dangle = when {
                            minDst.angle - orientation > PI -> 2 * PI.toFloat() - (minDst.angle - orientation)
                            minDst.angle - orientation < -PI -> 2 * PI.toFloat() + minDst.angle - orientation
                            else -> minDst.angle - orientation
                        }
                        oldAngle = orientation
                        if (dangle > -criticalAngle && dangle < criticalAngle)
                            orientation = minDst.angle
                        else
                            needToRotate = true

                        pos = Point(pos.x + dx, pos.y + dy)

                        currentPoints -= energyConsumptionPerUnit * speed * dt
                    } else {

                        dangle = when {
                            minDstToPredator.angle - orientation > PI -> 2 * PI.toFloat() - (minDstToPredator.angle - orientation)
                            minDstToPredator.angle - orientation < -PI -> 2 * PI.toFloat() + minDstToPredator.angle - orientation
                            else -> minDstToPredator.angle - orientation
                        }
                        oldAngle = orientation
                        if (dangle > -criticalAngle && dangle < criticalAngle)
                            orientation = minDstToPredator.angle
                        else
                            needToRotate = true

                        pos = Point(
                            pos.x + minDstToPredator.len * cos(minDstToPredator.angle) * dt,
                            pos.y + minDstToPredator.len * sin(minDstToPredator.angle) * dt
                        )

                        currentPoints -= energyConsumptionPerUnit * speed * dt
                    }

                    isMoved = true

                    break
                }
            }

            if (!isMoved && minDstToPredator.index != -1) {

                dangle = when {
                    minDstToPredator.angle - orientation > PI -> 2 * PI.toFloat() - (minDstToPredator.angle - orientation)
                    minDstToPredator.angle - orientation < -PI -> 2 * PI.toFloat() + minDstToPredator.angle - orientation
                    else -> minDstToPredator.angle - orientation
                }
                oldAngle = orientation
                if (dangle > -criticalAngle && dangle < criticalAngle)
                    orientation = minDstToPredator.angle
                else
                    needToRotate = true

                pos = Point(
                    pos.x + minDstToPredator.len * cos(minDstToPredator.angle) * dt,
                    pos.y + minDstToPredator.len * sin(minDstToPredator.angle) * dt
                )

                currentPoints -= energyConsumptionPerUnit * speed * dt
            } else if (!isMoved) {
                val dlen = speed * dt
                var angle = orientation
                val rndt = (900..10000).random()

                if (System.currentTimeMillis() - time > rndt) {
                    time = System.currentTimeMillis()
                    angle = (-180..180).random() / 180f * PI.toFloat()
                }

                var dx = dlen * cos(angle)
                var dy = dlen * sin(angle)

                if (dx + pos.x in (0f + size)..(99f - size) &&
                    dy + pos.y in (0f + size)..(99f - size)
                ) {
                    dangle = when {
                        angle - orientation > PI -> 2 * PI.toFloat() - (angle - orientation)
                        angle - orientation < -PI -> 2 * PI.toFloat() + angle - orientation
                        else -> angle - orientation
                    }
                    oldAngle = orientation
                    if (dangle > -criticalAngle && dangle < criticalAngle)
                        orientation = angle
                    else
                        needToRotate = true

                    pos = Point(pos.x + dx, pos.y + dy)

                    currentPoints -= energyConsumptionPerUnit * speed * dt
                } else {
                    angle = PI.toFloat() - angle
                    dx = dlen * cos(angle)
                    dy = dlen * sin(angle)
                    System.currentTimeMillis()

                    if (dx + pos.x in (0f + size)..(99f - size) &&
                        dy + pos.y in (0f + size)..(99f - size)
                    ) {
                        dangle = when {
                            angle - orientation > PI -> 2 * PI.toFloat() - (angle - orientation)
                            angle - orientation < -PI -> 2 * PI.toFloat() + angle - orientation
                            else -> angle - orientation
                        }
                        oldAngle = orientation
                        if (dangle > -criticalAngle && dangle < criticalAngle)
                            orientation = angle
                        else
                            needToRotate = true

                        pos = Point(pos.x + dx, pos.y + dy)

                        currentPoints -= energyConsumptionPerUnit * speed * dt
                    }
                }
            }
        } else {
            rotate(dt)

            val newX = pos.x + speed * cos(orientation) * dt
            val newY = pos.y + speed * sin(orientation) * dt
            if (newX in (size..99f - size) && newY in (size..99f - size))
                pos = Point(newX, newY)
        }
        return -1
    }

    private class MinDistanceWithIndex(
        var len: Float,
        var angle: Float,
        var index: Int
    )

    private fun length(dX: Float, dY: Float): Float {
        return sqrt(dX * dX + dY * dY)
    }

    private fun rotate(dt: Float) {
        time = System.currentTimeMillis()

        if (orientation !in (oldAngle + dangle - 4 * PI.toFloat()..oldAngle + dangle + 4 * PI.toFloat())) {
            orientation = oldAngle + dangle
            needToRotate = false
        }

        for (i in 0..rotationSpeed.toInt())
            if (orientation !in (oldAngle + dangle - 2 * criticalAngle..oldAngle + dangle + 2 * criticalAngle) && needToRotate) {
                orientation += dangle * dt
            }
            else {
                orientation = oldAngle + dangle
                needToRotate = false
            }
    }
}