package com.id.destinasyik.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.id.destinasyik.data.local.dao.UserProfileDao
import com.id.destinasyik.data.local.entity.UserProfile
import android.content.Context

@Database(entities = [UserProfile::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "user_profile_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

