package com.example.trackmysleepquality.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trackmysleepquality.database.SleepDatabaseDAO
import com.example.trackmysleepquality.database.SleepNight

class SleepDetailViewModel (
    private val sleepNightKey : Long = 0L,
    dataSource : SleepDatabaseDAO) : ViewModel() {

    /**
     * Hold a reference to SleepDatabase via its SleepDatabaseDao.
     */
    val database = dataSource

    private val night : LiveData<SleepNight>
    fun getNight() = night

    init {
        night = database.getNightWithId(sleepNightKey)
    }

    /**
     * Variable that tells the fragment whether it should navigate to [SleepTrackerFragment].
     *
     * This is `private` because we don't want to expose the ability to set [MutableLiveData] to
     * the [Fragment]
     */
    private val _navigateToSleepTracker  = MutableLiveData<Boolean>()

    /**
     * When true immediately navigate back to the [SleepTrackerFragment]
     */
    val navigateToSleepTracker : LiveData<Boolean?>
    get() = _navigateToSleepTracker

    /**
     * Call this immediately after navigating to [SleepTrackerFragment]
     * 導航到 [SleepTrackerFragment] 後立即調用
     */
    fun doneNavigating(){
        _navigateToSleepTracker.value = null
    }

    fun onClose(){
        _navigateToSleepTracker.value = true
    }

}