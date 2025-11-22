package com.example.fefusport.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.fefusport.data.converters.ActivityTypeConverter

@Entity(
    tableName = "user_activities",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"])
    ]
)
@TypeConverters(ActivityTypeConverter::class)
data class UserActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val activityType: ActivityType,
    val startTime: Long,
    val endTime: Long? = null,
    val distance: Double? = null,
    val duration: Long? = null
)