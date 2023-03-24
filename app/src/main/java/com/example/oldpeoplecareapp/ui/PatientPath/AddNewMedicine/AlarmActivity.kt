package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.oldpeoplecareapp.R

class AlarmActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_alarm)

        val fragment: Fragment = AlarmFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContaine, fragment)
            .commit()
    }
}