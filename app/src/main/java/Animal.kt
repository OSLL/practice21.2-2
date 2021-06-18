import java.lang.Math.random

class Point(val posX: Int, val posY: Int) {
}

abstract class Animal(var position: Point, val fieldOfView: Int, val speed: Int) {
    fun setPosition(X: Int, Y: Int) {
        position = Point(X, Y)
    }
    abstract fun move()
}