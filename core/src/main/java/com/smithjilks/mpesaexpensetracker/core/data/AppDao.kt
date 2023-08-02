package com.smithjilks.mpesaexpensetracker.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smithjilks.mpesaexpensetracker.core.model.Record
import com.smithjilks.mpesaexpensetracker.core.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // User queries
    @Query("SELECT * FROM user_table LIMIT 1")
    fun getUser(): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM user_table")
    suspend fun deleteUserData()


    // Records queries
    @Query("SELECT * FROM records_table")
    fun getAllRecords(): Flow<List<Record>>

    @Query("SELECT * FROM records_table ORDER BY id DESC LIMIT 10")
    fun getRecentRecords(): Flow<List<Record>>

    @Query("SELECT * FROM records_table WHERE recordType =:recordType")
    fun getAllRecordsByRecordType(recordType: String): Flow<List<Record>>

    @Query("SELECT * FROM records_table where id =:id")
    suspend fun getRecordById(id: Int): Record

    @Query("SELECT * FROM records_table where transactionRef =:transactionRef")
    suspend fun getRecordByTransactionRef(transactionRef: String): Record

    @Query(
        """ SELECT * FROM records_table
            WHERE (:recordType IS NULL OR recordType LIKE :recordType)
            AND (:category IS NULL OR category = :category)
            AND (:startDate IS NULL OR timestamp >= :startDate)
            AND (:endDate IS NULL OR timestamp <= :endDate)
            ORDER BY 
            CASE WHEN :byHighestAmount = 1 THEN amount END DESC,
            CASE WHEN :byHighestAmount = 0 THEN amount END ASC,
            CASE WHEN :byNewest = 1 THEN id END DESC,
            CASE WHEN :byNewest = 0 THEN id END ASC
            """
    )
    fun getFilteredRecords(
        recordType: String? = null,
        category: String? = null,
        startDate: Long? = null,
        endDate: Long? = null,
        byHighestAmount: Boolean = false,
        byNewest: Boolean = false
    ): Flow<List<Record>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecords(records: List<Record>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecord(record: Record)

    @Query("DELETE FROM records_table")
    suspend fun deleteAllRecords()

    @Delete
    suspend fun deleteRecord(record: Record)


}


