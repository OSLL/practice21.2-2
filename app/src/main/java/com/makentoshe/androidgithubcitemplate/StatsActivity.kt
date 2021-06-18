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
        val BackBtn = findViewById<Button>(R.id.BackBtn2)

        val domainLabels = arrayOf<Number>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val series1Number = arrayOf<Number>(1, 4, 8, 12, 16, 32, 26, 29, 10, 13)
        val series1 : XYSeries = SimpleXYSeries(Arrays.asList(* series1Number), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1")
        val series1Format = LineAndPointFormatter(Color.BLUE, Color.RED, null, null )
        val plot = findViewById<XYPlot>(R.id.plot)

        series1Format.setInterpolationParams(CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal))

        plot.addSeries(series1, series1Format)
        plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object: Format(){
            override fun format(obj: Any?, toAppendTo: StringBuffer, pos: FieldPosition?): StringBuffer {
                val i = Math.round((obj as Number).toFloat())
                return toAppendTo.append(domainLabels[i])
            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }

        }

        BackBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}