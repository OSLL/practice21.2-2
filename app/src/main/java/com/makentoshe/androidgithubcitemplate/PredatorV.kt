package com.makentoshe.androidgithubcitemplate

import kotlin.math.abs
import kotlin.math.atan

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

    /* Основная функция класса, отвечающая за поведение
     * Возвращает координату съеденного объекта или (-1, -1), если никто не был съеден
     */
    fun move(
        herbivores: MutableList<HerbivoreV>, // Список всех травоядных
        predators: MutableList<PredatorV>,   // Список всех хищников
        plants: MutableList<PlantV>          // Список всех растений
    ): Point {
        var isMoved = false

        for (herbivore in herbivores)
            if (!isMoved) {
                val x = herbivore.position.x
                val y = herbivore.position.y
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

                    var isPlantCollisionFound = false
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

                        if (isXCollided && isYCollided)
                            isPlantCollisionFound = true
                    }

                    if (!isPredatorCollisionFound && !isPlantCollisionFound) {
                        orientation = atan(moveY / moveX)
                        position = Point(position.x + moveX, position.y + moveY)
                        isMoved = true

                        for (herbivore1 in herbivores) {
                            var isXCollided = false
                            var isYCollided = false
                            if (moveX > 0 &&
                                position.x + size + moveX > herbivore1.position.x - herbivore1.size
                            )
                                isXCollided = true
                            if (moveX < 0 &&
                                position.x - size + moveX < herbivore1.position.x + herbivore1.size
                            )
                                isXCollided = true

                            if (moveY > 0 &&
                                position.y + size + moveY > herbivore1.position.y - herbivore1.size
                            )
                                isYCollided = true
                            if (moveY < 0 &&
                                position.y - size + moveY < herbivore1.position.y + herbivore1.size
                            )
                                isYCollided = true

                            if (isXCollided && isYCollided) {
                                currentPoints += herbivore.size * const
                                return herbivore1.position
                            }
                        }
                    }
                    break
                }
            }

        if (!isMoved) // Если нету травоядных в поле зрения
        {
            val switch = (0..1).random()
            val moveX: Float
            val moveY: Float

            if (switch == 0) {
                moveX = speed
                moveY = (-1000..1000).random().toFloat() / 1000
            } else {
                moveY = speed
                moveX = (-1000..1000).random().toFloat() / 1000
            }

            var isPlantCollisionFound = false
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

                if (isXCollided && isYCollided)
                    isPlantCollisionFound = true
            }

            if (!isPlantCollisionFound &&
                position.x + moveX < 100 &&
                position.x + moveX >= 0 &&
                position.y + moveY < 100 &&
                position.y + moveY >= 0
            ) {
                orientation = atan(moveY / moveX)
                position = Point(
                    position.x + moveX,
                    position.y + moveY
                )
            }
        }
        return Point(-1F, -1F)
    }
}