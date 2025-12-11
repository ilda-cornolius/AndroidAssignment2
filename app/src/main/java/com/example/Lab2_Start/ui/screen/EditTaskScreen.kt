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



 //This is a composable ui function for the edit task screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    //The taskId of the task to be edited
    taskId: String,
    //Creates a listener for when the user presses the back button
    onNavigateBack: () -> Unit,
    //Creates a modifier for the edit task screen
    modifier: Modifier = Modifier,
    //Creates a viewModel for the edit task screen
    viewModel: EditTaskViewModel = koinViewModel()
) {
    //Variables to store data from the viewModel
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
        //This is the topbar ui for the edit task screen
        topBar = {
            TopAppBar(
                title = {
                    //Setting the title text of the topbar
                    Text(   
                        text = "Edit Task",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                //The back button icon in the topbar
                navigationIcon = {
                    //Sets an onClick listender for the back button
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
            //This is the loading state ui for the edit task screen
            //It is displayed when the task is not loaded yet

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
            //This is the main content ui for the edit task screen
            //It is displayed when the task is loaded
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // This is the completion status toggle ui for the edit task screen
                //It is displayed when the task is loaded

                //Card ui properties to display the completion status of the task
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
                        //Task status text
                        Text(
                            text = "Task Status",
                            style = MaterialTheme.typography.titleMedium
                        )
                        //A toggle switch to be clicked when the task is completed
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

                
                //This is the input text field for the title of the task
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

               //The input text field for the description of the task
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

                // The due date picker field for the task
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
                        //An icon button to open the date picker dialog box when the user clicks the due date icon
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

                // The save button used to save the task to the database
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

            // Date picker dialog box to select the due date of the task
            if (showDatePicker) {
                val datePickerState = rememberDatePickerState(
                    //The initial selected date of the date picker
                    initialSelectedDateMillis = dueDate ?: System.currentTimeMillis()
                )
                DatePickerDialog(
                    //A listener to close the date picker dialog box when the user clicks the cancel button
                    onDismissRequest = { showDatePicker = false },
                    //A listener to save the selected date to the database
                    onConfirm = {
                        //A listener to save the selected date to the database
                        datePickerState.selectedDateMillis?.let {
                            //A listener to update the due date of the task
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


