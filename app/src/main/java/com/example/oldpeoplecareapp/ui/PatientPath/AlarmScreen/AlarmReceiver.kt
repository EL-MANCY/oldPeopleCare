package com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.oldpeoplecareapp.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        try {
            context?.let {

                val medImageUrl = intent?.getStringExtra("medImageUrl").toString()
                val medName = intent?.getStringExtra("medName").toString()
                val alarmSoundPath = intent?.getStringExtra("alarmSoundPath").toString()

                val requestCode = intent.getIntExtra("requestCode", -1)
                if (requestCode != -1) {
                    // Acquire a wake lock to prevent the device from going to sleep
                    val powerManager =
                        context.getSystemService(Context.POWER_SERVICE) as PowerManager
                    val wakeLock = powerManager.newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK,
                        "MyApp::AlarmReceiver"
                    )
                    wakeLock.acquire()

                    val alarmIntent = Intent(context, AlarmActivity::class.java)
                    alarmIntent.action =
                        "com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen.ALARM_ACTION"
                    alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                    alarmIntent.putExtra("medImageUrl", medImageUrl)
                    alarmIntent.putExtra("medName", medName)
                    alarmIntent.putExtra("alarmSoundPath", alarmSoundPath)

                    context.startActivity(alarmIntent)

                    val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
                    ringtone.play()
                    wakeLock.release()


                    Log.i("nammme", medName)

                    val pendingIntent = PendingIntent.getActivity(
                        context,
                        requestCode,
                        alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel(
                            "alarm_channel_id",
                            "alarm_channel_id",
                            NotificationManager.IMPORTANCE_HIGH
                        )
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }

                    val builder = NotificationCompat.Builder(context!!, "alarm_channel_id")
                        .setSmallIcon(R.drawable.med_img)
                        .setContentTitle("Alarm for $medName")
                        .setContentText("It's time to take your medication")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setSound(Uri.parse(alarmSoundPath))
                        .setContentIntent(pendingIntent)


                    // Show the notification using the NotificationManager
                    val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(notificationChannel)

                    notificationManager.notify(requestCode, builder.build())

                }
            }
        } catch (e: Exception) {
            Log.i("erroralarm", e.toString())

        }
    }
}
