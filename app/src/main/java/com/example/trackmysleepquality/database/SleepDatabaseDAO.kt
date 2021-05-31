package com.example.trackmysleepquality.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 *  在Android上，DAO提供了用於插入，刪除和更新數據庫的便捷方法。
    使用Room數據庫時，可以通過在代碼中定義和調用Kotlin函數來查詢數據庫。
    這些Kotlin函數映射到SQL查詢。您可以使用註釋在DAO中定義這些映射，並Room創建必要的代碼。
 *  定義將SleepNight類與Room一起使用的方法。
 */
@Dao
interface SleepDatabaseDAO {
    @Insert
    suspend fun insert(night: SleepNight)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param night new value to write
     */
    @Update
    suspend fun update(night: SleepNight)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId = :key")
    suspend fun get(key: Long): SleepNight?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    /**
     * Selects and returns the latest night.
     * 要從數據庫中獲取“今晚”信息，請編寫一個SQLite查詢，該查詢返回nightId
     * 按降序排列的結果列表的第一個元素。用於LIMIT 1僅返回一個元素。
     */
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

    /**
     * Selects and returns all rows in the table,
     * sorted by start time in descending order.
     *
     * 已以getAllNights()返回SleepNight實體列表LiveData。為您Room保持LiveData更新狀態，這意味著您只需要顯式獲取一次數據。
     * 需要導入LiveData的androidx.lifecycle.LiveData。
     */
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    /**
     * Selects and returns the night with given nightId.
     */
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun getNightWithId(key: Long): LiveData<SleepNight>
}