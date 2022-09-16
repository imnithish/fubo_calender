/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.fubocalender.ui.screens.add

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fubocalender.R
import com.example.fubocalender.util.formatDate
import com.example.fubocalender.util.formatTime
import com.example.fubocalender.util.toast
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

enum class PickerType {
    DATE, TIME
}

enum class TimeType {
    START, END
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddScreen(
    navController: NavController,
    addScreenVM: AddScreenVM = hiltViewModel()
) {

    val keyboardController = LocalSoftwareKeyboardController.current


    var eventName by remember {
        mutableStateOf("")
    }
    var startTime by remember {
        mutableStateOf("")
    }
    var endTime by remember {
        mutableStateOf("")
    }
    var eventDate by remember {
        mutableStateOf("")
    }
    var pickerType by remember {
        mutableStateOf(PickerType.DATE)
    }
    var timeType by remember {
        mutableStateOf(TimeType.START)
    }

    val context = LocalContext.current

    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        when (pickerType) {
            PickerType.DATE -> {
                datepicker { date ->
                    eventDate = date.formatDate()
                }
            }
            PickerType.TIME -> {
                timepicker { time ->
                    val formatted = time.formatTime()
                    when (timeType) {
                        TimeType.START -> {
                            if (endTime != "") {
                                if (addScreenVM.isStartTimeAfterEndTime(
                                        formatted,
                                        endTime,
                                        eventDate
                                    )
                                )
                                    context.toast("Start time is after ending time")
                                else
                                    startTime = time.formatTime()
                            } else
                                startTime = time.formatTime()
                        }
                        TimeType.END -> {
                            if (startTime != "") {
                                if (addScreenVM.isEndTimeBeforeStartTime(
                                        formatted,
                                        startTime,
                                        eventDate
                                    )
                                )
                                    context.toast("End time is before start time")
                                else
                                    endTime = time.formatTime()
                            } else
                                endTime = time.formatTime()
                        }
                    }
                }
            }
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Add Event")
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primary,
                onClick = {
                    if (startTime.isEmpty() || endTime.isEmpty() || eventDate.isEmpty())
                        context.toast("Please enter all event details")
                    else {
                        addScreenVM.saveEvent(
                            eventName,
                            eventDate,
                            startTime,
                            endTime
                        ) {
                            it?.let {
                                context.toast(it)
                            } ?: run {
                                context.toast("Event saved")
                                navController.popBackStack()
                            }
                        }
                    }
                }
            ) {
                Icon(Icons.Filled.Save, "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = eventName,
                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                onValueChange = {
                    eventName = it
                },
                label = {
                    Text(text = "Name of the event")
                },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.primary,
                    focusedLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.primary
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        pickerType = PickerType.DATE
                        dialogState.show()
                    },
                value = eventDate,
                readOnly = true,
                enabled = false,
                textStyle = TextStyle(color = MaterialTheme.colors.primary),
                onValueChange = {
                }, label = {
                    Text(text = "Date")
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_date_range_24),
                        contentDescription = "Select date"
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.primary,
                    disabledLabelColor = MaterialTheme.colors.primary,
                    unfocusedLabelColor = MaterialTheme.colors.primary,
                    trailingIconColor = MaterialTheme.colors.primary,
                    disabledTrailingIconColor = MaterialTheme.colors.primary
                )
            )


            AnimatedVisibility(visible = eventDate != "", enter = slideInVertically()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                pickerType = PickerType.TIME
                                timeType = TimeType.START
                                dialogState.show()
                            },
                        readOnly = true,
                        enabled = false,
                        textStyle = TextStyle(color = MaterialTheme.colors.primary),
                        value = startTime,
                        onValueChange = {
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_access_time_24),
                                contentDescription = "Start time"
                            )
                        },
                        label = {
                            Text(text = "Start time")
                        }, colors = TextFieldDefaults.outlinedTextFieldColors(
                            disabledBorderColor = MaterialTheme.colors.primary,
                            unfocusedBorderColor = MaterialTheme.colors.primary,
                            disabledLabelColor = MaterialTheme.colors.primary,
                            disabledTrailingIconColor = MaterialTheme.colors.primary
                        )
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                pickerType = PickerType.TIME
                                timeType = TimeType.END
                                dialogState.show()
                            },
                        readOnly = true,
                        enabled = false,
                        textStyle = TextStyle(color = MaterialTheme.colors.primary),
                        value = endTime,
                        onValueChange = {
                        }, label = {
                            Text(text = "End time")
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_access_time_24),
                                contentDescription = "End time"
                            )
                        }, colors = TextFieldDefaults.outlinedTextFieldColors(
                            disabledBorderColor = MaterialTheme.colors.primary,
                            unfocusedBorderColor = MaterialTheme.colors.primary,
                            disabledLabelColor = MaterialTheme.colors.primary,
                            disabledTrailingIconColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
            AnimatedVisibility(
                visible = eventDate == "", exit = fadeOut()
            ) {
                Text(
                    text = "Select date to enter event time",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

        }


    }
}