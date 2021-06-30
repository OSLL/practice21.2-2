package com.makentoshe.androidgithubcitemplate

import kotlin.math.*

class PredatorV(
    var pos: Point,                         // Положение животного относительно левого верхнего угла поля
    val fieldOfView: Float,                 // Область, в которой животное видит объекты (размер поля - 100)
    val speed: Float,                       // Скорость, с которой двигается животное (единицы в установленный промежуток) (размер поля - 100)
    val baseRotationSpeed: Float,           // Скорость поворота
    var orientation: Float,                 // Угол поворота животного относительно горизонтальной оси
    val pointsForBreeding: Float            // Количество очков, необходимых для размножения
) {
    var currentPoints = (-1..1).random().toFloat()    // Текущие очки

    var size = 2 - 1 / (currentPoints + 5.75f)
    private val const = 1.5f                // Константа для подсчёта очков относительно веса
    private var rotationSpeed = baseRotationSpeed / size / size / 2
    private var energyConsumptionPerUnit =
        0.0003f * fieldData.hungerRatio * size * size * speed * fieldOfView * rotationSpeed / pointsForBreeding

    private var dangle = orientation
    private var oldAngle = orientation
    private var needToRotate = false
    private val criticalAngle = PI / 45

    var time = System.currentTimeMillis()

    private var rndTime = System.currentTimeMillis()
    private var rndt = (fieldData.minStraightWalkTime..fieldData.maxStraightWalkTime).random()

    /* Основная функция класса, отвечающая за поведение
     * Возвращает координату съеденного объекта или (-1, -1), если никто не был съеден
     */
    fun move(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>,         // Список всех растений
        speed1: Float                            // Время после прошлого перемещения
    ): Int {
        val dt = (System.currentTimeMillis() - time) / 1000f * speed1
        time = System.currentTimeMillis()

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

                if (fieldData.deathHerbivoresIndices.any {it == minDst.index})
                    continue

                val herbivore = herbivores[minDst.index]

                if (minDst.len < speed * dt) {
                    val lx = minDst.len * cos(minDst.angle)
                    val ly = minDst.len * sin(minDst.angle)

                    pos = Point(pos.x + lx, pos.y + ly)

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

                    currentPoints -= energyConsumptionPerUnit * speed * dt
                    currentPoints += herbivore.size * const
                    return minDst.index
                }

                if (dx + pos.x in (size)..(fieldData.fieldSizeW - 1 - size) &&
                    dy + pos.y in (size)..(fieldData.fieldSizeH - 1 - size)
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

                rndt = (fieldData.minStraightWalkTime..fieldData.maxStraightWalkTime).random()

                if (System.currentTimeMillis() - rndTime > rndt) {
                    rndTime = System.currentTimeMillis()
                    angle = (-180..180).random() / 180f * PI.toFloat()
                }

                var dx = dlen * cos(angle)
                var dy = dlen * sin(angle)

                if (dx + pos.x in (size)..(fieldData.fieldSizeW - 1 - size) &&
                    dy + pos.y in (size)..(fieldData.fieldSizeH - 1 - size)
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

                    if (dx + pos.x in (size)..(fieldData.fieldSizeW - 1 - size) &&
                        dy + pos.y in (size)..(fieldData.fieldSizeH - 1 - size)
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
                        rndTime -= 1000
                    }
                }
            }
            resize()
        } else {
            rotate(dt, speed1)

            val newX = pos.x + speed * cos(orientation) * dt
            val newY = pos.y + speed * sin(orientation) * dt
            if (newX in (size..fieldData.fieldSizeW - 1 - size) &&
                newY in (size..fieldData.fieldSizeH - 1 - size)
            ) {
                pos = Point(newX, newY)
                for (herbivore in herbivores)
                    if (length(
                            newX - herbivore.pos.x,
                            newY - herbivore.pos.y
                        ) < herbivore.size + size
                    ) {
                        currentPoints += herbivore.size * const
                        return herbivores.indexOf(herbivore)
                    }
            }
        }
        return -1
    }

    private class MinDistanceWithIndex(var len: Float, var angle: Float, var index: Int)


    private fun length(dX: Float, dY: Float): Float {
        return sqrt(dX * dX + dY * dY)
    }

    private fun rotate(dt: Float, speed1: Float) {
        rndTime = System.currentTimeMillis()

        if (orientation !in (oldAngle + dangle - 2 * PI.toFloat()..oldAngle + dangle + 2 * PI.toFloat())) {
            orientation = 0f
            oldAngle = 0f
            dangle = 0f
            needToRotate = false
        }

        for (i in 0..rotationSpeed.toInt())
            if (orientation !in (oldAngle + dangle - 2 * criticalAngle * speed1..oldAngle + dangle + 2 * criticalAngle * speed1) && needToRotate) {
                orientation += dangle * dt
            } else {
                orientation = oldAngle + dangle
                needToRotate = false
            }
    }

    private fun resize() {
        size = 2 - 1 / (currentPoints + 5.75f)
        energyConsumptionPerUnit =
            0.0003f * fieldData.hungerRatio * size * size * speed * fieldOfView * rotationSpeed / pointsForBreeding
        rotationSpeed = baseRotationSpeed / (size * size)
    }
}