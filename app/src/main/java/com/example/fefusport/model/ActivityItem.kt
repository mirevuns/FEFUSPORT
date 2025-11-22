package com.example.fefusport.model

sealed class ActivityItem {
    data class Activity(
        val id: String,
        val ownerName: String,
        val ownerAvatar: String?,
        val distance: String,
        val duration: String,
        val type: String,
        val timeAgo: String,
        val startTime: String,
        val endTime: String,
        val isMyActivity: Boolean,
        val isCommunity: Boolean = false
    ) : ActivityItem()
}
