package com.example.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.trackmysleepquality.database.SleepDatabaseDAO

/**
 * 您的sleep-tracker視圖模型將處理按鈕單擊，通過DAO與數據庫進行交互以及通過向UI提供數據LiveData。
 * 所有數據庫操作都必須在主UI線程之外運行，您將使用協程進行操作。
 */

class SleepTrackerViewModel (
    val database : SleepDatabaseDAO,
    application: Application) : AndroidViewModel(application){

    }
