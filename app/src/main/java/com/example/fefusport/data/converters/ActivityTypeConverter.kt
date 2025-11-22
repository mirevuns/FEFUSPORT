package com.example.fefusport.data.converters

import androidx.room.TypeConverter
import com.example.fefusport.model.ActivityType

class ActivityTypeConverter {
    @TypeConverter
    fun fromActivityType(activityType: ActivityType): String {
        return activityType.name
    }

    @TypeConverter
    fun toActivityType(value: String): ActivityType {
        return try {
            ActivityType.valueOf(value)
        } catch (e: IllegalArgumentException) {
            // Если значение не найдено (старые данные), возвращаем значение по умолчанию
            android.util.Log.e("ActivityTypeConverter", "Unknown activity type: $value, using RUNNING as default")
            ActivityType.RUNNING
        }
    }
} 