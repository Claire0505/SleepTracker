package com.example.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackmysleepquality.database.SleepDatabaseDAO
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepQualityFragment.
 *
 * @param sleepNightKey The key of the current night we are working on.
 */
class SleepQualityViewModel
    (private val sleepNightKey : Long = 0L,
     val database: SleepDatabaseDAO): ViewModel() {

    /**
     * Variable that tells the fragment whether it should navigate to [SleepTrackerFragment].
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the [Fragment]
     */
    private val _navigateToSleepTracker = MutableLiveData<Boolean>()

    /**
     * 設置為true時，立即導航回到 [SleepTrackerFragment]
     */
    val navigateToSleepTracker : LiveData<Boolean>
    get() = _navigateToSleepTracker

    /**
     * 導航到[SleepTrackerFragment]後立即調用此函數
     */
    fun doneNavigating(){
        _navigateToSleepTracker.value = null
    }

    /**
     *  設置睡眠質量並更新數據庫。
     *  設置睡眠質量並更新數據庫。
     */
    fun onSetSleepQuality( quality : Int){
        viewModelScope.launch {
            val tonight = database.get(sleepNightKey) ?: return@launch
            tonight.sleepQuality = quality
            database.update(tonight)

            // 將此狀態變量設置為true將警告觀察者並觸發導航。
            _navigateToSleepTracker.value = true
        }

    }



}