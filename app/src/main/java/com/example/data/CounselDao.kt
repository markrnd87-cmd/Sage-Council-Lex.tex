package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CounselDao {
    @Query("SELECT * FROM counsel_sessions ORDER BY timestampMillis DESC")
    fun getAllSessions(): Flow<List<CounselSessionEntity>>

    @Query("SELECT COUNT(*) FROM counsel_sessions")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: CounselSessionEntity): Long

    @Update
    suspend fun updateSession(session: CounselSessionEntity)

    @Query("DELETE FROM counsel_sessions WHERE id = :id")
    suspend fun deleteSessionById(id: Long)
}
