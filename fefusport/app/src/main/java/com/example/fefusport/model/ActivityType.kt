package com.example.fefusport.model

enum class ActivityType(val displayName: String, val emoji: String) {
    BICYCLE("Велосипед", "🚲"),
    RUNNING("Бег", "🏃"),
    SWIMMING("Плавание", "🏊"),
    WORKOUT("Тренировка", "💪"),
    WALKING("Ходьба", "🚶");

    fun getFullName(): String = "$displayName $emoji"
} 