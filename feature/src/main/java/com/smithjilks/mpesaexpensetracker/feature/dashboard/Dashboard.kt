package com.smithjilks.mpesaexpensetracker.feature.dashboard

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.smithjilks.mpesaexpensetracker.core.constants.AppConstants
import com.smithjilks.mpesaexpensetracker.core.constants.MpesaExpenseTrackerScreens
import com.smithjilks.mpesaexpensetracker.core.model.Record
import com.smithjilks.mpesaexpensetracker.core.utils.CoreUtils
import com.smithjilks.mpesaexpensetracker.core.widgets.BottomNavigation
import com.smithjilks.mpesaexpensetracker.feature.R
import java.util.Date


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Dashboard(
    navController: NavController,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(MpesaExpenseTrackerScreens.RecordDetailsScreen.name)
                          },
            ) {
                Icon(Icons.Filled.Edit, "Create new record button")
            }
        }
    ) {
        MainDashboardContent(
            dashboardViewModel = dashboardViewModel,
            navController = navController
        )
    }

}

@Composable
fun MainDashboardContent(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel,
    navController: NavController
) {

    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val recentRecords = dashboardViewModel.recentRecordsList.collectAsState().value

        IncomeAndExpenseSummaryRow(
            income = CoreUtils.formatAmount(dashboardViewModel.totalIncome),
            expense = CoreUtils.formatAmount(dashboardViewModel.totalExpenses)
        )

        Row(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier.padding(vertical = 2.dp),
                fontWeight = FontWeight.Bold
            )

            ElevatedButton(
                onClick = {
                    navController.navigate(MpesaExpenseTrackerScreens.RecordsScreen.name)
                },
                modifier = Modifier
                    .padding(vertical = 2.dp),
                shape = ButtonDefaults.elevatedShape,
                colors = ButtonDefaults.elevatedButtonColors()
            ) {
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.labelSmall
                )
            }


        }


        LazyColumn {
            items(recentRecords) {
                RecordSummaryRow(
                    modifier = Modifier.clickable {
                        navController.navigate(
                            "${MpesaExpenseTrackerScreens.RecordDetailsScreen.name}?recordId=${it.id}"
                        )
                    },
                    record = it,
                )
            }
        }
    }

    NotificationPermissionEffect()

}

@Composable
fun IncomeAndExpenseSummaryRow(modifier: Modifier = Modifier, income: String, expense: String) {

    Row(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IncomeExpenseCard(
            modifier = modifier,
            amount = income,
            image = R.drawable.income_icon,
            isIncome = true
        )

        IncomeExpenseCard(
            modifier = modifier,
            amount = expense,
            image = R.drawable.expense_icon,
            isIncome = false
        )

    }
}

@Composable
fun IncomeExpenseCard(
    modifier: Modifier = Modifier,
    amount: String,
    image: Int,
    isIncome: Boolean
) {

    val backgroundColor = if (isIncome) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.error


    ElevatedCard(
        modifier = modifier
            .background(color = Color.Transparent)
            .padding(horizontal = 8.dp),
        shape = CardDefaults.elevatedShape,
        elevation = CardDefaults.elevatedCardElevation(),
    ) {
        Row(
            modifier = modifier
                .background(color = backgroundColor)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {

            Image(
                modifier = modifier.padding(horizontal = 8.dp),
                painter = painterResource(id = image),
                contentDescription = "Income  Image",
                contentScale = ContentScale.Fit
            )

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = if (isIncome) "Income" else "Expense",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier.padding(end = 8.dp),
                )

                Text(
                    "Ksh. $amount",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = modifier.padding(end = 8.dp),
                )

            }
        }
    }
}


@Composable
fun RecordSummaryRow(record: Record, modifier: Modifier = Modifier) {

    Divider(modifier = modifier.padding(3.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ColumnNoteAndCategory(record = record, modifier = modifier)

        ColumnAmountAndDate(record = record, modifier = modifier)

    }

}


@Composable
fun ColumnAmountAndDate(record: Record, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${if (record.recordType == AppConstants.INCOME) "+" else "-"}Ksh. ${
                record.amount + record.transactionCost
                
            }",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.End,
            color = if (record.recordType == AppConstants.INCOME) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error,
            modifier = modifier.padding(horizontal = 8.dp)
        )

        Text(
            text = CoreUtils.formatTimestamp(record.timestamp),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.End,
            color = Color.LightGray,
            modifier = modifier.padding(horizontal = 8.dp)
        )

    }

}

@Composable
fun ColumnNoteAndCategory(record: Record, modifier: Modifier = Modifier) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Surface(
            shape = CircleShape,
            color = Color.Transparent
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(
                    record.recordImageResourceId ?: R.drawable.shopping_icon
                ),
                contentDescription = "${record.category} Icon",
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = record.category,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Start,
                color = Color.Black,
                modifier = modifier.padding(horizontal = 8.dp)
            )

            Text(
                text = record.note,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
                color = Color.LightGray,
                modifier = modifier.padding(horizontal = 8.dp)
            )

        }
    }

}

@Preview
@Composable
fun IncomeAndExpenseSummaryRowPreview() {
    IncomeAndExpenseSummaryRow(income = "3000", expense = "4000")
}


@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun NotificationPermissionEffect() {
    // Permission requests should only be made from an Activity Context, which is not present
    // in previews
    if (LocalInspectionMode.current) return
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
    val notificationsPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS,
    )
    LaunchedEffect(notificationsPermissionState) {
        val status = notificationsPermissionState.status
        if (status is PermissionStatus.Denied && !status.shouldShowRationale) {
            notificationsPermissionState.launchPermissionRequest()
        }
    }
}

@Preview
@Composable
fun RecordSummaryRowPreview() {
    val record = Record(
        1,
        "BXMASNJAS",
        "Transport",
        300,
        20,
        "MY first Note",
        Date().time,
        "",
        "",
        AppConstants.INCOME,
        R.drawable.transport_icon
    )

    LazyColumn {
        items(listOf(record, record, record, record)) {
            RecordSummaryRow(
                it
            )
        }
    }
}
