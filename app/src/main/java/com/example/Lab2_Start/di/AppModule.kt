package com.example.Lab2_Start.di

import com.example.Lab2_Start.data.repository.TaskRepository
import com.example.Lab2_Start.data.repository.TaskRepositoryImpl
import com.example.Lab2_Start.ui.viewmodel.CreateTaskViewModel
import com.example.Lab2_Start.ui.viewmodel.EditTaskViewModel
import com.example.Lab2_Start.ui.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin dependency injection module.
 * Provides all necessary dependencies for the app including repositories and ViewModels.
 */
val appModule = module {
    // Repository
    single<TaskRepository> { TaskRepositoryImpl() }

    // ViewModels
    viewModel { HomeViewModel(get()) }
    viewModel { CreateTaskViewModel(get()) }
    viewModel { EditTaskViewModel(get()) }
}


