package com.example.Lab2_Start.di

import com.example.Lab2_Start.data.repository.TaskRepository
import com.example.Lab2_Start.data.repository.TaskRepositoryImpl
import com.example.Lab2_Start.ui.viewmodel.CreateTaskViewModel
import com.example.Lab2_Start.ui.viewmodel.EditTaskViewModel
import com.example.Lab2_Start.ui.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



 //This class instantiates the Koin dependency injection module
val appModule = module {
    // Repository
    //This is a singleton instance of the TaskRepositoryImpl class
    single<TaskRepository> { TaskRepositoryImpl() }

    // ViewModels
    //The following are instances of viewModels to be injected into the app when needed
    viewModel { HomeViewModel(get()) }
    viewModel { CreateTaskViewModel(get()) }
    viewModel { EditTaskViewModel(get()) }
}


