package com.example.fefusport.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fefusport.data.AppDatabase
import com.example.fefusport.data.repository.ActivityRepository
import com.example.fefusport.data.repository.CommunityFeedbackRepository
import com.example.fefusport.data.repository.UserRepository
import com.example.fefusport.model.User
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    private val activityRepository: ActivityRepository
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        val database = AppDatabase.getDatabase(application)
        repository = UserRepository(database.userDao())
        activityRepository = ActivityRepository(
            database.userActivityDao(),
            database.activityFeedbackDao()
        )
    }

    fun register(user: User, onResult: (Result<Long>) -> Unit) {
        viewModelScope.launch {
            try {
                val id = repository.registerUser(user)
                onResult(Result.success(id))
            } catch (t: Throwable) {
                onResult(Result.failure(t))
            }
        }
    }

    fun login(login: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.authenticate(login, password)
            onResult(user)
        }
    }

    fun observeUser(userId: Long): LiveData<User?> = repository.observeUser(userId)

    fun updateUser(user: User, oldUser: User? = null, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val oldName = oldUser?.nickname
                val newName = user.nickname
                val newAvatar = user.avatarUri
                
                repository.updateUser(user)
                
                // Обновляем имя в отзывах, если оно изменилось
                if (oldName != null && oldName != newName) {
                    activityRepository.updateFeedbackAuthorName(oldName, newName)
                    CommunityFeedbackRepository.updateAuthorName(oldName, newName)
                }
                
                // Обновляем аватар в отзывах для текущего пользователя
                if (newName.isNotEmpty()) {
                    activityRepository.updateFeedbackAuthorAvatar(newName, newAvatar)
                    CommunityFeedbackRepository.updateAuthorAvatar(newName, newAvatar)
                }
                
                onResult(true)
            } catch (t: Throwable) {
                onResult(false)
            }
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Error(val error: Throwable) : AuthState()
    }
}

