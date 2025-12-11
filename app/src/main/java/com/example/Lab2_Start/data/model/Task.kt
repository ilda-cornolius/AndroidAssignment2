package com.example.Lab2_Start.data.model

import java.util.UUID


 //This is the data model for a Task entity
 //it contains values such as the title, id, description and due date 
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val dueDate: Long, // Timestamp in milliseconds
    val isCompleted: Boolean = false
)


