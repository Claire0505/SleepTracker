package com.example.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackmysleepquality.R
import com.example.trackmysleepquality.convertDurationToFormatted
import com.example.trackmysleepquality.convertNumericQualityToString
import com.example.trackmysleepquality.database.SleepNight
import com.example.trackmysleepquality.databinding.ListItemSleepNightBinding

// SleepNightDiffCallback()作為參數添加到構造函數。在ListAdapter將使用它來找出在列表中發生了什麼變化。
class SleepNightListAdapter  : ListAdapter<SleepNight,
        SleepNightListAdapter.MyViewHolder>(SleepNightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(myholder: MyViewHolder, position: Int) {
        val item = getItem(position)
        myholder.bind(item)
    }

    class MyViewHolder private constructor(val binding: ListItemSleepNightBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item: SleepNight) {
            val res = itemView.context.resources
            binding.sleepTvLength.text =
                convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)

            binding.qualityTvString.text = convertNumericQualityToString(item.sleepQuality, res)

            binding.qualityImage.setImageResource(
                when (item.sleepQuality) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_active
                }
            )
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }
    /**
     * RecyclerView有一個稱為的類DiffUtil，用於計算兩個列表之間的差異，計算出要從舊列表生成新列表的最小更改數。
     * DiffUtil列出一個舊列表和一個新列表，並找出有什麼不同。它查找已添加，刪除或更改的項目。
     *
     */

    class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>(){
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            // 測試兩個傳入SleepNight項目oldItem和newItem是否相同的代碼。
            // 如果項目相同nightId，則它們是相同的項目，因此請返回true。否則，返回false。
            // DiffUtil使用此測試可幫助發現是否添加，刪除或移動了一項。
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            // 檢查是否oldItem與newItem包含相同的數據;
            // 相等檢查將檢查所有字段，因為它SleepNight是一個數據類。
            // Data類會自動equals為您定義和其他一些方法。
            // 如果oldItem和之間存在差異newItem，則此代碼DiffUtil表明該項目已更新。
            return oldItem == newItem
        }

    }

}