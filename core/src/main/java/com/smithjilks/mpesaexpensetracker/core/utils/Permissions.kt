package com.smithjilks.mpesaexpensetracker.core.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission

object Permissions {
    private const val REQUEST_CODE_SMS_PERMISSION = 4

    fun requestSmsPermission(context: Activity) {
        val permission = Manifest.permission.RECEIVE_SMS
        val grant = checkSelfPermission(context, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(permission),
                REQUEST_CODE_SMS_PERMISSION
            )
        }
    }

    fun notificationPermissionGranted(context: Context): Boolean {
        var granted = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            granted =
                context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }

        return granted
    }
}