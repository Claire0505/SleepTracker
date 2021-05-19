package com.example.trackmysleepquality.sleeptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.trackmysleepquality.R
import com.example.trackmysleepquality.database.SleepDatabase
import com.example.trackmysleepquality.databinding.FragmentSleepTrackerBinding

class SleepTrackerFragment : Fragment() {
    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleep_tracker, container, false
        )

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory. 要獲取對數據庫DAO的引用
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDAO
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated (關聯的) with this fragment.
        // 獲取對此片段關聯的ViewModel的引用。
        val sleepTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        //將當前活動指定為綁定的生命週期所有者。以便綁定可以觀察LiveData更新。
        binding.setLifecycleOwner(this)

        //若要將View Model與數據綁定一起使用，您必須顯式 ,給綁定對像一個引用。
        binding.sleepTrackerViewModel = sleepTrackerViewModel



        return binding.root
    }

}