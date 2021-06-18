/* Class Predator (хищник)
 * position - координаты животного (количество клеток от левого верхнего угла)
 * fieldOfView - область видмости (количество клеток от животного во все стороны (в т.ч. по диагонали)
 * speed - скорость животного (количество клеток, которое может пройти животное за один ход)
 */
class Predator(position: Point, fieldOfView: Int, speed: Int): Animal(position, fieldOfView, speed) {
    val isItPredator: Boolean = true             // Для отрисовки
    /* Функция, реализующая поведение для животного (куда животное пойдет на следующем ходу)
     * Возвращает состояние животного (умерло - false/живо - true)
     */
    override fun move(animals: MutableList<Animal>): Boolean {
        return true
    }
}