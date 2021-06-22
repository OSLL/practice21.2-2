package com.makentoshe.androidgithubcitemplate

import kotlin.math.max

class TimeStatistic {
    public var maxSize = 10

    public var predatorsAmount = mutableListOf<Int>()
    public var herbivoresAmount = mutableListOf<Int>()
    public var plantsAmount = mutableListOf<Int>()

    fun toArray(list: MutableList<Int>): Array<Int> {
        var array = Array<Int>(list.size){0}
        for (i in array.indices)
            array[i] = list[i]
        return array
    }

    fun addTo(list : MutableList<Int>, number : Int){
        if (list.size < maxSize)
            list.add(number)
        else
        {
            for (i in (0..(list.size / 2 - 1)))
                list[i] = list[i * 2]

            var length = list.size
            for (i in (0..(length / 2))){
                list.removeAt(list.size - 1)
            }

            list.add(number)
        }
    }
}