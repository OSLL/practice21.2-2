package com.makentoshe.androidgithubcitemplate

import android.os.Handler
import android.os.Looper

class Field(
    fieldViewSet: FieldView
) {
    private var fieldView = fieldViewSet

    private var go = !isPauseEnabled
    var speed = 1f
    private var speeds = arrayOf(0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 4f, 5f)
    var speedIndex = 1

    private var tickLength = 50f

    var time = System.currentTimeMillis()

    fun stopProcess() {
        go = false
    }

    fun startProcess() {
        go = true
        time = System.currentTimeMillis()
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
        fieldData.ticksPassed = 0
    }

    fun speedIncrease() {
        if (speedIndex < speeds.size - 1)
            speedIndex++
        speed = speeds[speedIndex]
        fieldData.ticksPassed = 0
    }

    fun doFrame() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isPauseEnabled)
                fieldData.update(speed)

            fieldView.setListsToDraw(fieldData.predatorsList, fieldData.herbivoresList, fieldData.plantsList)
            fieldView.invalidate()
            if (!isPauseEnabled)
                doFrame()
        }, (tickLength).toLong())
    }


}