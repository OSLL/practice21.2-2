package com.makentoshe.androidgithubcitemplate
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class FieldView (
    context : Context,
    attrs : AttributeSet? = null,
    defaultAttrs : Int = 0
) : View(context, attrs, defaultAttrs){

    private var startX : Float = 0f
    private var startY : Float = 0f

    private var fieldWidth : Float = 1f
    private var pixelWidth = 100


    var predatorsList = mutableListOf<PredatorV>()
    var herbivoresList = mutableListOf<HerbivoreV>()
    var plantsList = mutableListOf<PlantV>()


    fun setSize(newWidth : Float){
        fieldWidth = newWidth
    }

    fun setPosition(newX : Float, newY : Float){
        startX = newX
        startY = newY
    }

    fun setPixelWidth(amount : Int){
        pixelWidth = amount
    }

    fun setListsToDraw (
        predators : MutableList<PredatorV>,
        herbivores : MutableList<HerbivoreV>,
        plants : MutableList<PlantV>){
        predatorsList = predators
        herbivoresList = herbivores
        plantsList = plants
    }



    private val painter = Paint().apply{
        color = Color.YELLOW
        style = Paint.Style.FILL
        isAntiAlias = true
        strokeWidth = 2f
    }



    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            val rectWidth: Float = width * fieldWidth / pixelWidth.toFloat()
            val rectHeight: Float = rectWidth

            val matrix = Matrix()

            painter.color = Color.GREEN
            for (plant in plantsList) {
                drawRect(
                    startX * width + rectWidth * (plant.pos.x - plant.size),
                    startY * height + rectHeight * (plant.pos.y - plant.size),
                    startX * width + rectWidth * (plant.pos.x + plant.size),
                    startY * height + rectHeight * (plant.pos.y + plant.size),
                    painter
                )
            }

            painter.color = Color.BLACK
            for(herbivore in herbivoresList)
            {
                matrix.reset()
                matrix.preTranslate(startX * width + rectWidth * herbivore.pos.x,
                    startY * height + rectHeight * herbivore.pos.y)
                matrix.preRotate(herbivore.orientation / 3.14159f * 180f + 90f)
                drawAnimal(canvas, herbivore.size / 2, matrix)
            }
            painter.color = Color.RED
            for(predator in predatorsList){
                matrix.reset()
                matrix.preTranslate(startX * width + rectWidth * predator.pos.x,
                    startY * height + rectHeight * predator.pos.y)
                matrix.preRotate(predator.orientation / 3.14159f * 180f + 90f)
                drawAnimal(canvas, predator.size / 2, matrix)
            }

        }
    }

    private fun drawAnimal(canvas: Canvas, size : Float, matrix: Matrix){

        canvas.apply {
            matrix.preScale(1f / 100f, 1f / 100f)
            matrix.preScale(size / pixelWidth.toFloat() * fieldWidth * width, size / pixelWidth.toFloat() * fieldWidth * width)

            val path = Path()
            path.fillType = Path.FillType.EVEN_ODD
            path.moveTo(-173f, -100f)
            path.lineTo(0f, -300f)
            path.lineTo(173f, -100f)
            path.lineTo(-173f, -100f)
            path.transform(matrix)
            canvas.drawPath(path, painter)
            path.reset()
            path.addCircle(0f, 0f, 200f, Path.Direction.CW)
            path.transform(matrix)
            canvas.drawPath(path, painter)
            canvas.restore()
        }
    }
}
