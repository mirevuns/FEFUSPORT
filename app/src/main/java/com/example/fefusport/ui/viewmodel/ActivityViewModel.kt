package com.example.fefusport.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fefusport.data.AppDatabase
import com.example.fefusport.data.repository.ActivityRepository
import com.example.fefusport.model.ActivityFeedback
import com.example.fefusport.model.ActivityType
import com.example.fefusport.model.ActivityWithOwner
import com.example.fefusport.model.UserActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityViewModel(application: Application) : AndroidViewModel(application) {
    val repository: ActivityRepository
    val allActivities: LiveData<List<ActivityWithOwner>>

    init {
        Log.d("ActivityViewModel", "Initializing ActivityViewModel")
        val database = AppDatabase.getDatabase(application)
        repository = ActivityRepository(
            database.userActivityDao(),
            database.activityFeedbackDao(),
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

    fun updateActivityWithRealData(activityId: Long, endTime: Long, duration: Long, distance: Double) {
        Log.d("ActivityViewModel", "Updating activity $activityId with real data: endTime=$endTime, duration=$duration, distance=$distance")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val activityWithOwner = repository.getActivityById(activityId)
                if (activityWithOwner != null) {
                    val existing = activityWithOwner.activity
                    val updated = existing.copy(
                        endTime = endTime,
                        duration = duration,
                        distance = distance
                    )
                    repository.updateActivity(updated)
                    Log.d("ActivityViewModel", "Activity updated successfully: $updated")
                } else {
                    Log.e("ActivityViewModel", "Activity not found with ID: $activityId, creating new one")
                    // Если активность не найдена, возможно она была удалена, создаем новую
                }
            } catch (e: Exception) {
                Log.e("ActivityViewModel", "Error updating activity: ${e.message}", e)
                e.printStackTrace()
            }
        }
    }

    fun deleteActivity(activity: UserActivity) {
        viewModelScope.launch {
            repository.deleteActivity(activity)
        }
    }

    fun observeFeedback(activityId: Long): LiveData<List<ActivityFeedback>> =
        repository.observeFeedback(activityId)

    fun addFeedback(activityId: Long, authorName: String, authorAvatar: String?, rating: Int, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFeedback(
                ActivityFeedback(
                    activityId = activityId,
                    authorName = authorName,
                    authorAvatar = authorAvatar,
                    rating = rating,
                    comment = comment
                )
            )
        }
    }

    suspend fun hasFeedbackFromAuthor(activityId: Long, authorName: String): Boolean {
        return repository.hasFeedbackFromAuthor(activityId, authorName)
    }

    fun updateFeedbackAuthorName(oldName: String, newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFeedbackAuthorName(oldName, newName)
        }
    }
} 