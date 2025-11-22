package com.example.fefusport.model

import androidx.room.Embedded
import androidx.room.Relation

data class ActivityWithOwner(
    @Embedded val activity: UserActivity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val owner: User
)

