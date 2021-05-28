package com.example.trackmysleepquality.sleeptracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trackmysleepquality.*
import com.example.trackmysleepquality.database.SleepNight

class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {
    // 創建一個listOf SleepNight變量來保存數據。
    // 要知道RecyclerView何時顯示的數據已更改，請將自定義設置器添加到類data頂部的變量中SleepNightAdapter。
    var data = listOf<SleepNight>()
        // 在設置器中，提供data一個新值，然後調用notifyDataSetChanged()以觸發使用新數據重畫列表。
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val res = holder.itemView.context.resources
        holder.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
        holder.quality.text = convertNumericQualityToString(item.sleepQuality, res)
        holder.qualityImage.setImageResource( when (item.sleepQuality){
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    }

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * 建立ViewHolder 在內部ViewHolder，獲取對視圖的引用。
     * 每次綁定時ViewHolder，都需要訪問圖像和兩個文本視圖。（您可以將此代碼轉換為以後使用數據綁定。）
     */
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val sleepLength : TextView = itemView.findViewById(R.id.sleep_tv_length)
        val quality : TextView = itemView.findViewById(R.id.quality_tv_string)
        val qualityImage : ImageView = itemView.findViewById(R.id.quality_image)

    }
}