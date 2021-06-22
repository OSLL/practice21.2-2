package com.makentoshe.androidgithubcitemplate
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ArrayDrawer (
    context : Context,
    attrs : AttributeSet? = null,
    defaultAttrs : Int = 0
) : View(context, attrs, defaultAttrs){

    private var startX : Float = 0f
    private var startY : Float = 0f

    private var arrayWidth : Float = 1f

    private var array: Array <Array <Int>> = arrayOf(arrayOf())


    fun setSize(newWidth : Float){
        arrayWidth = newWidth
    }

    fun setPosition(newX : Float, newY : Float){
        startX = newX
        startY = newY
    }

    fun setArrayToDraw (newArray : Array <Array <Int>>){
        array = newArray
    }

    private val painter = Paint().apply{
        color = Color.YELLOW
        style = Paint.Style.FILL
        isAntiAlias = true
        strokeWidth = 30f
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            val rectWidth: Float = width * arrayWidth / array.size
            val rectHeight: Float = rectWidth
            for (i in array.indices)
                for (k in array[i].indices) {
                    if (array[i][k] == 0)
                        painter.color = Color.WHITE
                    if (array[i][k] == 1)
                        painter.color = Color.GREEN
                    if (array[i][k] == 2)
                        painter.color = Color.BLACK
                    if (array[i][k] == 3)
                        painter.color = Color.RED
                    drawRect(
                        startX * width + rectWidth * i,
                        startY * height + rectHeight * k,
                        startX * width + rectWidth * (i + 1) + 1f,
                        startY * height + rectHeight * (k + 1) + 1f,
                        painter
                    )
                }
        }
    }
}