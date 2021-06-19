package com.makentoshe.androidgithubcitemplate

class HerbivoreV(position: Point,
                 fieldOfView: Float,
                 speed: Float, size: Float,
                 orientation: Float,
                 pointsForBreeding: Float,
                 afraidOfPredator: Boolean
                 ) : AnimalV(position, fieldOfView, speed, size, orientation, pointsForBreeding, afraidOfPredator) {
    override fun isPredator(): Boolean {
        return false
    }

    override fun move(animals: MutableList<AnimalV>, // Список всех животных
                      plants: MutableList<PlantV> // Список всех растений
    ): Point {
        TODO("Not yet implemented")
    }
}