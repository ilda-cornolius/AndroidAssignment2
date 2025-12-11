package com.example.Lab2_Start.data.repository

import com.example.Lab2_Start.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



 //This is the interface for the TaskRepository
 //it contains methods to retrieve, add, update, and delete tasks
interface TaskRepository {
    fun getAllTasks(): StateFlow<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: String)
    suspend fun getTaskById(taskId: String): Task?
}




class TaskRepositoryImpl : TaskRepository {

    //Stores an empty list of tasks in the _tasks variable
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    //Returns the list of tasks as a StateFlow in order to observe data changes
    //It also updates the user interface automatically
    override fun getAllTasks(): StateFlow<List<Task>> = _tasks.asStateFlow()

//This method adds a task to the list of tasks by appending it to the existing list
    override suspend fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
    }

    //updateTask method updates a task in the list by matching the id
    override suspend fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
    }

    //the deleteTask method deletes a task by searching its' id
    override suspend fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    //This method retrieves a task from the list of tasks by searching for the task with the same id
    override suspend fun getTaskById(taskId: String): Task? {
        return _tasks.value.find { it.id == taskId }
    }
}


