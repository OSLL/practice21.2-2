package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ArrayDrawer(
    context: Context,
    attrs: AttributeSet? = null,
    defaultAttrs: Int = 0
) : View(context, attrs, defaultAttrs) {

    private var startX: Float = 0f
    private var startY: Float = 0f

    private var arrayWidth: Float = 1f


    var predatorsList = mutableListOf<PredatorV>()
    var herbivoresList = mutableListOf<HerbivoreV>()
    var plantsList = mutableListOf<PlantV>()


    fun setSize(newWidth: Float) {
        arrayWidth = newWidth
    }

    fun setPosition(newX: Float, newY: Float) {
        startX = newX
        startY = newY
    }

    fun setListsToDraw(
        predators: MutableList<PredatorV>,
        herbivores: MutableList<HerbivoreV>,
        plants: MutableList<PlantV>
    ) {
        predatorsList = predators
        herbivoresList = herbivores
        plantsList = plants
    }

    private val painter = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
        isAntiAlias = true
        strokeWidth = 30f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            val rectWidth: Float = width * arrayWidth / 100f
            val rectHeight: Float = rectWidth

            painter.color = Color.GREEN
            for (plant in plantsList) {
                drawRect(
                    startX * width + rectWidth * (plant.position.x - plant.size),
                    startY * height + rectHeight * (plant.position.y - plant.size),
                    startX * width + rectWidth * (plant.position.x + plant.size),
                    startY * height + rectHeight * (plant.position.y + plant.size),
                    painter
                )
            }

            painter.color = Color.BLACK
            for (herbivore in herbivoresList) {
                drawRect(
                    startX * width + rectWidth * (herbivore.position.x - herbivore.size),
                    startY * height + rectHeight * (herbivore.position.y - herbivore.size),
                    startX * width + rectWidth * (herbivore.position.x + herbivore.size),
                    startY * height + rectHeight * (herbivore.position.y + herbivore.size),
                    painter
                )
            }

            painter.color = Color.RED
            for (predator in predatorsList) {
                drawRect(
                    startX * width + rectWidth * (predator.position.x - predator.size),
                    startY * height + rectHeight * (predator.position.y - predator.size),
                    startX * width + rectWidth * (predator.position.x + predator.size),
                    startY * height + rectHeight * (predator.position.y + predator.size),
                    painter
                )
            }
        }
    }
}
