package com.makentoshe.androidgithubcitemplate

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sign

class PredatorV(
    var position: Point,            // Положение животного относительно левого верхнего угла поля
    private val fieldOfView: Float, // Область, в которой животное видит объекты (размер поля - 100)
    private val speed: Float,       // Скорость, с которой двигается животное (единицы в секунду) (размер поля - 100)
    var size: Float,                // Размеры животного относительно базовой модельки
    var orientation: Float,         // Угол поворота животного относительно горизонтальной оси
    val pointsForBreeding: Float    // Количество очков, необходимых для размножения
) {
    var currentPoints = 0F          // Текущие очки
    private val const = 1           // Константа для подсчёта очков относительно веса

    private var moveX = 0F
    private var moveY = 0F
    private var oldPosition = position

    class ReturnData(
        val isMoved: Boolean,
        val index: Int
    )

    /* Основная функция класса, отвечающая за поведение
     * Возвращает координату съеденного объекта или (-1, -1), если никто не был съеден
     */
    fun setDirection(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>          // Список всех растений
    ): Int {
        var isMoved = false

        oldPosition = position

        val retData = herbivoresCheck(herbivores, predators, plants, isMoved)
        isMoved = retData.isMoved

        if (!isMoved) // Если нету травоядных в поле зрения
        {
            val switch = (0..1).random()
            val moveX: Float
            val moveY: Float

            if (switch == 0) {
                moveX = (-1..1).random() * speed
                moveY = (-1000..1000).random().toFloat() / 1000
            } else {
                moveY = (-1..1).random() * speed
                moveX = (-1000..1000).random().toFloat() / 1000
            }

            var isPlantCollisionFound = false
            for (plant in plants)
                if ((position.x + size + moveX in (plant.position.x - plant.size)..(plant.position.x + plant.size) &&
                            position.y + size + moveY in (plant.position.y - plant.size)..(plant.position.y + plant.size)) ||
                    (position.x + moveX == plant.position.x && position.y + moveY == plant.position.y)
                )
                    isPlantCollisionFound = true


            if (!isPlantCollisionFound &&
                position.x + moveX < 100 &&
                position.x + moveX >= 0 &&
                position.y + moveY < 100 &&
                position.y + moveY >= 0
            ) {
                if (moveX > 0)
                    orientation = atan(moveY / moveX)
                if (moveX < 0)
                    orientation = PI.toFloat() + atan(moveY / moveX)
                if (moveX == 0F)
                    orientation = PI.toFloat() / 2 * sign(moveY)

                this.moveX = moveX
                this.moveY = moveY
                oldPosition = position

                position = Point(
                    position.x + moveX,
                    position.y + moveY
                )
            }
        }
        return retData.index
    }

    private fun herbivoresCheck(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>,          // Список всех растений
        isMoved1: Boolean
    ): ReturnData {
        var isMoved = isMoved1

        for (herbivore in herbivores)
            if (!isMoved) {
                val x = herbivore.position.x
                val y = herbivore.position.y
                var moveX: Float
                var moveY: Float

                if (abs(x - position.x) > abs(y - position.y)) {
                    moveX = if (x - position.x == 0F)
                        0F
                    else
                        (x - position.x) / abs(x - position.x) * speed

                    moveY = if (x - position.x == 0F)
                        if (y - position.y == 0F)
                            0F
                        else
                            (y - position.y) / abs(y - position.y) * speed
                    else
                        (y - position.y) / abs(x - position.x) * speed
                } else {
                    moveY = if (y - position.y == 0F)
                        0F
                    else
                        (y - position.y) / abs(y - position.y) * speed

                    moveX = if (y - position.y == 0F)
                        if (x - position.x == 0F)
                            0F
                        else
                            (x - position.x) / abs(x - position.x) * speed
                    else
                        (x - position.x) / abs(y - position.y) * speed
                }

                if (abs(x - position.x) < speed && abs(y - position.y) < speed) {
                    moveX = x - position.x
                    moveY = y - position.y
                }

                if (abs(x - position.x) < fieldOfView &&
                    abs(y - position.y) < fieldOfView &&
                    position.x + moveX < 100 &&
                    position.x + moveX >= 0 &&
                    position.y + moveY < 100 &&
                    position.y + moveY >= 0
                ) {

                    var isPredatorCollisionFound = false
                    for (predator1 in predators)
                        if ((position.x + moveX in (predator1.position.x - size)..(predator1.position.x + size) &&
                                    position.y + moveY in (predator1.position.y - size)..(predator1.position.y + size)) ||
                            (position.x + moveX == predator1.position.x && position.y + moveY == predator1.position.y)
                        )
                            isPredatorCollisionFound = true

                    var isPlantCollisionFound = false
                    for (plant in plants)
                        if ((position.x + moveX in (plant.position.x - size)..(plant.position.x + size) &&
                                    position.y + moveY in (plant.position.y - size)..(plant.position.y + size)) ||
                            (position.x + moveX == plant.position.x && position.y + moveY == plant.position.y)
                        )
                            isPlantCollisionFound = true


                    if (!isPredatorCollisionFound && !isPlantCollisionFound) {
                        if (moveX > 0)
                            orientation = atan(moveY / moveX)
                        if (moveX < 0)
                            orientation = PI.toFloat() + atan(moveY / moveX)
                        if (moveX == 0F)
                            orientation = PI.toFloat() / 2 * sign(moveY)

                        this.moveX = moveX
                        this.moveY = moveY
                        oldPosition = position

                        position = Point(position.x + moveX, position.y + moveY)
                        isMoved = true

                        for (herbivore1 in herbivores)
                            if (position.x in (herbivore1.position.x - size)..(herbivore1.position.x + size) &&
                                position.y in (herbivore1.position.y - size)..(herbivore1.position.y + size) ||
                                (position.x == herbivore1.position.x && position.y == herbivore1.position.y)
                            ) {
                                currentPoints += herbivore1.size * const
                                return ReturnData(isMoved, herbivores.indexOf(herbivore1))
                            }
                    }
                    break
                }
            }
        return ReturnData(isMoved, -1)
    }

    fun rollBack() {
        position = oldPosition
    }

    fun move(current_dt: Long, max_dt: Int) {
        if (current_dt <= max_dt) {
            position = Point(
                oldPosition.x + moveX * current_dt / max_dt,
                oldPosition.y + moveY * current_dt / max_dt
            )
        }
    }
}