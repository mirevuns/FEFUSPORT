package com.example.fefusport.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.fefusport.data.converters.ActivityTypeConverter

@Entity(tableName = "user_activities")
@TypeConverters(ActivityTypeConverter::class)
data class UserActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val activityType: ActivityType,
    val startTime: Long,
    val endTime: Long? = null,
    val distance: Double? = null,
    val duration: Long? = null
) 