package com.example.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackmysleepquality.R
import com.example.trackmysleepquality.database.SleepNight
import com.example.trackmysleepquality.databinding.ListItemSleepNightBinding

// 這RecyclerView將需要區分每個項目的視圖類型，以便它可以正確地為其分配一個視圖持有者。
private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

// SleepNightDiffCallback()作為參數添加到構造函數。在ListAdapter將使用它來找出在列表中發生了什麼變化。
class SleepNightListAdapter(val clickListener: SleepNightListener) : ListAdapter<DataItem,
        RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    // 將使用此函數添加標題，然後提交列表，而不是使用submitList()提供的ListAdapter來提交您的列表。
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        val items = when(list){
            null -> listOf(DataItem.Header)
            else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
        }
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> MyViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
    }

    /**
     * getItemViewType()以根據當前項目的類型返回正確的標題或項目常量。
     */
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }

    class MyViewHolder private constructor(val binding: ListItemSleepNightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            // 將單擊偵聽器分配給函數binding內部的對象bind()
            binding.clickListener = clickListener
            // 添加binding.executePendingBindings()。此調用是一種優化，它要求數據綁定立即執行任何未決的綁定。
            binding.executePendingBindings()
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

    class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            // 測試兩個傳入SleepNight項目oldItem和newItem是否相同的代碼。
            // 如果項目相同nightId，則它們是相同的項目，因此請返回true。否則，返回false。
            // DiffUtil使用此測試可幫助發現是否添加，刪除或移動了一項。
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            // 檢查是否oldItem與newItem包含相同的數據;
            // 相等檢查將檢查所有字段，因為它SleepNight是一個數據類。
            // Data類會自動equals為您定義和其他一些方法。
            // 如果oldItem和之間存在差異newItem，則此代碼DiffUtil表明該項目已更新。
            return oldItem == newItem
        }

    }

}

/**
 *  創建一個點擊監聽器並從item佈局中觸發它
 *  處理點擊的回調應該有一個有用的標識符名稱。使用clickListener作為其名稱。
 *  該clickListener回調只需要night.nightId從數據庫中存取數據。
 */
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

/**
 * sealed class 是一個 abstract class ，本身並不能被 instantiate（實體化）。
 * 但可以透過繼承某個 sealed class ，限縮可能的類型。
 * 可以傳遞變數這種特性讓它可以作為進階版的 enum 使用，與 when語法配合後效果更佳。
 */
sealed class DataItem {
    // 當adapter用於DiffUtil確定項目是否以及如何更改時，DiffItemCallback需要知道每個項目的 id。
    // 您將看到一個錯誤，因為SleepNightItem並且Header需要覆蓋抽象屬性id。
    abstract val id: Long

    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        // 在 中SleepNightItem，覆蓋id以返回nightId.
        override val id = sleepNight.nightId
    }

    // Header, 表示標題。由於標頭沒有實際數據，您可以將其聲明為object. 這意味著將永遠只有一個Header. 再次，讓它擴展DataItem。
    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }
}