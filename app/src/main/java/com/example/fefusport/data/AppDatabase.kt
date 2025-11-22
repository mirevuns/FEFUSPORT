package com.example.fefusport.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fefusport.data.converters.ActivityTypeConverter
import com.example.fefusport.data.dao.ActivityFeedbackDao
import com.example.fefusport.data.dao.UserActivityDao
import com.example.fefusport.data.dao.UserDao
import com.example.fefusport.model.ActivityFeedback
import com.example.fefusport.model.User
import com.example.fefusport.model.UserActivity

@Database(
    entities = [UserActivity::class, User::class, ActivityFeedback::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(ActivityTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userActivityDao(): UserActivityDao
    abstract fun userDao(): UserDao
    abstract fun activityFeedbackDao(): ActivityFeedbackDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fefusport_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 