package com.example.Lab2_Start.data.repository

import com.example.Lab2_Start.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository interface for managing tasks.
 * Provides methods to retrieve, add, update, and delete tasks.
 */
interface TaskRepository {
    fun getAllTasks(): StateFlow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: String)
    suspend fun getTaskById(taskId: String): Task?
}

/**
 * Implementation of TaskRepository using in-memory storage.
 * Uses StateFlow to observe data changes.
 */
class TaskRepositoryImpl : TaskRepository {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    override fun getAllTasks(): StateFlow<List<Task>> = _tasks.asStateFlow()

    override suspend fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
    }

    override suspend fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
    }

    override suspend fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    override suspend fun getTaskById(taskId: String): Task? {
        return _tasks.value.find { it.id == taskId }
    }
}


