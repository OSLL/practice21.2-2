import kotlin.random.Random
import kotlin.random.nextInt

class Point(val posX: Int, val posY: Int)

/* Обозначения в массиве поля:
 * 0 - пустая клетка
 * 1 - клетка с растением
 * 2 - клетка с травоядным
 * 3 - клетка с хищником
 */
/* Class Herbivore (травоядное)
 * position - координаты животного (количество клеток от левого верхнего угла)
 * fieldOfView - область видмости (количество клеток от животного во все стороны (в т.ч. по диагонали)
 * speed - скорость животного (количество клеток, которое может пройти животное за один ход)
 */
class Herbivore(var position: Point, val fieldOfView: Int, val speed: Int){
    var currentAmountOfFood = 0
    val amountFoodForBreeding = 1
    /* Функция, реализующая поведение для животного (куда животное пойдет на следующем ходу)
     * Возвращает координату уничтоженного растения или (-1, -1), если этого не произошло
     */
    fun move(herbivores: MutableList<Herbivore>,
             predators: MutableList<Predator>,
             plants: MutableList<EdiblePlant>): Point {
        var isMoved = false

        for (plant in plants)
            if (!isMoved) {

                val x = plant.position.posX
                val y = plant.position.posY

                val moveY: Int = if (y - position.posY == 0)
                    0
                else
                    (y - position.posY) / kotlin.math.abs(y - position.posY) * speed
                val moveX: Int = if (x - position.posX == 0)
                    0
                else
                    (x - position.posX) / kotlin.math.abs(x - position.posX) * speed

                if (kotlin.math.abs(x - position.posX) < fieldOfView &&
                    kotlin.math.abs(y - position.posY) < fieldOfView &&
                    position.posX + moveX < 100 &&
                    position.posX + moveX >= 0 &&
                    position.posY + moveY < 100 &&
                    position.posY + moveY >= 0
                ) {

                    var isPredatorFound = false
                    for (predator in predators)
                        if (predator.position.posX == position.posX + moveX &&
                            predator.position.posY == position.posY + moveY)
                            isPredatorFound = true

                    var isHerbivoreFound = false
                    for (herbivore in herbivores)
                        if (herbivore.position.posX == position.posX + moveX &&
                            herbivore.position.posY == position.posY + moveY)
                            isHerbivoreFound = true

                    if (!isPredatorFound && !isHerbivoreFound) {
                        position = Point(position.posX + moveX, position.posY + moveY)
                        isMoved = true
                    }

                    for (plant1 in plants)
                        if (plant1.position.posX == position.posX &&
                            plant1.position.posY == position.posY
                        ) {
                            currentAmountOfFood++
                            return plant1.position
                        }
                    break
                }
            }

        for (predator in predators)
            if (!isMoved) {
                val x = predator.position.posX
                val y = predator.position.posY

                val moveY: Int = if (y - position.posY == 0)
                    0
                else
                    (y - position.posY) / kotlin.math.abs(y - position.posY) * speed
                val moveX: Int = if (x - position.posX == 0)
                    0
                else
                    (x - position.posX) / kotlin.math.abs(x - position.posX) * speed

                if (kotlin.math.abs(x - position.posX) < fieldOfView &&
                    kotlin.math.abs(y - position.posY) < fieldOfView &&
                    position.posX - moveX < 100 &&
                    position.posX - moveX >= 0 &&
                    position.posY - moveY < 100 &&
                    position.posY - moveY >= 0
                ) {

                    var isPredatorFound = false
                    for (predator1 in predators)
                        if (predator1.position.posX == position.posX - moveX &&
                            predator1.position.posY == position.posY - moveY)
                            isPredatorFound = true

                    var isHerbivoreFound = false
                    for (herbivore in herbivores)
                        if (herbivore.position.posX == position.posX - moveX &&
                            herbivore.position.posY == position.posY - moveY)
                            isHerbivoreFound = true

                    if (!isPredatorFound && !isHerbivoreFound) {
                        position = Point(position.posX - moveX, position.posY - moveY)
                        isMoved = true
                    }
                    isMoved = true

                    for (plant in plants)
                        if (plant.position.posX == position.posX &&
                            plant.position.posY == position.posY
                        ) {
                            currentAmountOfFood++
                            return plant.position
                        }
                    break

                }
            }

        if (!isMoved) // Если нету растений или хищников в поле зрения
        {
            val dx = Random.nextInt(-1..1)
            val dy = Random.nextInt(-1..1)
            if (position.posX + dx * speed < 100 &&
                position.posX + dx * speed >= 0 &&
                position.posY + dy * speed < 100 &&
                position.posY + dy * speed >= 0) {

                position = Point(
                    position.posX + dx * speed,
                    position.posY + dy * speed
                )
            }
        }
        return Point(-1, -1)
    }
}