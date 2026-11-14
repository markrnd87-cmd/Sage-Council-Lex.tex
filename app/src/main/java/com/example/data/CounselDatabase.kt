package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CounselSessionEntity::class], version = 1, exportSchema = false)
abstract class CounselDatabase : RoomDatabase() {
    abstract fun counselDao(): CounselDao

    companion object {
        @Volatile
        private var INSTANCE: CounselDatabase? = null

        fun getInstance(context: Context): CounselDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CounselDatabase::class.java,
                    "sage_counsel_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
