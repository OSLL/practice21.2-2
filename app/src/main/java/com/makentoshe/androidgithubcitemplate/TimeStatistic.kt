package com.makentoshe.androidgithubcitemplate

class TimeStatistic {
    public var maxSize = 20

    public var predatorsAmount = mutableListOf<Int>()
    public var herbivoresAmount = mutableListOf<Int>()
    public var plantsAmount = mutableListOf<Int>()

    private val statsAmount = 3
    private var statsWasSqueezed = 0
    private var length = 1
    private var pass = 0

    fun toArray(list: MutableList<Int>): Array<Int> {
        var array = Array<Int>(list.size){0}
        for (i in array.indices)
            array[i] = list[i]
        return array
    }

    fun addTo(list : MutableList<Int>, number : Int){
        pass++
        if (list.size < maxSize)
            if (pass == length) {
                list.add(number)
                pass = 0
            }
            else
                pass += 1
        else
        {
            statsWasSqueezed++
            if (statsWasSqueezed == statsAmount){
                statsWasSqueezed = 0
                length *= 2
            }

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