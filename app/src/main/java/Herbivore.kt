class Herbivore(position: Point, fieldOfView: Int, speed: Int): Animal(position, fieldOfView, speed) {
    val isItPredator: Boolean = false
    override fun move() {
    }
}