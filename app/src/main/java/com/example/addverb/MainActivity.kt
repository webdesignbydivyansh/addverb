package com.example.addverb

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    val db by lazy {
        Room.databaseBuilder(this,AppDatabase::class.java,"User.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    lateinit var recyclerAdapter:CountriesAdapter

    @DelicateCoroutinesApi
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerCountry=findViewById<RecyclerView>(R.id.rvView)
        val countryList= arrayListOf<CountryModel>()
        var layoutManager:RecyclerView.LayoutManager

        val queue=Volley.newRequestQueue(this)
        val url="https://restcountries.com/v3.1/region/asia"

        if(isOnline(applicationContext))
        {
            val jsonArrayRequest=JsonArrayRequest(Request.Method.GET,url,null,
                {
                    try {
                        for (i in 0 until it.length())
                        {
                            val country=it.getJSONObject(i)
                            val nameObject=country.getJSONObject("name").getString("common")
                            val capitalArray=country.getJSONArray("capital")
                            var s=""
                            for (j in 0 until capitalArray.length())
                                s=s+capitalArray[j]+", "
                            val imageObject=country.getJSONObject("flags").getString("png")
                            val regionObject=country.getString("region")
                            val subregObject=country.getString("subregion")
                            val popObject=country.getInt("population")
                            val borderObject=country.getJSONArray("borders")
                            var s1=""
                            for (j in 0 until borderObject.length())
                                s1=s1+borderObject[j]+", "
                            val countryObject=CountryModel(nameObject,s,imageObject,regionObject,subregObject,popObject,s1)
                            countryList.add(countryObject)
                            layoutManager=LinearLayoutManager(this)
                            recyclerAdapter= CountriesAdapter(countryList)
                            recyclerCountry.adapter=recyclerAdapter
                            recyclerCountry.layoutManager=layoutManager

                            GlobalScope.launch(Dispatchers.IO) {
                                db.countryDao().insertCountry(countryObject)
                            }
                        }
                    }
                    catch (e: JSONException)
                    {
                        Toast.makeText(this,"Some error occured!",Toast.LENGTH_SHORT).show()
                    }
                },
                {
                    Toast.makeText(this,"Volley error occured $it!",Toast.LENGTH_SHORT).show()
                    Log.d("TAG","$it")
                })
            queue.add(jsonArrayRequest)
        }
        else
        {
            startActivity(Intent(this,OfflineActivity::class.java))
        }
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
}