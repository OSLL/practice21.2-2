package com.makentoshe.androidgithubcitemplate

import android.os.Handler
import android.os.Looper

class Field(
    fieldViewSet: FieldView
) {
    private var fieldView = fieldViewSet

    private var go = true
    var speed = 1f

    private var tickLength = 100f

    fun stopProcess() {
        go = false
    }

    fun startProcess() {
        go = true
        doFrame()
    }

    fun setTick(tick: Float) {
        tickLength = tick
    }

    fun speedDecrease() {
        if (speed >= 0.2)
            speed /= 1.2f
    }

    fun speedIncrease() {
        if (speed < 5f)
            speed *= 1.2f
    }

    fun doFrame() {
        Handler(Looper.getMainLooper()).postDelayed({

            fieldData.update(tickLength.toLong())

            fieldView.setListsToDraw(fieldData.predatorsList, fieldData.herbivoresList, fieldData.plantsList)
            fieldView.invalidate()
            if (go)
                doFrame()
        }, (tickLength).toLong())
    }


}