package com.makentoshe.androidgithubcitemplate

/* Класс точки
 * Нужен для хранения координат
 */
class Point(val x: Float, val y: Float)

class PlantV(val pos: Point,        // Положение растения относительно левого верхнего угла
             var size: Float,            // Размер растения (Размер поля - 100)
             private val basePointsForEating: Float) // Кол-во очков, дающееся за съедение этого растения
{
    var pointsForEating = basePointsForEating * size

    fun recalculatePoints() {
        pointsForEating = basePointsForEating * size
    }
}