package com.example.addverb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class CountriesAdapter(val clist:ArrayList<CountryModel>):RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {
    class CountryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name=view.findViewById<TextView>(R.id.txtname)
        val capital=view.findViewById<TextView>(R.id.txtcapital)
        val region=view.findViewById<TextView>(R.id.txtregion)
        val subreg=view.findViewById<TextView>(R.id.txtsubreg)
        val pop=view.findViewById<TextView>(R.id.txtpop)
        val border=view.findViewById<TextView>(R.id.txtborder)
        val image=view.findViewById<ImageView>(R.id.img)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CountryViewHolder {
        return CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.country_single,parent,false))
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val country=clist[position]
        holder.name.text=country.common
        holder.capital.text=country.capital
        Picasso.get().load(country.png).into(holder.image)
        holder.region.text=country.region
        holder.subreg.text=country.subregion
        holder.pop.text=country.population.toString()
        holder.border.text=country.borders
    }

    override fun getItemCount(): Int {
        return clist.size
    }

}