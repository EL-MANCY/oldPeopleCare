package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.PowerManager
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            context?.let {
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
                        "com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine.ALARM_ACTION"
                    alarmIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(alarmIntent)

                    val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
                    ringtone.play()
                    wakeLock.release()

                }
            }
            } catch (e: Exception) {
                Log.i("erroralarm", "errrrer")
            }
        }
    }
