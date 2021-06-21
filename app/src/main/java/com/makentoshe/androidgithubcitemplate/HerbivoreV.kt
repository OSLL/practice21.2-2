package com.makentoshe.androidgithubcitemplate

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sign

class HerbivoreV(
    var position: Point,            // Положение животного относительно левого верхнего угла поля
    private val fieldOfView: Float, // Область, в которой животное видит объекты (размер поля - 100)
    private val speed: Float,       // Скорость, с которой двигается животное (единицы в секунду) (размер поля - 100)
    var size: Float,                // Размеры животного относительно базовой модельки
    var orientation: Float,         // Угол поворота животного относительно горизонтальной оси
    val pointsForBreeding: Float,   // Количество очков. необходимых для размножения
    private val afraidOfPredator: Boolean   // (Только для травоядных) приоритет ходьбы (идти к растению или бежать от животного)
) {
    var currentPoints = 0F          // Текущие очки

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
        var retData: ReturnData

        oldPosition = position

        if (!afraidOfPredator) {
            retData = plantsCheck(herbivores, predators, plants, isMoved)
            if (retData.index == -1)
                retData = predatorsCheck(herbivores, predators, plants, retData.isMoved)
        } else {
            retData = predatorsCheck(herbivores, predators, plants, isMoved)
            if (retData.index == -1)
                retData = plantsCheck(herbivores, predators, plants, retData.isMoved)
        }

        isMoved = retData.isMoved

        if (!isMoved) // Если нету растений или хищников в поле зрения
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

            if (position.x + moveX < 100 &&
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

    private fun plantsCheck(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>,         // Список всех растений
        isMoved1: Boolean
    ): ReturnData {
        var isMoved = isMoved1

        for (plant in plants)
            if (!isMoved) {
                val x = plant.position.x
                val y = plant.position.y
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
                    for (predator in predators)
                        if ((position.x + moveX in (predator.position.x - size)..(predator.position.x + size) &&
                                    position.x + moveY in (predator.position.y - size)..(predator.position.y + size)) ||
                            (position.x + moveX == predator.position.x && position.y + moveY == predator.position.y)
                        )
                            isPredatorCollisionFound = true

                    var isHerbivoreCollisionFound = false
                    for (herbivore in herbivores)
                        if ((position.x + moveX in (herbivore.position.x - size)..(herbivore.position.x + size) &&
                                    position.y + moveY in (herbivore.position.y - size)..(herbivore.position.y + size)) ||
                            (position.x + moveX == herbivore.position.x && position.y + moveY == herbivore.position.y)
                        )
                            isHerbivoreCollisionFound = true

                    if (!isPredatorCollisionFound && !isHerbivoreCollisionFound) {
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

                        for (plant1 in plants)
                            if ((position.x in (plant1.position.x - size)..(plant1.position.x + size) &&
                                        position.y in (plant1.position.y - size)..(plant1.position.y + size)) ||
                                (position.x == plant1.position.x && position.y == plant1.position.y)
                            ) {
                                currentPoints += plant1.pointsForEating
                                return ReturnData(isMoved, plants.indexOf(plant))
                            }
                    }
                    break
                }
            }
        return ReturnData(isMoved, -1)
    }

    private fun predatorsCheck(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>,         // Список всех растений
        isMoved1: Boolean
    ): ReturnData {
        var isMoved = isMoved1

        for (predator in predators)
            if (!isMoved) {
                val x = predator.position.x
                val y = predator.position.y
                val moveX: Float
                val moveY: Float

                if (abs(x - position.x) > abs(y - position.y)) {
                    moveX = if (position.x - x == 0F)
                        0F
                    else
                        (position.x - x) / abs(position.x - x) * speed

                    moveY = if (position.x - x == 0F)
                        if (position.y - y == 0F)
                            0F
                        else
                            (position.y - y) / abs(position.y - y) * speed
                    else
                        (position.y - y) / abs(position.x - x) * speed
                } else {
                    moveY = if (position.y - y == 0F)
                        0F
                    else
                        (position.y - y) / abs(position.y - y) * speed

                    moveX = if (position.y - y == 0F)
                        if (position.x - x == 0F)
                            0F
                        else
                            (position.x - x) / abs(position.x - x) * speed
                    else
                        (position.x - x) / abs(position.y - y) * speed
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
                        if (moveX > 0 &&
                            position.x + moveX in (predator1.position.x - size)..(predator1.position.x + size) &&
                            position.y + moveY in (predator1.position.y - size)..(predator1.position.y + size) ||
                            (position.x + moveX == predator1.position.x && position.y + moveY == predator1.position.y)
                        )
                            isPredatorCollisionFound = true

                    var isHerbivoreCollisionFound = false
                    for (herbivore in herbivores)
                        if ((position.x + moveX in (herbivore.position.x - size)..(herbivore.position.x + size) &&
                                    position.y + moveY in (herbivore.position.y - size)..(herbivore.position.y + size)) ||
                            (position.x + moveX == herbivore.position.x && position.y + moveY == herbivore.position.y)
                        )
                            isHerbivoreCollisionFound = true


                    if (!isPredatorCollisionFound && !isHerbivoreCollisionFound) {
                        if (moveX > 0)
                            orientation = atan(moveY / moveX)
                        if (moveX < 0)
                            orientation = PI.toFloat() + atan(moveY / moveX)
                        if (moveX == 0F)
                            orientation = PI.toFloat() / 2 * sign(moveY)

                        this.moveX = moveX
                        this.moveY = moveY

                        position = Point(position.x + moveX, position.y + moveY)
                        isMoved = true

                        for (plant in plants) {
                            if ((position.x in (plant.position.x - size)..(plant.position.x + size) &&
                                        position.y in (plant.position.y - size)..(plant.position.y + size)) ||
                                (position.x == plant.position.x && position.y == plant.position.y)
                            ) {
                                currentPoints += plant.pointsForEating
                                return ReturnData(isMoved, plants.indexOf(plant))
                            }
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