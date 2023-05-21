package com.smithjilks.mpesaexpensetracker

import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.smithjilks.mpesaexpensetracker.core.theme.MpesaExpenseTrackerTheme
import com.smithjilks.mpesaexpensetracker.core.utils.Permissions
import com.smithjilks.mpesaexpensetracker.core.utils.SmsBroadcastReceiver
import com.smithjilks.mpesaexpensetracker.navigation.MpesaExpenseTrackerNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intentFilter = IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        Permissions.requestSmsPermission(this)
        setContent {
            MpesaExpenseTrackerApp()
        }
    }

    @Composable
    fun MpesaExpenseTrackerApp() {
        MpesaExpenseTrackerTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MpesaExpenseTrackerNavigation()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MpesaExpenseTrackerTheme {
        }
    }




}