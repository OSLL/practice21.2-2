package com.makentoshe.androidgithubcitemplate

/* Класс точки
 * Нужен для хранения координат
 */
class Point(val X: Float, val Y: Float)

/* Класс животного, рассчитаный на векторную графику */
abstract class AnimalV(var position: Point,          // Положение животного относительно левого верхнего угла поля
                       fieldOfView: Float,           // Область, в которой животное видит объекты (размер поля - 100)
                       val speed: Float,             // Скорость, с которой двигается животное (единицы в секунду) (размер поля - 100)
                       var size: Float,              // Размеры животного относительно базовой модельки
                       var orientation: Float,       // Угол поворота животного относительно горизонтальной оси
                       val pointsForBreeding: Float, // Количество очков. необходимых для размножения
                       val afraidOfPredator: Boolean // (Только для травоядных) приоритет ходьбы (идти к растению или бежать от животного)
                       ) {
    abstract fun isPredator(): Boolean                                                   // Функция для отличия травоядного от хищника в списках
    abstract fun move(animals: MutableList<AnimalV>, plants: MutableList<PlantV>): Point // Основная функция. Меняет координаты животного на основе координат других объектов и возвращает координату съеденного объекта или (-1, -1)
}