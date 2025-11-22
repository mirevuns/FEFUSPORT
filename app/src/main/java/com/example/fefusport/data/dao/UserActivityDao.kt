package com.example.fefusport.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Transaction
import com.example.fefusport.model.ActivityWithOwner
import com.example.fefusport.model.UserActivity

@Dao
interface UserActivityDao {
    @Transaction
    @Query("SELECT * FROM user_activities ORDER BY startTime DESC")
    fun getAllActivities(): LiveData<List<ActivityWithOwner>>

    @Transaction
    @Query("SELECT * FROM user_activities WHERE id = :activityId")
    suspend fun getActivityById(activityId: Long): ActivityWithOwner?

    @Insert
    suspend fun insertActivity(activity: UserActivity): Long

    @Update
    suspend fun updateActivity(activity: UserActivity)

    @Delete
    suspend fun deleteActivity(activity: UserActivity)

    @Query("DELETE FROM user_activities")
    suspend fun deleteAllActivities()
} 