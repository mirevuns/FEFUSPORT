package com.example.fefusport.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fefusport.model.UserActivity

@Dao
interface UserActivityDao {
    @Query("SELECT * FROM user_activities ORDER BY startTime DESC")
    fun getAllActivities(): LiveData<List<UserActivity>>

    @Query("SELECT * FROM user_activities WHERE id = :activityId")
    suspend fun getActivityById(activityId: Int): UserActivity?

    @Insert
    suspend fun insertActivity(activity: UserActivity): Long

    @Update
    suspend fun updateActivity(activity: UserActivity)

    @Delete
    suspend fun deleteActivity(activity: UserActivity)

    @Query("DELETE FROM user_activities")
    suspend fun deleteAllActivities()
} 