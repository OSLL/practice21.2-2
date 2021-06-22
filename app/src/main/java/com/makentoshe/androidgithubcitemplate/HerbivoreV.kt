package com.makentoshe.androidgithubcitemplate

import kotlin.math.*

class HerbivoreV(
    var pos: Point,                        // Положение животного относительно левого верхнего угла поля
    private val fieldOfView: Float,        // Область, в которой животное видит объекты (размер поля - 100)
    private val speed: Float,              // Скорость, с которой двигается животное (единицы в секунду) (размер поля - 100)
    var size: Float,                       // Размеры животного относительно базовой модельки
    var orientation: Float,                // Угол поворота животного относительно горизонтальной оси
    val pointsForBreeding: Float,          // Количество очков. необходимых для размножения
    private val afraidOfPredator: Boolean  // (Только для травоядных) приоритет ходьбы (идти к растению или бежать от животного)
) {
    var currentPoints = 0F                 // Текущие очки

    val enegryConsumptionPerUnit = 0.0002f * size * size * speed * fieldOfView / pointsForBreeding

    private var moveX = 0F                 // Запомненное перемещение по X (для плавного движения)
    private var moveY = 0F                 // Запомненное перемещение по Y (для плавного движения)
    private var oldPosition = pos          // Позиция до перемещения (для плавного движения)


    /* Основная функция класса, отвечающая за поведение
     * Возвращает координату съеденного объекта или (-1, -1), если никто не был съеден
     */
    fun setDirection(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>          // Список всех растений
    ): Int {
        var retData: ReturnData

        oldPosition = pos

        if (!afraidOfPredator) {
            retData = plantsCheck(herbivores, predators, plants)
            if (!retData.isMoved)
                retData = predatorsCheck(herbivores, predators, plants)
        } else {
            retData = predatorsCheck(herbivores, predators, plants)
            if (!retData.isMoved)
                retData = plantsCheck(herbivores, predators, plants)
        }

        if (!retData.isMoved) // Если нету растений или хищников в поле зрения
        {
            val dlen = (100..(speed * 100).toInt()).random() / 100f
            val angle = (0..(2 * PI * 100).toInt()).random() / 100f

            val dx = dlen * cos(angle)
            val dy = dlen * sin(angle)

            if (pos.x + dx < 100 &&
                pos.x + dx >= 0 &&
                pos.y + dy < 100 &&
                pos.y + dy >= 0
            ) {
                orientation = angle

                moveX = dx
                moveY = dy
                pos = Point(pos.x + dx, pos.y + dy)

                currentPoints -= enegryConsumptionPerUnit * dlen
            }
            else {
                moveX = 0f
                moveY = 0f
            }
        }
        return retData.index
    }

    private fun plantsCheck(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>          // Список всех растений
    ): ReturnData {
        val minDst = MinDistanceWithIndex(1000f, -1f, -1, 1000f)

        for (plant in plants) {
            val lx = plant.pos.x - pos.x
            val ly = plant.pos.y - pos.y
            val len = length(lx, ly)

            if (len >= fieldOfView)
                continue

            val angle = when {
                lx > 0 -> atan(ly / lx)
                lx < 0 -> atan(ly / lx) + PI.toFloat()
                else -> sign(ly) * PI.toFloat() / 2
            }

            var dx = speed * cos(angle)
            var dy = speed * sin(angle)

            val dlen: Float

            if (abs(lx) < abs(dx) && abs(ly) < abs(dy)) {
                dx = lx
                dy = ly
                dlen = length(dx, dy)
            } else
                dlen = speed

            dx += pos.x
            dy += pos.y

            if (dx < 0 || dx >= 100f || dy < 0 || dy >= 100f)
                continue

            var isPredatorCollisionFound = false
            for (predator in predators)
                if (length(dx - predator.pos.x, dy - predator.pos.y) < size + predator.size)
                    isPredatorCollisionFound = true

            var isHerbivoreCollisionFound = false
            for (herbivore in herbivores)
                if (length(dx - herbivore.pos.x, dy - herbivore.pos.y) < size + herbivore.size)
                    isHerbivoreCollisionFound = true

            if (isHerbivoreCollisionFound || isPredatorCollisionFound)
                continue

            if (minDst.len > len) {
                minDst.len = len
                minDst.value = dlen
                minDst.angle = angle
                minDst.index = plants.indexOf(plant)
            }
        }

        if (minDst.index == -1)
            return ReturnData(false, -1)

        val plant = plants[minDst.index]

        moveX = minDst.value * cos(minDst.angle)
        moveY = minDst.value * sin(minDst.angle)
        pos = Point(
            pos.x + moveX,
            pos.y + moveY
        )

        currentPoints -= enegryConsumptionPerUnit * minDst.value
        orientation = minDst.angle

        if (minDst.value < speed) {
            currentPoints += plant.pointsForEating
            return ReturnData(true, minDst.index)
        }

        return ReturnData(true, -1)
    }

    private fun predatorsCheck(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>          // Список всех растений
    ): ReturnData {
        val minDst = MinDistanceWithIndex(1000f, -1f, -1, 1000f)

        for (predator in predators) {
            val lx = pos.x - predator.pos.x
            val ly = pos.y - predator.pos.y
            val len = length(lx, ly)

            if (len >= fieldOfView)
                continue

            val angle = when {
                lx > 0 -> atan(ly / lx)
                lx < 0 -> atan(ly / lx) + PI.toFloat()
                else -> sign(ly) * PI.toFloat() / 2
            }

            var dx = speed * cos(angle)
            var dy = speed * sin(angle)

            val dlen: Float

            if (abs(lx) < abs(dx) && abs(ly) < abs(dy)) {
                dx = lx
                dy = ly
                dlen = length(dx, dy)
            } else
                dlen = speed

            dx += pos.x
            dy += pos.y

            if (dx < 0 || dx >= 100f || dy < 0 || dy >= 100f)
                continue

            var isPredatorCollisionFound = false
            for (predator1 in predators)
                if (length(dx - predator1.pos.x, dy - predator1.pos.y) < size + predator1.size)
                    isPredatorCollisionFound = true

            var isHerbivoreCollisionFound = false
            for (herbivore in herbivores)
                if (length(dx - herbivore.pos.x, dy - herbivore.pos.y) < size + herbivore.size)
                    isHerbivoreCollisionFound = true

            if (isHerbivoreCollisionFound || isPredatorCollisionFound)
                continue

            if (minDst.len > len) {
                minDst.len = len
                minDst.value = dlen
                minDst.angle = angle
                minDst.index = predators.indexOf(predator)
            }
        }

        if (minDst.index == -1)
            return ReturnData(false, -1)

        moveX = minDst.value * cos(minDst.angle)
        moveY = minDst.value * sin(minDst.angle)
        pos = Point(
            pos.x + moveX,
            pos.y + moveY
        )

        currentPoints -= enegryConsumptionPerUnit * minDst.value
        orientation = minDst.angle

        for (plant in plants)
            if (length(pos.x - plant.pos.x, pos.y - plant.pos.y) < size + plant.size) {
                currentPoints += plant.pointsForEating
                return ReturnData(true, plants.indexOf(plant))
            }
        return ReturnData(true, -1)
    }

    private class MinDistanceWithIndex(var value: Float, var angle: Float, var index: Int, var len: Float)

    private class ReturnData(
        val isMoved: Boolean,
        val index: Int
    )

    private fun length(dX: Float, dY: Float): Float {
        return sqrt(dX * dX + dY * dY)
    }

    fun rollBack() {
        pos = oldPosition
    }

    fun move(current_dt: Long, max_dt: Int) {
        if (current_dt <= max_dt) {
            pos = Point(
                oldPosition.x + moveX * current_dt / max_dt,
                oldPosition.y + moveY * current_dt / max_dt
            )
        }
    }
}