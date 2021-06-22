package com.makentoshe.androidgithubcitemplate

/* Класс точки
 * Нужен для хранения координат
 */
class Point(val x: Float, val y: Float)

class PlantV(val pos: Point,        // Положение растения относительно левого верхнего угла
             val size: Float,            // Размер растения (Размер поля - 100)
             basePointsForEating: Float) // Кол-во очков, дающееся за съедение этого растения
{
    val pointsForEating = basePointsForEating * size
}