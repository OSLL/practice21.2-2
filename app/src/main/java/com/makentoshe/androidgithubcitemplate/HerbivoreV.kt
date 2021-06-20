package com.makentoshe.androidgithubcitemplate

import kotlin.math.abs
import kotlin.math.atan

class HerbivoreV(
    var position: Point,          // Положение животного относительно левого верхнего угла поля
    private val fieldOfView: Float,           // Область, в которой животное видит объекты (размер поля - 100)
    private val speed: Float,             // Скорость, с которой двигается животное (единицы в секунду) (размер поля - 100)
    var size: Float,              // Размеры животного относительно базовой модельки
    var orientation: Float,       // Угол поворота животного относительно горизонтальной оси
    val pointsForBreeding: Float, // Количество очков. необходимых для размножения
    val afraidOfPredator: Boolean // (Только для травоядных) приоритет ходьбы (идти к растению или бежать от животного)
) {
    var currentPoints = 0F // Текущие очки

    /* Основная функция класса, отвечающая за поведение
     * Возвращает координату съеденного объекта или (-1, -1), если никто не был съеден
     */
    fun move(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>          // Список всех растений
    ): Point {
        var isMoved = false

        for (plant in plants)
            if (!isMoved) {
                val x = plant.position.x
                val y = plant.position.y
                val moveX: Float
                val moveY: Float

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

                if (abs(x - position.x) < fieldOfView &&
                    abs(y - position.y) < fieldOfView &&
                    position.x + moveX < 100 &&
                    position.x + moveX >= 0 &&
                    position.y + moveY < 100 &&
                    position.y + moveY >= 0
                ) {

                    var isPredatorCollisionFound = false
                    for (predator in predators) {
                        var isXCollided = false
                        var isYCollided = false
                        if (moveX > 0 &&
                            position.x + size + moveX > predator.position.x - predator.size
                        )
                            isXCollided = true
                        if (moveX < 0 &&
                            position.x - size + moveX < predator.position.x + predator.size
                        )
                            isXCollided = true

                        if (moveY > 0 &&
                            position.y + size + moveY > predator.position.y - predator.size
                        )
                            isYCollided = true
                        if (moveY < 0 &&
                            position.y - size + moveY < predator.position.y + predator.size
                        )
                            isYCollided = true

                        if (isXCollided && isYCollided)
                            isPredatorCollisionFound = true
                    }

                    var isHerbivoreCollisionFound = false
                    for (herbivore in herbivores) {
                        var isXCollided = false
                        var isYCollided = false
                        if (moveX > 0 &&
                            position.x + size + moveX > herbivore.position.x - herbivore.size
                        )
                            isXCollided = true
                        if (moveX < 0 &&
                            position.x - size + moveX < herbivore.position.x + herbivore.size
                        )
                            isXCollided = true

                        if (moveY > 0 &&
                            position.y + size + moveY > herbivore.position.y - herbivore.size
                        )
                            isYCollided = true
                        if (moveY < 0 &&
                            position.y - size + moveY < herbivore.position.y + herbivore.size
                        )
                            isYCollided = true

                        if (isXCollided && isYCollided)
                            isHerbivoreCollisionFound = true
                    }

                    if (!isPredatorCollisionFound && !isHerbivoreCollisionFound) {
                        orientation = atan(moveY / moveX)
                        position = Point(position.x + moveX, position.y + moveY)
                        isMoved = true

                        for (plant1 in plants) {
                            var isXCollided = false
                            var isYCollided = false
                            if (moveX > 0 &&
                                position.x + size + moveX > plant1.position.x - plant1.size
                            )
                                isXCollided = true
                            if (moveX < 0 &&
                                position.x - size + moveX < plant1.position.x + plant1.size
                            )
                                isXCollided = true

                            if (moveY > 0 &&
                                position.y + size + moveY > plant1.position.y - plant1.size
                            )
                                isYCollided = true
                            if (moveY < 0 &&
                                position.y - size + moveY < plant1.position.y + plant1.size
                            )
                                isYCollided = true


                            if (isXCollided && isYCollided) {
                                currentPoints += plant1.pointsForEating
                                return plant1.position
                            }
                        }
                    }
                    break
                }
            }


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
                    for (predator1 in predators) {
                        var isXCollided = false
                        var isYCollided = false
                        if (moveX > 0 &&
                            position.x + size + moveX > predator1.position.x - predator1.size
                        )
                            isXCollided = true
                        if (moveX < 0 &&
                            position.x - size + moveX < predator1.position.x + predator1.size
                        )
                            isXCollided = true

                        if (moveY > 0 &&
                            position.y + size + moveY > predator1.position.y - predator1.size
                        )
                            isYCollided = true
                        if (moveY < 0 &&
                            position.y - size + moveY < predator1.position.y + predator1.size
                        )
                            isYCollided = true

                        if (isXCollided && isYCollided)
                            isPredatorCollisionFound = true
                    }

                    var isHerbivoreCollisionFound = false
                    for (herbivore in herbivores) {
                        var isXCollided = false
                        var isYCollided = false
                        if (moveX > 0 &&
                            position.x + size + moveX > herbivore.position.x - herbivore.size
                        )
                            isXCollided = true
                        if (moveX < 0 &&
                            position.x - size + moveX < herbivore.position.x + herbivore.size
                        )
                            isXCollided = true

                        if (moveY > 0 &&
                            position.y + size + moveY > herbivore.position.y - herbivore.size
                        )
                            isYCollided = true
                        if (moveY < 0 &&
                            position.y - size + moveY < herbivore.position.y + herbivore.size
                        )
                            isYCollided = true

                        if (isXCollided && isYCollided)
                            isHerbivoreCollisionFound = true
                    }

                    if (!isPredatorCollisionFound && !isHerbivoreCollisionFound) {
                        orientation = atan(moveY / moveX)
                        position = Point(position.x + moveX, position.y + moveY)
                        isMoved = true

                        for (plant in plants) {
                            var isXCollided = false
                            var isYCollided = false
                            if (moveX > 0 &&
                                position.x + size + moveX > plant.position.x - plant.size
                            )
                                isXCollided = true
                            if (moveX < 0 &&
                                position.x - size + moveX < plant.position.x + plant.size
                            )
                                isXCollided = true

                            if (moveY > 0 &&
                                position.y + size + moveY > plant.position.y - plant.size
                            )
                                isYCollided = true
                            if (moveY < 0 &&
                                position.y - size + moveY < plant.position.y + plant.size
                            )
                                isYCollided = true

                            if (isXCollided && isYCollided) {
                                currentPoints += plant.pointsForEating
                                return plant.position
                            }
                        }
                    }
                    break
                }
            }

        if (!isMoved) // Если нету растений или хищников в поле зрения
        {
            val dx = (-1..1).random()
            val dy = (-1..1).random()

            if (position.x + dx * speed < 100 &&
                position.x + dx * speed >= 0 &&
                position.y + dy * speed < 100 &&
                position.y + dy * speed >= 0
            ) {

                position = Point(
                    position.x + dx * speed,
                    position.y + dy * speed
                )
            }
        }
        return Point(-1F, -1F)
    }
}