package com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.oldpeoplecareapp.R

class AlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_alarm)

        val medImageUrl = intent?.getStringExtra("medImageUrl").toString()
        val medName = intent?.getStringExtra("medName").toString()
        val alarmSoundPath = intent?.getStringExtra("alarmSoundPath").toString()
        val medId = intent?.getStringExtra("medId").toString()
        val medTime = intent?.getStringExtra("medTime").toString()


        val fragment: Fragment = AlarmFragment()

        val args = Bundle()
        args.putString("medImageUrl", medImageUrl)
        args.putString("medName", medName)
        args.putString("alarmSoundPath", alarmSoundPath)
        args.putString("requestCode", medId)
        args.putString("medTime", medTime)
        fragment.arguments = args

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContaine, fragment)
            .commit()
    }
}