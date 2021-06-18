import kotlin.random.Random
import kotlin.random.nextInt

/* Class Predator (хищник)
 * position - координаты животного (количество клеток от левого верхнего угла)
 * fieldOfView - область видмости (количество клеток от животного во все стороны (в т.ч. по диагонали)
 * speed - скорость животного (количество клеток, которое может пройти животное за один ход)
 */
class Predator(position: Point, fieldOfView: Int, speed: Int): Animal(position, fieldOfView, speed, 2F) {
    override fun isPredator(): Boolean {
        return true
    }           // Для отрисовки

    /* Функция, реализующая поведение для животного (куда животное пойдет на следующем ходу)
     * Возвращает координату съеденного травоядного или (-1, -1), если этого не произошло
     */
    override fun move(field: Array<Array<Int>>): Point {
        var isMoved = false

        for (i in field.indices) {
            if (!isMoved) {
                for (j in field.indices) {
                    if (Point(j, i) != position) {

                        val moveY: Int = if (i - position.posY == 0)
                            0
                        else
                            (i - position.posY) / kotlin.math.abs(i - position.posY) * speed
                        val moveX: Int = if (j - position.posX == 0)
                            0
                        else
                            (j - position.posX) / kotlin.math.abs(j - position.posX) * speed

                        if (field[i][j] == 2) { // Если на координате травоядное
                            if (kotlin.math.abs(i - position.posY) <= fieldOfView && kotlin.math.abs(j - position.posX) <= fieldOfView &&
                                position.posX + moveX < field.size &&
                                position.posX + moveX >= 0 &&
                                position.posY + moveY < field.size &&
                                position.posY + moveY >= 0 &&
                                field[position.posY + moveY][position.posX + moveX] == 0 ||
                                field[position.posY + moveY][position.posX + moveX] == 2) {
                                // Если травоядное находится в поле видимости и при этом нет никаких преград, чтобы добраться до него
                                position = Point(
                                    position.posX + moveX,
                                    position.posY + moveY
                                )
                                isMoved = true
                                if (field[position.posY][position.posX] == 2) { // Если хищник на клетке с травоядным
                                    currentAmountOfFood++
                                    return Point(position.posX, position.posY)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!isMoved) // Если нету травоядных в поле зрения
        {
            val dx = Random.nextInt(-1..1)
            val dy = Random.nextInt(-1..1)
            if (position.posX + dx * speed < field[0].size &&
                position.posX + dx * speed >= 0 &&
                position.posY + dy * speed < field[0].size &&
                position.posY + dy * speed > 0) {

                position = Point(position.posX + dx * speed,
                    position.posY + dy * speed)
            }
        }
        return Point(-1, -1)
    }
}