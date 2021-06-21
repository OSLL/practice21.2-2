package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.androidplot.xy.*



import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.*

class StatsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val backBtn = findViewById<Button>(R.id.BackBtn2)
        //first plot
        val domainLabelsHerbivore = arrayOf<Number>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val series1NumberHerbivore = arrayOf<Number>(20, 24, 28, 22, 26, 22, 26, 29, 23, 23)

        val series1 : XYSeries = SimpleXYSeries(Arrays.asList(* series1NumberHerbivore), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1")
        val series1Format = LineAndPointFormatter(Color.BLUE, Color.RED, null, null )
        val plot1 = findViewById<XYPlot>(R.id.plot)

        series1Format.setInterpolationParams(CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal))

        plot1.addSeries(series1, series1Format)
        plot1.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object: Format(){
            override fun format(obj: Any?, toAppendTo: StringBuffer, pos: FieldPosition?): StringBuffer {
                val i = Math.round((obj as Number).toFloat())
                return toAppendTo.append(domainLabelsHerbivore[i])
            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }

        }
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        PanZoom.attach(plot1)

        //second plot

        val domainLabelsPredator = arrayOf<Number>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val series1NumberPredator = arrayOf<Number>(20, 24, 28, 22, 26, 22, 26, 29, 23, 23)

        val series2 : XYSeries = SimpleXYSeries(Arrays.asList(* series1NumberPredator), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1")
        val series2Format = LineAndPointFormatter(Color.BLUE, Color.RED, null, null )
        val plot2 = findViewById<XYPlot>(R.id.plot2)

        series2Format.setInterpolationParams(CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal))

        plot2.addSeries(series2, series2Format)
        plot2.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object: Format(){
            override fun format(obj: Any?, toAppendTo: StringBuffer, pos: FieldPosition?): StringBuffer {
                val i = Math.round((obj as Number).toFloat())
                return toAppendTo.append(domainLabelsPredator[i])
            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }

        }
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        PanZoom.attach(plot2)
    }
}