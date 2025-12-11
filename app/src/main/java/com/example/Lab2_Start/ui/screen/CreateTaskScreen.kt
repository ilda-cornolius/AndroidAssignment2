package com.example.Lab2_Start.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.Lab2_Start.ui.viewmodel.CreateTaskViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*


 //This is a composable ui function for the create task screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    //Creates a listener for when the user presses the back button
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateTaskViewModel = koinViewModel()
) {
    //Variables to store data from the viewModel
    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val dueDate by viewModel.dueDate.collectAsState()
    val saveEnabled by viewModel.saveEnabled.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    //Variable to store the state of the date picker
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            //The topbar ui for the create task screen
            TopAppBar(
                title = {
                    //Setting the title text of the topbar
                    Text(
                        text = "Create Task",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                //This is back button icon in the topbar
                navigationIcon = {
                    IconButton(
                        //Sets an onClick listender for the back button
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // This is a input field for the title of the task
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

            // This is the dscription text input field
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

            // This is the due date picker input field 
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

            // This is the save button to save the task to the database
            Button(
                onClick = {
                    coroutineScope.launch {
                        //contains a onclick listener to save the task to the database
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
                            "Save task button"
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
                //setting the text of the save button
                Text(
                    text = "Save Task",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Opens the date picker calender dialog box when the user clicks the due date icon
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



 //This is the jetpack compose function for the date picker dialog box
 //ExperimentalMatieral3Api is required to use a Card object with onClick functionality
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier
) {
    //Setting up the Alert Dialog box functionality
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Select Due Date",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            DatePicker(state = datePickerState)
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                modifier = Modifier
                    .height(48.dp) //Setting the height of the confirm button
                    .semantics {
                        contentDescription = "Confirm date selection" //Setting the content description of the confirm button
                    }
            ) {
                Text("OK") //Setting the text of the confirm button
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .height(48.dp) //Setting the height of the dismiss button
                    .semantics {
                        contentDescription = "Cancel date selection" //Setting the content description of the dismiss button
                    }
            ) {
                Text("Cancel") //Setting the text of the dismiss button
            }
        },
        modifier = modifier
    )
}
