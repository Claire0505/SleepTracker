package com.example.trackmysleepquality.sleeptracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
         *  當navigateToSleepDetail更改時，導航到SleepDetailFragment，傳入night，
         *  然後調用onSleepDetailNavigated()。
         */
        sleepTrackerViewModel.navigateToSleepDetail.observe(viewLifecycleOwner, Observer {
            night -> night?.let {
                this.findNavController().navigate(SleepTrackerFragmentDirections
                    .actionSleepTrackerFragmentToSleepDetailFragment(night))
            sleepTrackerViewModel.onSleepDetailNavigated()
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
       // 添加調用點擊處理程序
        val listAdapter = SleepNightListAdapter(SleepNightListener { nightId ->
           //Toast.makeText(context, "${nightId}", Toast.LENGTH_LONG).show()
           sleepTrackerViewModel.onSleepNightClicked(nightId)
       })
        binding.sleepRvList.adapter = listAdapter

        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {
            it?.let {
                listAdapter.addHeaderAndSubmitList(it)
            }
        })

        // 要修復標題寬度，您需要告訴GridLayoutManager何時跨所有列跨越數據。
        // 您可以通過SpanSizeLookup在GridLayoutManager.
        // 這是一個配置對象，GridLayoutManager用於確定列表中每個項目使用的跨度數。
        val manager = GridLayoutManager(activity, 3)

        // getSpanSize()，返回每個位置的正確跨度大小。位置 0 的跨度大小為 3，其他位置的跨度大小為 1。
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =  when (position) {
                0 -> 3
                else -> 1
            }
        }

        binding.sleepRvList.layoutManager = manager

        return binding.root
    }

}