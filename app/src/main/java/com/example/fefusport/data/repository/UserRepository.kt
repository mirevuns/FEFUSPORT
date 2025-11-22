package com.example.fefusport.data.repository

import androidx.lifecycle.LiveData
import com.example.fefusport.data.dao.UserDao
import com.example.fefusport.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: User): Long = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }

    suspend fun authenticate(login: String, password: String): User? = withContext(Dispatchers.IO) {
        val user = userDao.getUserByLogin(login)
        if (user != null && user.password == password) user else null
    }

    suspend fun getUserById(userId: Long): User? = withContext(Dispatchers.IO) {
        userDao.getUserById(userId)
    }

    fun observeUser(userId: Long): LiveData<User?> = userDao.observeUser(userId)

    suspend fun updateUser(user: User) = withContext(Dispatchers.IO) {
        userDao.updateUser(user)
    }
}

