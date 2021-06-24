package com.makentoshe.androidgithubcitemplate

import java.util.Collections.max

class TimeStatistic {
    public var maxSize = 100000
    private var maxArraySize = 100

    public var predatorsAmount = mutableListOf<Int>()
    public var herbivoresAmount = mutableListOf<Int>()
    public var plantsAmount = mutableListOf<Int>()


    private val statsAmount = 3
    private var statsWasSqueezed = 0
    private var length = 1
    private var pass = statsAmount

    fun toArray(list: MutableList<Int>): Array<Int> {
        var k = list.size / maxArraySize
        if (k == 0)
            k = 1
        var array = Array<Int>(list.size / k){0}
        for (i in (0..(array.size - 2)))
            array[i] = list[i * k]

        array[array.lastIndex] = list[list.lastIndex]
        return array
    }

    fun addTo(list : MutableList<Int>, number : Int){
        if (list.size < maxSize){
            list.add(number)
        }
        else
        {
            list.removeAt(0)
            list.add(number)
        }
    }
}