package com.makentoshe.androidgithubcitemplate

import android.os.Handler
import android.os.Looper

class Field(
    fieldViewSet: FieldView
) {
    private var fieldView = fieldViewSet

    private var go = true
    var speed = 1f
    private var speeds = arrayOf(0.2f, 0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 4f, 6f)
    var speedIndex = 2

    private var tickLength = 50f

    var time = System.currentTimeMillis()

    fun stopProcess() {
        go = false
    }

    fun startProcess() {
        go = true
        for (herbivore in fieldData.herbivoresList)
            herbivore.time = System.currentTimeMillis()
        for (predator in fieldData.predatorsList)
            predator.time = System.currentTimeMillis()
        doFrame()
    }

    fun setTick(tick: Float) {
        tickLength = tick
    }


    fun speedDecrease() {
        if (speedIndex > 0)
            speedIndex--
        speed = speeds[speedIndex]
    }

    fun speedIncrease() {
        if (speedIndex < speeds.size - 1)
            speedIndex++
        speed = speeds[speedIndex]
    }

    fun doFrame() {
        Handler(Looper.getMainLooper()).postDelayed({
            fieldData.update(speed)

            fieldView.setListsToDraw(fieldData.predatorsList, fieldData.herbivoresList, fieldData.plantsList)
            fieldView.invalidate()
            if (go)
                doFrame()
        }, (tickLength).toLong())
    }


}