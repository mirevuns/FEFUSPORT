package com.example.fefusport.data.repository

import android.util.Log
import androidx.lifecycle.LiveData

import com.example.fefusport.data.dao.ActivityFeedbackDao
import com.example.fefusport.data.dao.UserActivityDao
import com.example.fefusport.model.ActivityFeedback
import com.example.fefusport.model.ActivityWithOwner
import com.example.fefusport.model.UserActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ActivityRepository(
    private val userActivityDao: UserActivityDao,
    private val feedbackDao: ActivityFeedbackDao,
) {
    val allActivities: LiveData<List<ActivityWithOwner>> = userActivityDao.getAllActivities()

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

    suspend fun getActivityById(activityId: Long): ActivityWithOwner? =
        withContext(Dispatchers.IO) { userActivityDao.getActivityById(activityId) }

    fun observeFeedback(activityId: Long): LiveData<List<ActivityFeedback>> =
        feedbackDao.observeFeedback(activityId)

    suspend fun addFeedback(feedback: ActivityFeedback): Long =
        withContext(Dispatchers.IO) { feedbackDao.insertFeedback(feedback) }

    suspend fun getAverageRating(activityId: Long): Double? =
        withContext(Dispatchers.IO) { feedbackDao.getAverageRating(activityId) }

    suspend fun getFeedbackCount(activityId: Long): Int =
        withContext(Dispatchers.IO) { feedbackDao.getFeedbackCount(activityId) }

    suspend fun hasFeedbackFromAuthor(activityId: Long, authorName: String): Boolean =
        withContext(Dispatchers.IO) { feedbackDao.getFeedbackByAuthor(activityId, authorName) != null }

    suspend fun updateFeedbackAuthorName(oldName: String, newName: String) =
        withContext(Dispatchers.IO) { feedbackDao.updateAuthorName(oldName, newName) }

    suspend fun updateFeedbackAuthorAvatar(authorName: String, newAvatar: String?) =
        withContext(Dispatchers.IO) { feedbackDao.updateAuthorAvatar(authorName, newAvatar) }
}