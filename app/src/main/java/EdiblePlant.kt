/* Class EdiblePlant
 * Хранит координаты съедобного растения на поле
 */
class EdiblePlant(val position: Point) {
    constructor(X: Int, Y: Int): this(Point(X, Y))
}