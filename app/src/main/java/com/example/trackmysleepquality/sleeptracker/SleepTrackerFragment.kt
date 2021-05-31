package com.example.trackmysleepquality.sleeptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.trackmysleepquality.R
import com.example.trackmysleepquality.database.SleepDatabase
import com.example.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

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

        // GridLayoutManager
        val manager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        binding.sleepRvList.layoutManager = manager

        val application = requireNotNull(this.activity).application

        // Create an instance of the ViewModel Factory. 要獲取對數據庫DAO的引用
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDAO
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated (關聯的) with this fragment.
        // 獲取對此片段關聯的ViewModel的引用。
        val sleepTrackerViewModel =
            ViewModelProvider(this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        //若要將View Model與數據綁定一起使用，您必須顯式 ,給綁定對像一個引用。
        binding.sleepTrackerViewModel = sleepTrackerViewModel

        //將當前活動指定為綁定的生命週期所有者。以便綁定可以觀察LiveData更新。
        binding.setLifecycleOwner(this)

        // 在按下STOP按鈕時，在狀態變量上添加一個用於導航的Observer。
        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer { night ->
            night?.let {
                // We need to get the navController from this, because button is not ready, and it
                // just has to be a view. For some reason, this only matters if we hit stop again
                // after using the back button, not if we hit stop and choose a quality.
                // Also, in the Navigation Editor, for Quality -> Tracker, check "Inclusive" for
                // popping the stack to get the correct behavior if we press stop multiple times
                // followed by back.
                // Also: https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneNavigating()
            }
        })

        // Add an Observer on the state variable for showing a Snackbar message
        // when the CLEAR button is pressed.
        sleepTrackerViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            if ( it == true){
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT).show()
                // Reset state to make sure the snackbar is only shown once, even if the device
                // has a configuration change.
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })

        /**
         * RecycleView
         * 通過提供片段的viewLifecycleOwner生命週期所有者，您可以確保該觀察者僅RecyclerView在屏幕上處於活動狀態時才處於活動狀態。
         * 在觀察器內部，只要獲得非null值（for nights），就將該值分配給適配器的data。
         */

//        val adapter = SleepNightAdapter()
//        binding.sleepRvList.adapter = adapter
//        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                adapter.data = it
//            }
//        })

       /**
        * ListAdapter提供了一種稱為的方法，submitList()用於告訴您ListAdapter列表的新版本可用。
        * 調用此方法時，ListAdapterdiff將新列表與舊列表進行比較，並檢測添加，刪除，移動或更改的項目。
        * 然後ListAdapter更新由表示的項目RecyclerView。
        */
        val listAdapter = SleepNightListAdapter()
        binding.sleepRvList.adapter = listAdapter
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.submitList(it)
            }
        })


        return binding.root
    }

}