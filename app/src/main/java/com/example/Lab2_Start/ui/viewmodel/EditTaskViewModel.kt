package com.example.Lab2_Start.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Lab2_Start.data.model.Task
import com.example.Lab2_Start.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


 //The ViewModel class for the Edit Task screen
class EditTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    //variable to store the task object
    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    //variable to store the title of the task
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    //variable to store the description of the task
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    //variable to store the due date of the task
    private val _dueDate = MutableStateFlow<Long?>(null)
    val dueDate: StateFlow<Long?> = _dueDate.asStateFlow()

    //variable to store the completion status of the task
    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    //variable to store the save enabled state of the task
    private val _saveEnabled = MutableStateFlow(false)
    val saveEnabled: StateFlow<Boolean> = _saveEnabled.asStateFlow()

    //method used to load a task by ID from the repository
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

    //method used to update the title of the task
    fun updateTitle(newTitle: String) {
        _title.value = newTitle
        updateSaveEnabled()
    }

    //method used to update the description of the task
    fun updateDescription(newDescription: String) {
        _description.value = newDescription
        updateSaveEnabled()
    }

    //method used to update the due date of the task
    fun updateDueDate(timestamp: Long) {
        _dueDate.value = timestamp
        updateSaveEnabled()
    }

    //method used to toggle the completion status of the task
    fun toggleCompletion() {
        _isCompleted.value = !_isCompleted.value
    }

    //method used to update the save enabled state of the task
    //Save is enabled when the title of the task is not empty and the task is loaded
    private fun updateSaveEnabled() {
        _saveEnabled.value = _title.value.isNotBlank() && _task.value != null
    }

    //function used to save the updated task to the repository.
    //returns true if the task was saved successfully, false otherwise
    suspend fun saveTask(): Boolean {
        //If the task is not loaded, return false
        val currentTask = _task.value ?: return false
        //If the title of the task is empty, return false
        if (_title.value.isBlank()) {
            return false
        }
        //Creates an updated copy of the current task with the updated values from the form
        val updatedTask = currentTask.copy(
            title = _title.value.trim(),
            description = _description.value.trim(),
            dueDate = _dueDate.value ?: System.currentTimeMillis(),
            isCompleted = _isCompleted.value
        )
        //Update the task in the repository
        taskRepository.updateTask(updatedTask)
        //Return true if the task was saved successfully
        return true
    }
}


