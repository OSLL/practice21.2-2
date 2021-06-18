class Predator(position: Point, fieldOfView: Int, speed: Int): Animal(position, fieldOfView, speed) {
    val isItPredator: Boolean = true
    override fun move() {
    }
}