package com.example.trackmysleepquality.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
在此任務中，您將一個晚上的睡眠定義為帶註釋的數據類，該數據類表示數據庫實體。
對於一晚的睡眠，您需要記錄開始時間，結束時間和質量等級。
並且您需要一個ID來唯一地標識那一晚。
*/
@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight (
    // autoGenerate = true 以便Room為每個實體生成ID。這樣可以保證每晚的ID是唯一的。
    @PrimaryKey(autoGenerate = true)
    var nightId: Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long = startTimeMilli,

    @ColumnInfo(name = "quality_rating")
    var sleepQuality: Int = -1 // 將其設置為-1，表示尚未收集到任何質量數據。

)