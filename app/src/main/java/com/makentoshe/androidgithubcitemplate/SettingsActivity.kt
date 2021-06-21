package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class aPreference(var tvCONTENT: String, var tvINDEX: String, var etCONTENT: Float, var etINDEX: String, var btnINDEX: String)
var settings = mutableListOf<aPreference>()

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Checkj", "onCreate")
        setContentView(R.layout.activity_settings)
        Log.d("Checkj", "setContentView")

        /*val shPrload = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        val backBtn = findViewById<Button>(R.id.BackBtn1)

        backBtn.setOnClickListener {
            finish()
        }

        val sbmtBtnPredNum = findViewById<Button>(R.id.PNBtn)
        val predTxt = findViewById<EditText>(R.id.editText)

        val sbmtBtnHerbNum = findViewById<Button>(R.id.HNBtn)
        val herbTxt = findViewById<EditText>(R.id.editText2)

        val sbmtBtnVelEff = findViewById<Button>(R.id.VEBtn)
        val velEffTxt = findViewById<EditText>(R.id.editText3)

        val sbmtBtnPoForPl = findViewById<Button>(R.id.PfPBtn)
        val poForPlTxt = findViewById<EditText>(R.id.editText4)

        //  При нажатии на сохранялку "Submit ..."(SbmtBtn...): в SharedPreferences остаются строки с целыми числовыми параметрами со своими Ключами(тэгами)
        //   (например: количество хищников и травоядных, коэффициент связи увеличения СКОРОСТИ и увеличения РАСХОДА ЭНЕРГИИ)
        val shPrSave = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = shPrSave.edit()

        //Обеспечиваем вывод в editText'ах актуальных настроек
        predTxt.setText(shPrload.getInt("PredNum", 5).toString())
        herbTxt.setText(shPrload.getInt("HerbNum", 5).toString())
        velEffTxt.setText(shPrload.getFloat("VelEff", 1f).toString())
        poForPlTxt.setText(shPrload.getFloat("PoForPl", 1f).toString())



        //Количество хищников

        sbmtBtnPredNum.setOnClickListener{
                editor.apply{
                    putInt("PredNum", predTxt.text.toString().toInt())
                }.apply()
        }

        //Количество травоядных
        sbmtBtnHerbNum.setOnClickListener{
            editor.apply{
                putInt("HerbNum", herbTxt.text.toString().toInt())
            }.apply()
        }

        //Отношение изменения скорости к соответствующему изменению эффективности
        sbmtBtnVelEff.setOnClickListener{
            editor.apply{
                putFloat("VelEff", velEffTxt.text.toString().toFloat())
            }.apply()
        }
        //Очки за съедение растения
        sbmtBtnPoForPl.setOnClickListener{
            editor.apply{
                putFloat("PoForPl", poForPlTxt.text.toString().toFloat())
            }.apply()
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Пример доступа к полученным значениям
        //Примечания: Даже после закрытия приложения данные, сохранённые нажатием Submit, остаются в памяти до следующего изменения или перезагрузки телефона.
        //            В ЛЮБОМ МЕСТЕ в программе можно реализовать следующий код и получить значения!
        // Важная строка в начале: [   val shPrload = getSharedPreferences("Settings", Context.MODE_PRIVATE)   ]
        Toast.makeText(this,
            "Predators:" + shPrload.getInt("PredNum", 5).toString() +
            "\nHerbivores:" + shPrload.getInt("HerbNum", 5).toString() +
            "\nVelocity/Efficiency:" + shPrload.getFloat("VelEff", 1f).toString() +
            "\nPoints for plant:" + shPrload.getFloat("PoForPl", 1f).toString() +
            "\nDefault check:" + shPrload.getString("DeCh", "default text"),
            Toast.LENGTH_SHORT).show() //null - параметр по умолчанию
*/
        //Через RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = MyRecyclerViewAdapter()
        val shPr = getSharedPreferences("Settings", MODE_PRIVATE)
        /*fun fillSettings(){
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            settings.add(aPreference("Predators number:", "predNumTxt", 5f, "predNum", "predBtn"))
            settings.add(aPreference("Herbivores number:", "herbNumTxt", 5f, "herbNum", "herbBtn"))
            settings.add(aPreference("Velocity/Efficiency:", "VelEffTxt", 1f, "VelEff", "VelEffBtn"))
            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }
        for(i in 0..settings.lastIndex) {
            val shPr = getSharedPreferences("Settings", Context.MODE_PRIVATE)
            settings[i].etCONTENT = shPr.getFloat(settings[i].etINDEX, settings[i].etCONTENT)
        }*/
    }
}

class MyRecyclerViewAdapter: RecyclerView.Adapter<MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.info.text = settings[pos].tvCONTENT
        holder.gettR.setText(settings[pos].etCONTENT.toString())
        holder.sbmt.setOnClickListener{
            val shPr:SharedPreferences = this.getSharedPreferences("Settings", Context.MODE_PRIVATE)
            val editor = shPr.edit()
            editor.apply{
                putFloat(settings[pos].etINDEX, settings[pos].etCONTENT)
            }

        }
    }

    override fun getItemCount(): Int{
        return 3
    }
}

class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
    val info: TextView = view.findViewById(R.id.tevi)
    val gettR: EditText = view.findViewById(R.id.edte)
    val sbmt: Button = view.findViewById(R.id.sbmt)
}