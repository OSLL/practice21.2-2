package com.makentoshe.androidgithubcitemplate

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.androidplot.xy.*



import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.*
class StatsActivity : AppCompatActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        //кнопка перехода на главную страницу

        val backBtn = findViewById<Button>(R.id.BackBtn2)
        backBtn.setOnClickListener {
            finish()
        }

        //тестовые массивы

       // val time : Array<Int> = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
        //val h : Array<Int> = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 13, 15, 14, 17, 19, 16 , 15, 13, 19, 19, 16, 13, 14, 19, 17, 19, 18, 17, 15, 11, 10, 13, 16, 17, 18)
       // val p: Array<Int> = arrayOf(1, 3, 5, 7, 8, 9, 10, 13, 14, 14, 16, 17, 18, 19, 19, 20)

        //нормальные массивы

        val h : Array<Int> = fieldData.timeStats.toArray(fieldData.timeStats.herbivoresAmount)
        val p: Array<Int> = fieldData.timeStats.toArray(fieldData.timeStats.predatorsAmount)
        val time : Array<Int> = fieldData.timeStats.toArray(fieldData.timeStats.herbivoresAmount)

        for (i in 0 until h.size)
            time[i] = (i + 1)

        var n1: Int = h.size
        var n2: Int = p.size

        // функция отрисовки графиков
        fun DrawGraphics(timeArray: Array<Int>, herbivoreArray: Array<Int>, predatorArray: Array<Int>) {
            //first plot
            val domainLabelsHerbivore = timeArray
            val series1NumberHerbivore = herbivoreArray

            val series1: XYSeries = SimpleXYSeries(Arrays.asList(* series1NumberHerbivore), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1")
            val series1Format = LineAndPointFormatter(Color.BLUE, Color.RED, null, null)
            val plot1 = findViewById<XYPlot>(R.id.plot)
            series1Format.setInterpolationParams(CatmullRomInterpolator.Params(n1, CatmullRomInterpolator.Type.Centripetal))

            plot1.addSeries(series1, series1Format)
            plot1.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
                override fun format(obj: Any?, toAppendTo: StringBuffer, pos: FieldPosition?): StringBuffer {
                    val i = Math.round((obj as Number).toFloat())
                    return toAppendTo.append(domainLabelsHerbivore[i])
                }

                override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                    return null
                }
            }
            PanZoom.attach(plot1)

            //second plot

            val domainLabelsPredator = timeArray
            val series1NumberPredator = predatorArray

            val series2: XYSeries = SimpleXYSeries(Arrays.asList(* series1NumberPredator), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1")
            val series2Format = LineAndPointFormatter(Color.BLUE, Color.RED, null, null)
            val plot2 = findViewById<XYPlot>(R.id.plot2)

            series2Format.setInterpolationParams(CatmullRomInterpolator.Params(n2, CatmullRomInterpolator.Type.Centripetal))


            plot2.addSeries(series2, series2Format)
            plot2.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
                override fun format(
                    obj: Any?,
                    toAppendTo: StringBuffer,
                    pos: FieldPosition?
                ): StringBuffer {
                    val i = Math.round((obj as Number).toFloat())
                    return toAppendTo.append(domainLabelsPredator[i])
                }

                override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                    return null
                }
            }
            PanZoom.attach(plot2)
        }

        //Quantity of minions

        fun Quantity(herbivoreArray: Array<Int>, predatorArray: Array<Int>){
            var qntOfHerbivores : Int = herbivoreArray.last()
            var qntOfPredators : Int = predatorArray.last()
            var averageQntH: Int = herbivoreArray.average().toInt()
            var averageQntP : Int = predatorArray.average().toInt()

            var herbivores = findViewById<TextView>(R.id.textView2)
            herbivores.setText("Quantity of herbivores: $qntOfHerbivores")
            var predators = findViewById<TextView>(R.id.textView3)
            predators.setText("Quantity of predators: $qntOfPredators")
            var average1 = findViewById<TextView>(R.id.textView4)
            average1.setText("Average quantity: $averageQntH")
            var average2 = findViewById<TextView>(R.id.textView5)
            average2.setText("Average quantity: $averageQntP")
        }

        DrawGraphics(time, h, p)
        Quantity(h, p)
    }
}