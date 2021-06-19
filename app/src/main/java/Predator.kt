import kotlin.random.Random
import kotlin.random.nextInt

/* Class Predator (хищник)
 * position - координаты животного (количество клеток от левого верхнего угла)
 * fieldOfView - область видмости (количество клеток от животного во все стороны (в т.ч. по диагонали)
 * speed - скорость животного (количество клеток, которое может пройти животное за один ход)
 */
class Predator(var position: Point, val fieldOfView: Int, val speed: Int) {
    var currentAmountOfFood = 0
    val amountFoodForBreeding = 2
    /* Функция, реализующая поведение для животного (куда животное пойдет на следующем ходу)
     * Возвращает координату съеденного травоядного или (-1, -1), если этого не произошло
     */
    fun move(herbivores: MutableList<Herbivore>,
             predators: MutableList<Predator>,
             plants: MutableList<EdiblePlant>): Point {
        var isMoved = false

        for (herbivore in herbivores)
            if (!isMoved) {

                val x = herbivore.position.posX
                val y = herbivore.position.posY

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
                            predator.position.posY == position.posY + moveY
                        )
                            isPredatorFound = true

                    var isPlantFound = false
                    for (plant in plants)
                        if (plant.position.posX == position.posX + moveX &&
                            plant.position.posY == position.posY + moveY)
                            isPlantFound = true

                    if (!isPredatorFound && !isPlantFound) {
                        position = Point(position.posX + moveX, position.posY + moveY)
                        isMoved = true
                    }

                    for (herbivore1 in herbivores)
                        if (herbivore1.position.posX == position.posX &&
                            herbivore1.position.posY == position.posY) {
                            currentAmountOfFood++
                            return herbivore1.position
                        }
                    break
                }
            }

        if (!isMoved) // Если нету травоядных в поле зрения
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