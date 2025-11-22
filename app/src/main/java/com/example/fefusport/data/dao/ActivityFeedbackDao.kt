package com.example.fefusport.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fefusport.model.ActivityFeedback

@Dao
interface ActivityFeedbackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedback(feedback: ActivityFeedback): Long

    @Query("SELECT * FROM activity_feedback WHERE activityId = :activityId ORDER BY createdAt DESC")
    fun observeFeedback(activityId: Long): LiveData<List<ActivityFeedback>>

    @Query("SELECT AVG(rating) FROM activity_feedback WHERE activityId = :activityId")
    suspend fun getAverageRating(activityId: Long): Double?

    @Query("SELECT COUNT(*) FROM activity_feedback WHERE activityId = :activityId")
    suspend fun getFeedbackCount(activityId: Long): Int

    @Query("SELECT * FROM activity_feedback WHERE activityId = :activityId AND authorName = :authorName LIMIT 1")
    suspend fun getFeedbackByAuthor(activityId: Long, authorName: String): ActivityFeedback?

    @Query("UPDATE activity_feedback SET authorName = :newName WHERE authorName = :oldName")
    suspend fun updateAuthorName(oldName: String, newName: String)

    @Query("UPDATE activity_feedback SET authorAvatar = :newAvatar WHERE authorName = :authorName")
    suspend fun updateAuthorAvatar(authorName: String, newAvatar: String?)
}

