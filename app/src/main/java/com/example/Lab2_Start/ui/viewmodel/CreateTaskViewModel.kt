package com.example.Lab2_Start.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Lab2_Start.data.model.Task
import com.example.Lab2_Start.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


 //The ViewModel class for the Create Task screen
class CreateTaskViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    
    //variable to store the title of the task 
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    //variable to store the description of the task
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    //variable to store the due date of the task
    private val _dueDate = MutableStateFlow<Long?>(null)
    val dueDate: StateFlow<Long?> = _dueDate.asStateFlow()

    //variable to store the save enabled state of the task
    private val _saveEnabled = MutableStateFlow(false)
    val saveEnabled: StateFlow<Boolean> = _saveEnabled.asStateFlow()

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

    //method used to update the save enabled state of the task
    //Save is enabled when the title of the task is not empty.
    private fun updateSaveEnabled() {
        _saveEnabled.value = _title.value.isNotBlank()
    }

    //function used to save the task to the repository.
    suspend fun saveTask(): Boolean {
        //If the title of the task is empty, return false
        if (_title.value.isBlank()) {
            return false
        }
        //Create a new task with the stored title, description and due date values
        val task = Task(
            title = _title.value.trim(),
            description = _description.value.trim(),
            dueDate = _dueDate.value ?: System.currentTimeMillis()
        )
        //Adding the new task to the repository
        taskRepository.addTask(task)
        //Resetting the form to initial state
        resetForm()
        //Return true if the task was saved successfully
        return true
    }

    //method used to reset the form to initial state
    private fun resetForm() {
        _title.value = ""
        _description.value = ""
        _dueDate.value = null
        _saveEnabled.value = false
    }
}


