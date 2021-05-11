package com.example.trackmysleepquality

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.trackmysleepquality.database.SleepDatabase
import com.example.trackmysleepquality.database.SleepDatabaseDAO
import com.example.trackmysleepquality.database.SleepNight
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

// @RunWith 註釋識別測試運行，這是程序，建立和執行測試
@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {
    private lateinit var sleepDAO: SleepDatabaseDAO
    private lateinit var db: SleepDatabase

    // 在安裝過程中，與註釋的功能@Before被執行，它創建一個內存SleepDatabase與所述SleepDatabaseDao。
    // “內存中”表示該數據庫未保存在文件系統上，並且在測試運行後將被刪除。
    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
            // Allowing main thread queries, just for testing.
            // 默認情況下，如果嘗試在主線程上運行查詢，則會收到錯誤消息。此方法使您可以在主線程上運行測試，這僅應在測試期間進行。
            .allowMainThreadQueries()
            .build()
        sleepDAO = db.sleepDatabaseDAO
    }

    /**
     * 在以註釋的測試方法中@Test，您創建，插入和檢索SleepNight，並斷言它們是相同的。
     * 如果有任何問題，請拋出異常。在實際測試中，您將有多種@Test方法。
     */
    @Test
    @Throws(Exception::class)
    fun insertAndGetNight(){
        val night = SleepNight()
        sleepDAO.insert(night)
        val tonight = sleepDAO.getTonight()
        assertEquals(tonight?.sleepQuality, -1)
    }

    // 測試完成後，帶有註釋的函數將@After執行以關閉數據庫。
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}