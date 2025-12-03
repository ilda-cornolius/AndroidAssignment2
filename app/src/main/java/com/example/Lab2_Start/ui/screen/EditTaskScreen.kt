package com.example.Lab2_Start.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.Lab2_Start.ui.viewmodel.EditTaskViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for viewing and editing an existing task.
 * Includes input fields for title, description, due date, and completion status.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditTaskViewModel = koinViewModel()
) {
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val dueDate by viewModel.dueDate.collectAsState()
    val isCompleted by viewModel.isCompleted.collectAsState()
    val saveEnabled by viewModel.saveEnabled.collectAsState()
    val task by viewModel.task.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showDatePicker by remember { mutableStateOf(false) }

    // Load task when screen is displayed
    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Task",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .semantics {
                                contentDescription = "Back button"
                            }
                            .testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (task == null) {
            // Loading state
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .semantics {
                            contentDescription = "Loading task details"
                        }
                )
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Completion status toggle
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Task completion status. Currently ${if (isCompleted) "completed" else "pending"}"
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCompleted) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Task Status",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Switch(
                            checked = isCompleted,
                            onCheckedChange = { viewModel.toggleCompletion() },
                            modifier = Modifier
                                .semantics {
                                    contentDescription = if (isCompleted) {
                                        "Mark task as pending"
                                    } else {
                                        "Mark task as completed"
                                    }
                                }
                        )
                    }
                }

                // Title input field
                OutlinedTextField(
                    value = title,
                    onValueChange = { viewModel.updateTitle(it) },
                    label = { Text("Task Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Task title input field"
                        }
                        .testTag("title_input"),
                    singleLine = true,
                    isError = title.isBlank() && title.isNotEmpty()
                )

                // Description input field
                OutlinedTextField(
                    value = description,
                    onValueChange = { viewModel.updateDescription(it) },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Task description input field"
                        }
                        .testTag("description_input"),
                    minLines = 4,
                    maxLines = 8
                )

                // Due date picker
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                OutlinedTextField(
                    value = if (dueDate != null) {
                        dateFormat.format(Date(dueDate!!))
                    } else {
                        "Select due date"
                    },
                    onValueChange = {},
                    label = { Text("Due Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Due date selector. Tap to choose a date"
                        }
                        .testTag("date_input"),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            onClick = { showDatePicker = true },
                            modifier = Modifier
                                .size(48.dp) // Minimum touch target size for accessibility
                                .semantics {
                                    contentDescription = "Open date picker"
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Calendar icon"
                            )
                        }
                    }
                )

                // Save button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.saveTask()
                            onNavigateBack()
                        }
                    },
                    enabled = saveEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp) // Minimum touch target size for accessibility
                        .semantics {
                            contentDescription = if (saveEnabled) {
                                "Save task changes button"
                            } else {
                                "Save task button disabled. Please enter a task title"
                            }
                        }
                        .testTag("save_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Save Changes",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            // Date picker dialog
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = dueDate ?: System.currentTimeMillis()
                )
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    onConfirm = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.updateDueDate(it)
                        }
                        showDatePicker = false
                    },
                    datePickerState = datePickerState
                )
            }
        }
    }
}


