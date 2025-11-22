package com.example.fefusport.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "activity_feedback",
    foreignKeys = [
        ForeignKey(
            entity = UserActivity::class,
            parentColumns = ["id"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["activityId"])
    ]
)
data class ActivityFeedback(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val activityId: Long,
    val authorName: String,
    val authorAvatar: String? = null,
    val rating: Int,
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)

