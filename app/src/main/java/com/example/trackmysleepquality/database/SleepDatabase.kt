package com.example.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * A database that stores SleepNight information.
 * And a global method to get access to the database.
 *
 * This pattern is pretty much the same for any database,
 * so you can reuse it.
 */
// 請提供，並將其SleepNight作為列表的唯一項entities。
// 設置version為1**。**每當更改架構時，都必須增加版本號。
// 設置exportSchema為false，以免保留架構版本歷史記錄備份。
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {
    /**
     * Connects the database to the DAO.
     * 數據庫需要了解DAO。在類的主體內，聲明一個返回的抽象值SleepDatabaseDao。您可以有多個DAO。
     */
    abstract val sleepDatabaseDAO: SleepDatabaseDAO

    /**
     * 定義一個companion對象。伴隨對象使客戶端無需實例化類即可訪問用於創建或獲取數據庫的方法。
     * INSTANCE will keep a reference to any database returned via getInstance.
     *
     * 在companion對象內部，INSTANCE為數據庫聲明一個私有的可為空的變量，並將其初始化為null
     * INSTANCE創建數據庫後，該變量將保留對數據庫的引用。這可以幫助您避免重複打開與數據庫的連接，這在計算上是昂貴的。
     *
     *  註釋INSTANCE用@Volatile。volatile變量的值將永遠不會被緩存，並且所有讀寫操作都將在主內存中進行。
     *  這有助於確保的值INSTANCE始終是最新的，並且與所有執行線程相同。
     *  這意味著由一個線程進行的更改將INSTANCE立即對所有其他線程可見，並且不會出現兩個線程都更新緩存中相同實體的情況，這會造成問題。
     */
    companion object {
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        /**
         * 定義一個getInstance()方法，該方法具有Context數據庫構建器將需要的參數。返回一個類型SleepDatabase。
         *
         */
        fun getInstance(context: Context): SleepDatabase {
            // 在內部getInstance()，添加一個 已同步 synchronized{}塊。傳遞，this以便您可以訪問上下文。
            // 多個線程可能同時請求一個數據庫實例，從而導致兩個數據庫而不是一個。
            // 包裝代碼以使數據庫進入數據庫synchronized意味著一次只能有一個執行線程進入該代碼塊，從而確保數據庫僅被初始化一次。
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                // If instance is `null` make a new database instance.
                // 將所需的遷移策略添加到構建器。使用.fallbackToDestructiveMigration()。
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    // Assign INSTANCE to the newly created database.
                    INSTANCE = instance
                }
                // Return instance; smart cast to be non-null.
                return instance
            }
        }
    }


}