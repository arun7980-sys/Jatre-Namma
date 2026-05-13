package com.example.jatrenamma.model

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val startTime: String = "",
    val location: String = "",
    val date: String = "", // e.g., "2025-01-20"
    val isOngoing: Boolean = false,
    val timestamp: Long = 0
)
