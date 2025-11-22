package com.example.fefusport.model

data class UiFeedback(
    val authorName: String,
    val authorAvatar: String? = null,
    val rating: Int,
    val comment: String,
    val createdAt: Long
)

