package com.example.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.trackmysleepquality.database.SleepDatabaseDAO
import com.example.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.launch

/**
 * 您的sleep-tracker視圖模型將處理按鈕單擊，通過DAO與數據庫進行交互以及通過向UI提供數據LiveData。
 * 所有數據庫操作都必須在主UI線程之外運行，您將使用協程進行操作。
 */

class SleepTrackerViewModel(
    val database: SleepDatabaseDAO,
    application: Application
) : AndroidViewModel(application) {

    // 定義一個變量tonight來保存當前的夜晚。設置變量MutableLiveData，需要能夠觀察和更改數據。
    private var tonight = MutableLiveData<SleepNight?>()

    // 要tonight盡快初始化變量，請init在tonight和的定義下方創建一個塊initializeTonight()。
    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        var night = database.getTonight()
        if (night?.endTimeMilli != night?.startTimeMilli){
            night = null
        }
        return  night
    }

    /**
     * Executes when the START button is clicked.
     * 在內部onStartTracking()，在中啟動協程viewModelScope，因為您需要此結果才能繼續並更新UI。
     */
    fun onStartTracker(){
        viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newNight = SleepNight()
            insert(newNight)
        }
    }

    private suspend fun insert(night: SleepNight) {
        database.insert(night)
    }
}
