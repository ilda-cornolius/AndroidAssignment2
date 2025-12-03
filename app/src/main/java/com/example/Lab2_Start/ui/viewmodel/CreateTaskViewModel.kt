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
 * ViewModel for the Create Task screen.
 * Manages the state of task creation form and handles saving tasks.
 */
class CreateTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    // UI state for task creation form
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _dueDate = MutableStateFlow<Long?>(null)
    val dueDate: StateFlow<Long?> = _dueDate.asStateFlow()

    private val _saveEnabled = MutableStateFlow(false)
    val saveEnabled: StateFlow<Boolean> = _saveEnabled.asStateFlow()

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
     * Determines if the save button should be enabled.
     * Save is enabled when title is not empty.
     */
    private fun updateSaveEnabled() {
        _saveEnabled.value = _title.value.isNotBlank()
    }

    /**
     * Saves the task to the repository.
     * @return true if the task was saved successfully, false otherwise
     */
    suspend fun saveTask(): Boolean {
        if (_title.value.isBlank()) {
            return false
        }
        val task = Task(
            title = _title.value.trim(),
            description = _description.value.trim(),
            dueDate = _dueDate.value ?: System.currentTimeMillis()
        )
        taskRepository.addTask(task)
        resetForm()
        return true
    }

    /**
     * Resets the form to initial state.
     */
    private fun resetForm() {
        _title.value = ""
        _description.value = ""
        _dueDate.value = null
        _saveEnabled.value = false
    }
}


