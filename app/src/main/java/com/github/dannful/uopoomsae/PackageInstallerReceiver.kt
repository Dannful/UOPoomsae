package com.github.dannful.uopoomsae

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class PackageInstallerReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeIntentLaunch")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.getIntExtra(PackageInstaller.EXTRA_STATUS, -1)) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                val confirmationIntent =
                    intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java) ?: return
                context.startActivity(confirmationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }

            else -> {
                Toast.makeText(
                    context,
                    "Falha ao fazer download. Por favor, tente novamente mais tarde.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}