package com.example.Lab2_Start.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Lab2_Start.data.model.Task
import com.example.Lab2_Start.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Edit Task screen.
 * Manages the state of task editing form and handles updating tasks.
 */
class EditTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _dueDate = MutableStateFlow<Long?>(null)
    val dueDate: StateFlow<Long?> = _dueDate.asStateFlow()

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    private val _saveEnabled = MutableStateFlow(false)
    val saveEnabled: StateFlow<Boolean> = _saveEnabled.asStateFlow()

    /**
     * Loads a task by ID from the repository.
     * @param taskId The ID of the task to load
     */
    fun loadTask(taskId: String) {
        viewModelScope.launch {
            val loadedTask = taskRepository.getTaskById(taskId)
            if (loadedTask != null) {
                _task.value = loadedTask
                _title.value = loadedTask.title
                _description.value = loadedTask.description
                _dueDate.value = loadedTask.dueDate
                _isCompleted.value = loadedTask.isCompleted
                updateSaveEnabled()
            }
        }
    }

    /**
     * Updates the task title.
     * @param newTitle The new title value
     */
    fun updateTitle(newTitle: String) {
        _title.value = newTitle
        updateSaveEnabled()
    }

    /**
     * Updates the task description.
     * @param newDescription The new description value
     */
    fun updateDescription(newDescription: String) {
        _description.value = newDescription
        updateSaveEnabled()
    }

    /**
     * Updates the task due date.
     * @param timestamp The due date as a timestamp in milliseconds
     */
    fun updateDueDate(timestamp: Long) {
        _dueDate.value = timestamp
        updateSaveEnabled()
    }

    /**
     * Toggles the completion status of the task.
     */
    fun toggleCompletion() {
        _isCompleted.value = !_isCompleted.value
    }

    /**
     * Determines if the save button should be enabled.
     * Save is enabled when title is not empty and task is loaded.
     */
    private fun updateSaveEnabled() {
        _saveEnabled.value = _title.value.isNotBlank() && _task.value != null
    }

    /**
     * Saves the updated task to the repository.
     * @return true if the task was saved successfully, false otherwise
     */
    suspend fun saveTask(): Boolean {
        val currentTask = _task.value ?: return false
        if (_title.value.isBlank()) {
            return false
        }
        val updatedTask = currentTask.copy(
            title = _title.value.trim(),
            description = _description.value.trim(),
            dueDate = _dueDate.value ?: System.currentTimeMillis(),
            isCompleted = _isCompleted.value
        )
        taskRepository.updateTask(updatedTask)
        return true
    }
}


