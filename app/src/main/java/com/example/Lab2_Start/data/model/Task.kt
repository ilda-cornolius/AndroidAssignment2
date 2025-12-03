package com.example.Lab2_Start.data.model

import java.util.UUID

/**
 * Data model representing a Task entity.
 * Contains task details including title, description, due date, and completion status.
 */
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val dueDate: Long, // Timestamp in milliseconds
    val isCompleted: Boolean = false
)


