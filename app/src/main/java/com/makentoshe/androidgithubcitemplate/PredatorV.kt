package com.makentoshe.androidgithubcitemplate

class PredatorV(position: Point,
                fieldOfView: Float,
                speed: Float,
                size: Float,
                orientation: Float,
                pointsForBreeding: Float
                ) : AnimalV(position, fieldOfView, speed, size, orientation, pointsForBreeding, false) {
    override fun isPredator(): Boolean {
        return true
    }

    override fun move(animals: MutableList<AnimalV>, // Список всех животных
                      plants: MutableList<PlantV> // Список всех растений
                      ): Point {
        TODO("Not yet implemented")
    }
}