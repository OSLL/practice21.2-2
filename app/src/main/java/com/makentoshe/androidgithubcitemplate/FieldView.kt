package com.makentoshe.androidgithubcitemplate
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class FieldView (
    context : Context,
    attrs : AttributeSet? = null,
    defaultAttrs : Int = 0
) : View(context, attrs, defaultAttrs){

    var startX : Float = 0f
    var startY : Float = 0f

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
        pixelWidth = amount;
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
        strokeWidth = 30f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            val rectWidth: Float = width * fieldWidth / pixelWidth.toFloat()
            val rectHeight: Float = rectWidth

            painter.color = Color.GREEN
            for (plant in plantsList) {
                drawRect(
                    startX * width + rectWidth * plant.position.x,
                    startY * height + rectHeight * plant.position.y,
                    startX * width + rectWidth * (plant.position.x + 1),
                    startY * height + rectHeight * (plant.position.y + 1),
                    painter
                )
            }

            painter.color = Color.BLACK
            for(herbivore in herbivoresList){
                drawRect(
                    startX * width + rectWidth * herbivore.position.x,
                    startY * height + rectHeight * herbivore.position.y,
                    startX * width + rectWidth * (herbivore.position.x + 1),
                    startY * height + rectHeight * (herbivore.position.y + 1),
                    painter
                )
            }

            painter.color = Color.RED
            for(predator in predatorsList){
                drawRect(
                    startX * width + rectWidth * predator.position.x,
                    startY * height + rectHeight * predator.position.y,
                    startX * width + rectWidth * (predator.position.x + 1),
                    startY * height + rectHeight * (predator.position.y + 1),
                    painter
                )
            }
        }
    }
}
