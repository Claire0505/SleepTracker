package com.example.trackmysleepquality.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackmysleepquality.database.SleepDatabaseDAO
import java.lang.IllegalArgumentException

class SleepQualityViewModelFactory(
    private val sleepNightKey : Long,
    private val dataSource : SleepDatabaseDAO ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityViewModel::class.java)) {
            return SleepQualityViewModel(sleepNightKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}