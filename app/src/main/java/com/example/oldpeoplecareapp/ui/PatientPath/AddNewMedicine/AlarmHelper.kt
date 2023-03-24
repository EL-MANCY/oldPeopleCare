package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.util.*

class AlarmHelper {

    fun setAlarm(context: Context, alarmTimes: MutableList<Calendar>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.i("ALARMHELPER",alarmTimes.toString())

        // Loop through the alarm times and set a unique alarm for each one
        for ((index, alarmTime) in alarmTimes.withIndex()) {
            // Create an Intent for the BroadcastReceiver
            val intent = Intent(context, AlarmReceiver::class.java)
            // Create a unique request code for the PendingIntent
            val requestCode = index
            intent.putExtra("requestCode", index)
            // Create a PendingIntent to be triggered when the alarm goes off
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            // Set the alarm using the AlarmManager
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime.timeInMillis, pendingIntent)
        }
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an Intent for the BroadcastReceiver
        val intent = Intent(context, AlarmReceiver::class.java)

        // Create a PendingIntent to be triggered when the alarm goes off
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Cancel the alarm using the AlarmManager
        alarmManager.cancel(pendingIntent)

        // Stop the alarm tone

    }

    fun getNextAlarmTime(context: Context): Long {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an Intent for the BroadcastReceiver
        val intent = Intent(context, AlarmReceiver::class.java)

        // Create a PendingIntent to be triggered when the alarm goes off
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )

        // Get the next alarm time from the AlarmManager
        val alarmTime = if (pendingIntent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.nextAlarmClock.triggerTime
            } else {
                alarmManager.getNextAlarmClock()?.triggerTime ?: -1
            }
        } else {
            -1
        }

        return alarmTime
    }

//    fun getSnoozeIntent(context: Context): PendingIntent {
//        val intent = Intent(context, AlarmReceiver::class.java)
//        intent.action = AlarmReceiver.ACTION_SNOOZE
//
//        return PendingIntent.getBroadcast(
//            context,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//    }

//    fun getDismissIntent(context: Context): PendingIntent {
//        val intent = Intent(context, AlarmReceiver::class.java)
//        intent.action = AlarmReceiver.ACTION_DISMISS
//
//        return PendingIntent.getBroadcast(
//            context,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//    }
}
