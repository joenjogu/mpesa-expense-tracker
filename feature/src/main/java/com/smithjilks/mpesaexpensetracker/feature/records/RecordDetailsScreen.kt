package com.smithjilks.mpesaexpensetracker.feature.records

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.smithjilks.mpesaexpensetracker.core.constants.AppConstants
import com.smithjilks.mpesaexpensetracker.core.model.Category
import com.smithjilks.mpesaexpensetracker.core.model.Record
import com.smithjilks.mpesaexpensetracker.core.utils.CoreUtils
import com.smithjilks.mpesaexpensetracker.core.widgets.AppDatePickerDialog
import com.smithjilks.mpesaexpensetracker.core.widgets.AppInputTextField
import com.smithjilks.mpesaexpensetracker.core.widgets.AppSpinner
import com.smithjilks.mpesaexpensetracker.core.widgets.AppTimePicker
import com.smithjilks.mpesaexpensetracker.feature.R
import com.smithjilks.mpesaexpensetracker.feature.utils.FeatureUtils
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RecordDetailsScreen(
    navController: NavController,
    recordsViewModel: RecordsViewModel = hiltViewModel(),
    recordId: String?,
) {
    Scaffold(
        topBar = {

        }
    ) {
        Log.d("foofo", navController.currentBackStackEntry.toString())
        Log.d("foofo", navController.currentDestination.toString())

        RecordDetailsContent(
            navController = navController,
            recordsViewModel = recordsViewModel,
            recordId = recordId
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun RecordDetailsContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    recordsViewModel: RecordsViewModel,
    recordId: String?

) {

    var transactionRef by remember { mutableStateOf("") }

    var amount by remember { mutableStateOf(0) }

    var transactionCost by remember { mutableStateOf(0) }

    var category by remember { mutableStateOf("") }

    var note by remember { mutableStateOf("") }

    var recordType by remember { mutableStateOf(AppConstants.EXPENSE) }

    var account by remember { mutableStateOf("") }

    var payee by remember { mutableStateOf("") }

    var date by remember { mutableStateOf("") }

    var time by remember { mutableStateOf("") }

    val systemUiController = rememberSystemUiController()
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }


    if (recordId!!.isNotEmpty()) {
        recordsViewModel.getRecordById(recordId.toInt())
        LaunchedEffect(true) {
            transactionRef = recordsViewModel.dbRecord.transactionRef
            amount = recordsViewModel.dbRecord.amount
            transactionCost = recordsViewModel.dbRecord.transactionCost
            category = recordsViewModel.dbRecord.category
            note = recordsViewModel.dbRecord.note
            recordType = recordsViewModel.dbRecord.recordType
            account = recordsViewModel.dbRecord.account
            payee = recordsViewModel.dbRecord.payee
            date = CoreUtils.formatDate(recordsViewModel.dbRecord.timestamp.toLong() * 1000)
            time = CoreUtils.formatTime(recordsViewModel.dbRecord.timestamp)
        }
    }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = if (recordType == AppConstants.INCOME) {
            systemUiController.setStatusBarColor(
                color = MaterialTheme.colorScheme.primary
            )
            MaterialTheme.colorScheme.primary
        } else {
            systemUiController.setStatusBarColor(
                color = MaterialTheme.colorScheme.error
            )
            MaterialTheme.colorScheme.error
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(color = Color.Transparent),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                modifier = modifier
                    .padding(start = 8.dp)
                    .background(color = Color.Transparent)
                    .align(Alignment.Start),
                text = "How much?",
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                modifier = modifier
                    .padding(start = 8.dp, bottom = 8.dp)
                    .background(color = Color.Transparent)
                    .align(Alignment.Start),
                text = "Ksh $amount",
                style = MaterialTheme.typography.headlineLarge
            )

            LazyColumn(
                modifier = modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    ),
                verticalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                item {
                    AppInputTextField(
                        modifier = modifier.padding(top = 16.dp),
                        text = transactionRef,
                        label = "Mpesa Code",
                        onTextChange = { transactionRef = it.uppercase(Locale.ROOT) }) {
                    }

                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppInputTextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = amount.toString(),
                            label = "Amount",
                            prefix = {
                                Text(
                                    text = "KES"
                                )
                            },
                            keyboardType = KeyboardType.NumberPassword,
                            onTextChange = { amount = CoreUtils.convertStringAmountToInt(it) }) {
                        }

                        AppInputTextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = transactionCost.toString(),
                            label = "Transaction Cost",
                            prefix = {
                                Text(
                                    text = "KES"
                                )
                            },
                            keyboardType = KeyboardType.NumberPassword,
                            onTextChange = {
                                transactionCost = CoreUtils.convertStringAmountToInt(it)
                            }) {
                        }
                    }

                    //Categories spinner
                    AppSpinner(
                        label = "Category",
                        parentOptions = FeatureUtils.getCategoriesList(),
                        onValueChange = { category = it }
                    )

                    AppInputTextField(
                        text = note,
                        label = "Note",
                        minLines = 2,
                        maxLines = 2,
                        onTextChange = { note = it }
                    ) {
                    }

                    //Record Type spinner
                    AppSpinner(
                        label = "Record Type",
                        parentOptions = listOf(
                            Category(
                                name = AppConstants.INCOME,
                                imageId = R.drawable.income_arrow_icon
                            ),
                            Category(
                                name = AppConstants.EXPENSE,
                                imageId = R.drawable.expense_arrow_icon
                            ),
                        ),
                        value = recordType,
                        onValueChange = { recordType = it }
                    )

                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        AppInputTextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clickable {
                                    showDatePickerDialog = true
                                },
                            isEnabled = false,
                            text = date,
                            label = "Date",
                            trailingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        R.drawable.date_icon
                                    ),
                                    contentDescription = "Date Icon"
                                )
                            },
                            onTextChange = { date = it }) {
                        }

                        AppInputTextField(
                            modifier = modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clickable {
                                    showTimePicker = true
                                },
                            isEnabled = false,
                            text = time,
                            label = "Time",
                            trailingIcon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        R.drawable.time_icon
                                    ),
                                    contentDescription = "Date Icon"
                                )
                            },
                            onTextChange = { time = it }) {}
                    }

                    AppInputTextField(
                        text = payee,
                        label = "Payee",
                        onTextChange = { payee = it }) {}

                    AppInputTextField(
                        text = account,
                        label = "Account",
                        onTextChange = { account = it }) {}
                }

            }

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        val newRecord = Record(
                            recordId.toInt(),
                            transactionRef = transactionRef,
                            category = category,
                            amount = amount,
                            transactionCost = transactionCost,
                            note = note,
                            timestamp = CoreUtils.convertDateAndTimeToTimestamp(date, time),
                            account = account,
                            payee = payee,
                            recordType = recordType,
                            recordImageResourceId = FeatureUtils.getCategoriesList().first {
                                it.name == category
                            }.imageId
                        )

                        recordsViewModel.addRecord(record = newRecord)
                        navController.popBackStack()

                    },
                    modifier = modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 8.dp, bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Text(
                        text = "Continue",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    AppDatePickerDialog(
        showDatePickerDialog,
        onValueChange = {
            it?.let { date = it }
            showDatePickerDialog = false
        }
    )

    AppTimePicker(
        showTimePicker,
        onValueChange = {
            it?.let { time = it }
            showTimePicker = false
        }
    )

}


