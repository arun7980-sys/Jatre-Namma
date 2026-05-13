package com.example.jatrenamma.model

data class LostItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val contactNumber: String = "",
    val isResolved: Boolean = false,
    val type: String = "Lost", // "Lost" or "Found"
    val timestamp: Long = System.currentTimeMillis()
)
