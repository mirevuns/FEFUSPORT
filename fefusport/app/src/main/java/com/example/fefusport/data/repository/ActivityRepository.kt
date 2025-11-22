package com.example.fefusport.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.fefusport.data.dao.UserActivityDao
import com.example.fefusport.model.UserActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ActivityRepository(
    private val userActivityDao: UserActivityDao,
) {
    val allActivities: LiveData<List<UserActivity>> = userActivityDao.getAllActivities()

    suspend fun insertActivity(activity: UserActivity): Long {
        return withContext(Dispatchers.IO) {
            val id = userActivityDao.insertActivity(activity)
            Log.d("ActivityRepository", "Activity inserted with ID: $id")
            id
        }
    }

    suspend fun updateActivity(activity: UserActivity) {
        withContext(Dispatchers.IO) {
            userActivityDao.updateActivity(activity)
            Log.d("ActivityRepository", "Activity updated: ${activity.id}")
        }
    }

    suspend fun deleteActivity(activity: UserActivity) {
        withContext(Dispatchers.IO) {
            userActivityDao.deleteActivity(activity)
        }
    }

    suspend fun getActivityById(activityId: Int): UserActivity? {
        return withContext(Dispatchers.IO) {
            userActivityDao.getActivityById(activityId)
        }
    }

} 