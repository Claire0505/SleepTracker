package com.example.trackmysleepquality.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.trackmysleepquality.R
import com.example.trackmysleepquality.convertDurationToFormatted
import com.example.trackmysleepquality.convertNumericQualityToString
import com.example.trackmysleepquality.database.SleepNight

// 此功能將成為您計算和格式化睡眠時間的適配器。
// 將item參數的類型更改為可空，並用 包裹主體item?.let{...}。
@BindingAdapter("setSleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item : SleepNight?){
    item?.let {
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
    }
}

// 根據對像中的值設置睡眠質量SleepNight
@BindingAdapter("setSleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight?){
    item?.let {
        text = convertNumericQualityToString(item.sleepQuality, context.resources)
    }
}

// 用於將圖像設置在圖像視圖上
@BindingAdapter("setSleepImage")
fun ImageView.setSleepImage(item: SleepNight?) {
    item?.let {
        setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    }
}