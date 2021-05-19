package com.example.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackmysleepquality.database.SleepDatabaseDAO

class SleepTrackerViewModelFactory(
    private val dataSource: SleepDatabaseDAO,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UnChecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepTrackerViewModel::class.java)) {
            return SleepTrackerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("UnKnow ViewModel class")
    }
}