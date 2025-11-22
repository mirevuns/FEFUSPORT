package com.example.fefusport.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fefusport.data.AppDatabase
import com.example.fefusport.data.repository.ActivityRepository
import com.example.fefusport.model.ActivityType
import com.example.fefusport.model.UserActivity
import kotlinx.coroutines.launch

class ActivityViewModel(application: Application) : AndroidViewModel(application) {
    val repository: ActivityRepository
    val allActivities: LiveData<List<UserActivity>>

    init {
        Log.d("ActivityViewModel", "Initializing ActivityViewModel")
        val database = AppDatabase.getDatabase(application)
        repository = ActivityRepository(
            database.userActivityDao(),
        )
        allActivities = repository.allActivities
        Log.d("ActivityViewModel", "ActivityViewModel initialized successfully")
    }

    fun insertActivity(activity: UserActivity, onComplete: (Long) -> Unit) {
        Log.d("ActivityViewModel", "Inserting activity: ${activity.activityType.getFullName()}")
        viewModelScope.launch {
            try {
                val activityId = repository.insertActivity(activity)
                Log.d("ActivityViewModel", "Activity inserted with ID: $activityId")
                onComplete(activityId)
            } catch (e: Exception) {
                Log.e("ActivityViewModel", "Error inserting activity", e)
                onComplete(-1)
            }
        }
    }

    fun updateActivityWithRealData(activityId: Int, endTime: Long, duration: Long, distance: Double) {
        Log.d("ActivityViewModel", "Updating activity $activityId with real data")
        viewModelScope.launch {
            try {
                val activity = repository.getActivityById(activityId)
                activity?.let {
                    val updatedActivity = it.copy(
                        endTime = endTime,
                        duration = duration,
                        distance = distance
                    )
                    repository.updateActivity(updatedActivity)
                    Log.d("ActivityViewModel", "Activity updated successfully")
                }
            } catch (e: Exception) {
                Log.e("ActivityViewModel", "Error updating activity", e)
            }
        }
    }

    fun deleteActivity(activity: UserActivity) {
        viewModelScope.launch {
            repository.deleteActivity(activity)
        }
    }
} 