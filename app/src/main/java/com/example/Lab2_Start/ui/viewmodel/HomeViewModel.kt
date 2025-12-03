package com.example.Lab2_Start.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Lab2_Start.data.model.Task
import com.example.Lab2_Start.data.repository.TaskRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen.
 * Manages the list of tasks and handles task-related operations.
 */
class HomeViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    /**
     * StateFlow containing the list of all tasks.
     * Observes data changes from the repository.
     */
    val tasks: StateFlow<List<Task>> = taskRepository.getAllTasks()

    /**
     * Deletes a task from the repository.
     * @param taskId The ID of the task to delete
     */
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }

    /**
     * Toggles the completion status of a task.
     * @param task The task to toggle
     */
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
}


