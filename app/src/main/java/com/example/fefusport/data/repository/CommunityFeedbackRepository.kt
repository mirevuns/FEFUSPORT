package com.example.fefusport.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fefusport.model.UiFeedback

object CommunityFeedbackRepository {

    private val feedbackStorage = mutableMapOf<String, MutableLiveData<List<UiFeedback>>>()

    fun observeFeedback(activityId: String): LiveData<List<UiFeedback>> {
        return feedbackStorage.getOrPut(activityId) { MutableLiveData(emptyList()) }
    }

    fun addFeedback(activityId: String, feedback: UiFeedback) {
        val liveData = feedbackStorage.getOrPut(activityId) { MutableLiveData(emptyList()) }
        val current = liveData.value.orEmpty().toMutableList()
        current.add(0, feedback)
        liveData.postValue(current)
    }

    fun hasFeedbackFromAuthor(activityId: String, authorName: String): Boolean {
        val liveData = feedbackStorage[activityId]
        return liveData?.value?.any { it.authorName == authorName } ?: false
    }

    fun updateAuthorName(oldName: String, newName: String) {
        feedbackStorage.values.forEach { liveData ->
            val current = liveData.value.orEmpty().toMutableList()
            var updated = false
            current.forEachIndexed { index, feedback ->
                if (feedback.authorName == oldName) {
                    current[index] = feedback.copy(authorName = newName)
                    updated = true
                }
            }
            if (updated) {
                liveData.postValue(current)
            }
        }
    }

    fun updateAuthorAvatar(authorName: String, newAvatar: String?) {
        feedbackStorage.values.forEach { liveData ->
            val current = liveData.value.orEmpty().toMutableList()
            var updated = false
            current.forEachIndexed { index, feedback ->
                if (feedback.authorName == authorName) {
                    current[index] = feedback.copy(authorAvatar = newAvatar)
                    updated = true
                }
            }
            if (updated) {
                liveData.postValue(current)
            }
        }
    }
}

