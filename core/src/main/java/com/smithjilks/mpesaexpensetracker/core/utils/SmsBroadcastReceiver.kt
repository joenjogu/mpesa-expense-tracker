package com.smithjilks.mpesaexpensetracker.core.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.smithjilks.mpesaexpensetracker.core.R
import com.smithjilks.mpesaexpensetracker.core.constants.AppConstants
import com.smithjilks.mpesaexpensetracker.core.repository.AppDatabaseRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: AppDatabaseRepository
    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

    companion object {
        private val TAG = SmsBroadcastReceiver::class.java.name

    }

    override fun onReceive(context: Context, intent: Intent?) {
        if (!intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        var concatenatedSms = ""
        extractMessages.forEach { smsMessage ->
            if (smsMessage.displayOriginatingAddress == AppConstants.MPESA ) {
                    concatenatedSms += smsMessage.messageBody
            }
        }

        if (concatenatedSms.isNotEmpty()) {
            val record = CoreUtils.createRecordFromMpesaMessage(concatenatedSms)

            val notificationIconId = if (record?.recordType == AppConstants.EXPENSE)
                R.drawable.notification_expense_icon else R.drawable.notification_income_icon

            record?.let {
                CoroutineScope(ioDispatcher).launch {
                    val newRecordId = repository.insertRecord(it)

                    if (Permissions.notificationPermissionGranted(context)) {
                        NotificationUtil(
                            context, notificationIconId,
                            newRecordId.toInt()
                        ).showNotification()
                    }
                    Log.d(TAG, "Added $newRecordId")
                }
            }
        }
    }


}
