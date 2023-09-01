package com.smithjilks.mpesaexpensetracker.core.utils

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.smithjilks.mpesaexpensetracker.core.data.AppDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

object BulkSMSRetriever {

    fun readAndSaveBulkSMS(
        activity: ComponentActivity,
        ioDispatcher: CoroutineDispatcher,
        appDao: AppDao
    ) {
        val permission = Manifest.permission.READ_SMS
        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val smsUri = Uri.parse("content://sms/inbox")
        val contentResolver = activity.contentResolver
        val projection = arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE)
        val selection = "${Telephony.Sms.ADDRESS}=?"
        val selectionArgs = arrayOf("MPESA")
        val sortOrder = "${Telephony.Sms.DATE} DESC"

        val cursor = contentResolver.query(smsUri, projection, selection, selectionArgs, sortOrder)
        activity.lifecycleScope.launch(ioDispatcher) {
            val messages = readFromCursor(cursor)
            val records = messages.mapNotNull {
                CoreUtils.createRecordFromMpesaMessage(it)
            }
            appDao.insertRecords(records)
        }
    }

    private fun readFromCursor(cursor: Cursor?): List<String> {
        val messagesList = mutableListOf<String>()
        cursor?.use { cur ->
            while (cur.moveToNext()) {
                val senderAddress = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                val smsBody = cur.getString(cur.getColumnIndexOrThrow(Telephony.Sms.BODY))
                val timestamp = cur.getLong(cur.getColumnIndexOrThrow(Telephony.Sms.DATE))
                if (senderAddress != "MPESA") continue
                messagesList.add(smsBody)
            }
        }
        return messagesList
    }
}