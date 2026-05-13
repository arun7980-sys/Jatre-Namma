package com.example.jatrenamma.model

import java.util.Date

data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val startTime: String = "",
    val location: String = "",
    val isOngoing: Boolean = false
)
