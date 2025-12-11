package com.example.Lab2_Start.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.Lab2_Start.data.model.Task
import com.example.Lab2_Start.data.repository.TaskRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


 //This the ViewModel class for the Home screen
 //It manages the list of tasks in the database and handles task-related methods
class HomeViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    //StateFlow object containing a list of all tasks
    val tasks: StateFlow<List<Task>> = taskRepository.getAllTasks()

    //function used to delete a task from the repository
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            taskRepository.deleteTask(taskId)
        }
    }

    //method used to toggle the completion status of a task
    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
}


