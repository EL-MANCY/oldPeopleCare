package com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.*

class AlarmHelper {

    fun setAlarm(
        context: Context,
        reqCode: Int,
        medTime: Calendar,
        medImageUrl: String,
        medName: String,
        alarmSoundPath: String,
        time: String,
        retrivedID: String?,
        medId: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an Intent for the BroadcastReceiver
        val intent = Intent(context, AlarmReceiver::class.java)

        // Create a unique request code for the PendingIntent
        val requestCode = (reqCode.toString() + medTime.timeInMillis).hashCode()

        // Add the medicine information to the Intent as extras
        intent.putExtra("requestCode", requestCode)
        intent.putExtra("reqCode", reqCode)
        intent.putExtra("medImageUrl", medImageUrl)
        intent.putExtra("medName", medName)
        intent.putExtra("alarmSoundPath", alarmSoundPath)
        intent.putExtra("medTime", time)
        intent.putExtra("medId", medId)
        intent.putExtra("retrivedID", retrivedID)

        // Create a PendingIntent to be triggered when the alarm goes off
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        // Cancel any previously set alarms for this medicine and time
        alarmManager.cancel(pendingIntent)

        // Set the alarm using the AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, medTime.timeInMillis, pendingIntent)
    }

    fun cancelAlarm(context: Context, medId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Create an Intent for the BroadcastReceiver
        val intent = Intent(context, AlarmReceiver::class.java)
        // Create a unique request code for the PendingIntent
        val requestCode = medId.toString().hashCode()
        // Create a PendingIntent to be triggered when the alarm goes off
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE)
        if(pendingIntent != null){
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
        // Stop the alarm tone
    }



    fun getNextAlarmTime(context: Context): Long {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an Intent for the BroadcastReceiver
        val intent = Intent(context, AlarmActivity::class.java)

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
