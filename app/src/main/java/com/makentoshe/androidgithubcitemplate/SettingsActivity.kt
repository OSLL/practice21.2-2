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

class aPreference(
    var tvCONTENT: String,
    var tvINDEX: String,
    var etCONTENT: Float,
    var etINDEX: String,
    var btnINDEX: String
)

var settings = mutableListOf<aPreference>()

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        val backBtn = findViewById<Button>(R.id.BackBtn1)

        backBtn.setOnClickListener {
            finish()
        }
////////////////////////////////////////////////////////////////////////////////////////////////////
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView.adapter = MyRecyclerViewAdapter()
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        settings.add(aPreference("Predators number:", "predNumTxt", 5f, "predNum", "predBtn"))
        settings.add(aPreference("Herbivores number:", "herbNumTxt", 5f, "herbNum", "herbBtn"))
        settings.add(aPreference("Velocity/Efficiency:", "VelEffTxt", 1f, "VelEff", "VelEffBtn"))
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (i in 0..settings.lastIndex) {
            val shPr = getSharedPreferences("Settings", Context.MODE_PRIVATE)
            settings[i].etCONTENT = shPr.getFloat(settings[i].etINDEX, settings[i].etCONTENT)
        }
    }
}

class MyRecyclerViewAdapter : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.info.text = settings[pos].tvCONTENT
        holder.gettR.setText(settings[pos].etCONTENT.toString())
    }

    override fun getItemCount(): Int {
        return 3
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val info: TextView = view.findViewById(R.id.tevi)
    val gettR: EditText = view.findViewById(R.id.edte)
    val sbmt: Button = view.findViewById(R.id.sbmt)
}