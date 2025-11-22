package com.example.fefusport.model

enum class ActivityType(val displayName: String, val emoji: String) {
    RUNNING("Бег", "🏃"),
    SWIMMING("Плавание", "🏊"),
    CYCLING("Велоспорт", "🚴"),
    JUMPING("Прыжки", "🤸"),
    WALKING("Ходьба", "🚶");

    fun getFullName(): String = "$displayName $emoji"
} 