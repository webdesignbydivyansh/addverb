package com.example.addverb

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OfflineActivity : AppCompatActivity() {
    val db by lazy {
        Room.databaseBuilder(this,AppDatabase::class.java,"User.db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    lateinit var offAdapter:CountriesAdapter
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline)


        var layoutManager:RecyclerView.LayoutManager
        val offlist= arrayListOf<CountryModel>()
        val rcView=findViewById<RecyclerView>(R.id.rcView)

        val list = db.countryDao().getTask()
                for (i in list) {
                offlist.add(i)
                layoutManager = LinearLayoutManager(this)
                offAdapter = CountriesAdapter(offlist)
                rcView.adapter = offAdapter
                rcView.layoutManager = layoutManager
            }
                offAdapter.notifyDataSetChanged()

    val btn=findViewById<Button>(R.id.btndel)
    btn.setOnClickListener {
        GlobalScope.launch(Dispatchers.IO) {
            db.countryDao().deleteAll()
        }
        offlist.clear()
        offAdapter.notifyDataSetChanged()
    }
    }
}