package com.makentoshe.androidgithubcitemplate
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View

class ArrayDrawer (
    context : Context,
    attrs : AttributeSet? = null,
    defaultAttrs : Int = 0
) : View(context, attrs, defaultAttrs){

    var startX : Float = 0f
    var startY : Float = 0f

    var arrayWidth : Float = 1f

    var array: Array <Array <Int>> = arrayOf(arrayOf())


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
                var rectWidth: Float = width * arrayWidth / array.size
                var rectHeight: Float = rectWidth
                for (i in (0..(array.size - 1)))
                    for (k in (0..array[i].size - 1)) {
                        if (array[i][k] == 1)
                            painter.setColor(Color.WHITE)
                        else
                            painter.setColor(Color.BLUE)
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