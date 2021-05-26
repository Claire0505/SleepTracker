package com.example.trackmysleepquality.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.trackmysleepquality.convertLongToDateString
import com.example.trackmysleepquality.database.SleepDatabaseDAO
import com.example.trackmysleepquality.database.SleepNight
import com.example.trackmysleepquality.formatNights
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

    // 定義一個名為的變量nights。從數據庫獲取所有夜晚，並將其分配給nights變量。
    private val nights = database.getAllNights()

    // 在的定義下方nights，添加代碼以轉換nights為nightsString。使用中的formatNights()功能Util.kt。
    val nightString = Transformations.map(nights){
        nights -> formatNights(nights, application.resources)
    }

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
    fun onStartTracking(){
        viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newNight = SleepNight()
            insert(newNight)

            tonight.value = getTonightFromDatabase()

            Log.i("TAG", "onStartTracking: ${convertLongToDateString(newNight.startTimeMilli)}")
        }
    }

    private suspend fun insert(night: SleepNight) {
        database.insert(night)
    }

    /**
     * Executes when the STOP button is clicked.
     */
    fun onStopTracking() {
        viewModelScope.launch {
            // In Kotlin, the return@label syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch(),
            // not the lambda.
            val oldNight = tonight.value ?: return@launch

            // Update the night in the database to add the end time.
            oldNight.endTimeMilli = System.currentTimeMillis()

            update(oldNight)

            // Set state to navigate to the SleepQualityFragment.
            _navigateToSleepQuality.value = oldNight

            Log.i("TAG", "onStopTracking: ${convertLongToDateString(oldNight.endTimeMilli)}")
        }
    }

    private suspend fun update (night: SleepNight){
        database.update(night)
    }

    /**
     * Executes when the CLEAR button is clicked.
     */
    fun onClear(){
        viewModelScope.launch {
            clear()
            tonight.value = null
        }
    }

    suspend fun clear() {
        database.clear()
    }

    /**
     * 在中SleepTrackerViewModel，創建一個LiveData要在應用程序導航至時更改的SleepQualityFragment。
     * 使用封裝僅將的可獲取版本暴露LiveData給ViewModel。
     */
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality : LiveData<SleepNight>
    get() = _navigateToSleepQuality

    // 添加一個doneNavigating()函數來重置觸發導航的變量。
    fun doneNavigating(){
        _navigateToSleepQuality.value = null
    }
}
