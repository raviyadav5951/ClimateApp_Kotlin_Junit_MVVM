package com.example.climateforecastapplication.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.climateforecastapplication.databinding.ItemWeatherDayBinding
import com.example.climateforecastapplication.model.WeekList
import java.text.SimpleDateFormat
import java.util.*

class WeatherListAdapter(private val list: ArrayList<WeekList>?) :
    RecyclerView.Adapter<WeatherListAdapter.ChildViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherListAdapter.ChildViewHolder {

        val binding=ItemWeatherDayBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding)

    }


    inner class ChildViewHolder(val binding:  ItemWeatherDayBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemCount(): Int {
       // Log.e("item", "count=" + list?.size)
        return list!!.size
    }

    fun updateWeatherList(newWeatherList:List<WeekList>){
        list?.clear()
        list?.addAll(newWeatherList)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val weekList = list?.get(position)

        val dt: Long = weekList!!.dt
        val day = SimpleDateFormat("EEEE", Locale.ENGLISH).format(
                Date(dt * 1000))
        holder.binding.tvDay.text=day
        holder.binding.tvTemp.text=weekList.temp?.day?.toInt().toString() + " °C"
    }



}