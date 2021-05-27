package com.example.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trackmysleepquality.R
import com.example.trackmysleepquality.TextItemViewHolder
import com.example.trackmysleepquality.database.SleepNight

class SleepNightAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    // 創建一個listOf SleepNight變量來保存數據。
    // 要知道RecyclerView何時顯示的數據已更改，請將自定義設置器添加到類data頂部的變量中SleepNightAdapter。
    var data = listOf<SleepNight>()
        // 在設置器中，提供data一個新值，然後調用notifyDataSetChanged()以觸發使用新數據重畫列表。
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.sleepQuality.toString()
        // 如果睡眠質量保持在小於或等於1且表示睡眠不良的視圖持有人中，將文本顏色設置為紅色。
        if (item.sleepQuality <= 1){
            holder.textView.setTextColor(Color.RED)
        } else {
            holder.textView.setTextColor(Color.DKGRAY)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}