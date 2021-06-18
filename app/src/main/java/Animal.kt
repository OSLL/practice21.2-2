class Point(val posX: Int, val posY: Int)

/* Обозначения в массиве поля:
 * 0 - пустая клетка
 * 1 - клетка с растением
 * 2 - клетка с травоядным
 * 3 - клетка с хищником
 */

/* Class Animal */
abstract class Animal(
    protected var position: Point, // Координаты животного (количество клеток от левого верхнего угла)
    val fieldOfView: Int, // Область видмости (количество клеток от животного во все стороны
    val speed: Int, // Скорость животного (количество клеток, которое может пройти животное за один ход)
    var amountFoodForBreeding: Float // Количество еды, необхожимое для размножения
    ) {
    var currentAmountOfFood = 0 // Текущее количество еды
    // Функция, задающая координаты животного
    protected fun setPosition(X: Int, Y: Int) {
        position = Point(X, Y)
    }
    abstract fun move(field: ArrayList<ArrayList<Int>>): Point // Функция, реализующая поведение для животного
}