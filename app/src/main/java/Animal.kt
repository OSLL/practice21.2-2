import java.lang.Math.random

class Point(val posX: Int, val posY: Int) {
}

/* Class Animal
 * position - координаты животного (количество клеток от левого верхнего угла)
 * fieldOfView - область видмости (количество клеток от животного во все стороны (в т.ч. по диагонали)
 * speed - скорость животного (количество клеток, которое может пройти животное за один ход)
 */
abstract class Animal(var position: Point, val fieldOfView: Int, val speed: Int) {
    // Функция, задающая координаты животного
    protected fun setPosition(X: Int, Y: Int) {
        position = Point(X, Y)
    }
    abstract fun move(animals: MutableList<Animal>): Boolean // Функция, реализующая поведение для животного
}