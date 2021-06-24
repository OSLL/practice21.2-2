package com.makentoshe.androidgithubcitemplate

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View

class FieldView(
    context: Context,
    attrs: AttributeSet? = null,
    defaultAttrs: Int = 0
) : View(context, attrs, defaultAttrs) {

    var startX: Float = 0f
    var startY: Float = 0f

    var startXReal = startX
    var startYReal = startY

    private var zoom = 1f
    private val scrollSpeed = 0.05f

    private var fieldSizeX: Float = 1f
    private var fieldSizeY: Float = 1f

    private var predatorsList = mutableListOf<PredatorV>()
    private var herbivoresList = mutableListOf<HerbivoreV>()
    private var plantsList = mutableListOf<PlantV>()

    fun setSize(size: Float) {
        fieldSizeX = size
    }

    fun setZoom(newZoom : Float){
        var zoomChange = newZoom / zoom
        zoom = newZoom

        var distanceX = (startX + fieldSizeX / 2) - startXReal
        distanceX *= zoomChange
        startXReal = fieldSizeX / 2 + startX - distanceX
        var distanceY = (startY + fieldSizeY / 2) - startYReal
        distanceY *= zoomChange
        startYReal = fieldSizeY / 2 + startY - distanceY

        callibrate()
    }

    fun setPosition(newX: Float, newY: Float) {
        startX = newX
        startY = newY
        startXReal = startX
        startYReal = startY
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
        strokeWidth = 2f
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                var changeX = fieldSizeX / 2 - (event.x / width - startX)
                var changeY = fieldSizeY / 2 - (event.y / height - startY)
                val change = kotlin.math.sqrt(changeX * changeX + changeY * changeY)
                val k = change / scrollSpeed
                changeX /= k
                changeY /= k

                startXReal += changeX
                startYReal += changeY
                callibrate()
            }
        }
        return super.onTouchEvent(event)
    }


    fun callibrate(){
        if (startXReal + fieldSizeX * zoom < startX + fieldSizeX)
            startXReal =  startX + fieldSizeX - fieldSizeX * zoom
        if (startXReal > startX)
            startXReal = startX

        if (startYReal + fieldSizeY * zoom < startY + fieldSizeY)
            startYReal =  startY + fieldSizeY - fieldSizeY * zoom
        if (startYReal > startY)
            startYReal = startY
    }

    fun isInField(x : Float, y : Float) : Boolean{
        var xIn1 = x / fieldData.fieldSizeW
        var yIn1 = y / fieldData.fieldSizeH

        var xOnField = xIn1 * fieldSizeX * zoom + startXReal
        var yOnField = yIn1 * fieldSizeY * zoom + startYReal

        if (xOnField < startX || xOnField > startX + fieldSizeX)
            return false
        if (yOnField < startY || yOnField > startY + fieldSizeY)
            return false

        return true
    }

    val shPr = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

    override fun onDraw(canvas: Canvas) {
        fieldSizeY = fieldSizeX * width / height * fieldData.fieldSizeH / fieldData.fieldSizeW
        canvas.apply {
            val rectWidth: Float = width * fieldSizeX / fieldData.fieldSizeW.toFloat() * zoom
            val matrix = Matrix()

            painter.style = Paint.Style.STROKE
            drawRect(
                startX * width,
                startY * height,
                startX * width + fieldSizeX * width,
                startY * height + fieldSizeY * height,
                painter
            )
            painter.color = Color.rgb(162, 195, 232)
            drawGridAt(
                startX * width,
                startY * height,
                startX * width + fieldSizeX * width,
                startY * height + fieldSizeY * height,
                startXReal * width,
                startYReal * height,
                fieldSizeX * width / 10 * zoom,
                canvas
            )
            painter.style = Paint.Style.FILL

            painter.color = shPr.getInt("PlColor", Color.GREEN)
            for (plant in plantsList) {
                if (isInField(plant.pos.x, plant.pos.y)) {
                    drawRect(
                        startXReal * width + rectWidth * (plant.pos.x - plant.size),
                        startYReal * height + rectWidth * (plant.pos.y - plant.size),
                        startXReal * width + rectWidth * (plant.pos.x + plant.size),
                        startYReal * height + rectWidth * (plant.pos.y + plant.size),
                        painter
                    )
                }
            }

            painter.color = shPr.getInt("HeColor", Color.GREEN)
            for (herbivore in herbivoresList) {
                if (isInField(herbivore.pos.x, herbivore.pos.y)) {
                    matrix.reset()
                    matrix.preTranslate(
                        startXReal * width + rectWidth * herbivore.pos.x,
                        startYReal * height + rectWidth * herbivore.pos.y
                    )
                    matrix.preRotate(herbivore.orientation / 3.14159f * 180f + 90f)
                    drawAnimal(canvas, herbivore.size / 2, matrix)
                }
            }
            painter.color = shPr.getInt("PrColor", Color.RED)

            for (predator in predatorsList) {
                if (isInField(predator.pos.x, predator.pos.y)) {
                    matrix.reset()
                    matrix.preTranslate(
                        startXReal * width + rectWidth * predator.pos.x,
                        startYReal * height + rectWidth * predator.pos.y
                    )
                    matrix.preRotate(predator.orientation / 3.14159f * 180f + 90f)
                    drawAnimal(canvas, predator.size / 2, matrix)
                }
            }

        }
    }

    private fun drawAnimal(canvas: Canvas, size: Float, matrix: Matrix) {

        canvas.apply {
            matrix.preScale(1f / 100f, 1f / 100f)
            matrix.preScale(
                size / fieldData.fieldSizeW.toFloat() * fieldSizeX * width * zoom,
                size / fieldData.fieldSizeW.toFloat() * fieldSizeX * width * zoom
            )

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
        }
    }

    private fun drawGridAt(
        x1: Float,
        y1: Float,
        x2: Float,
        y2: Float,
        startX: Float,
        startY: Float,
        size: Float,
        canvas: Canvas
    ) {
        canvas.apply {
            var xc = startX
            var yc = startY
            while (xc < x2) {
                if(xc > x1)
                    drawLine(xc, y1, xc, y2, painter)
                xc += size
            }
            while (yc < y2) {
                if(yc > y1)
                    drawLine(x1, yc, x2, yc, painter)
                yc += size
            }
        }
    }
}
