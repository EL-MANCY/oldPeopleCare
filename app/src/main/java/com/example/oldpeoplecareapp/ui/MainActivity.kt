package com.example.oldpeoplecareapp.ui

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiConfiguration.AuthAlgorithm.SHARED
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.ActivityMainBinding
import com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients.AllPatientsFragmentDirections
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.CaregiveHomeFragmentDirections
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications.CaregiverNotificationsFragmentDirections
import com.example.oldpeoplecareapp.ui.Chat.ChatScreen.socketHandler
import com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine.AddNewMedicineFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient.CaregiversPatientFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.PatientNotification.PatientNotificationFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.PatientHomeFragmentDirections
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val CALL_PERMISSION_REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)





        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, UpcomingBroadcastReciever::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )


        val getpreferences = getSharedPreferences("MY_APP", MODE_PRIVATE)
        var retrivedToken:String? = getpreferences.getString("TOKEN", "null")
        var REGIST:String? = getpreferences.getString("REGIST", "null")




        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            val permissions = arrayOf(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.packageName)
                )
                startActivityForResult(intent, 111)
            } else {
                //Permission Granted-System will work
            }
        }

        Log.i("FCMTOKEN",FirebaseMessaging.getInstance().token.toString())

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment?
        val navController = navHostFragment!!.navController

        if (navController.currentDestination?.label == "fragment_registration"
            || navController.currentDestination?.label == "fragment_log_in"
            || navController.currentDestination?.label == "EditMedicineFragment"
            || navController.currentDestination?.label == "AddNewcaregiverPatientFragment"
            || navController.currentDestination?.label == "PatientNotificationFragment"
            || navController.currentDestination?.label == "CaregiveHomeFragment"
            || navController.currentDestination?.label == "SplashScreenFragment"
            || navController.currentDestination?.label=="fragment_chat"



        ) {
            binding.bottomNavigation.visibility = View.GONE

        } else {
            binding.bottomNavigation.visibility = View.VISIBLE
        }

        if (navController.currentDestination?.label == "fragment_registration"
            || navController.currentDestination?.label == "fragment_log_in"
            || navController.currentDestination?.label == "SplashScreenFragment"
            || navController.currentDestination?.label=="fragment_chat"


        ) {
            binding.bottomNavigation2.visibility = View.GONE

        } else {
            binding.bottomNavigation2.visibility = View.VISIBLE
        }


      //  navController.popBackStack()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.add_icon -> {
                    if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "PatientHomeFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(PatientHomeFragmentDirections.actionPatientHomeFragmentToAddNewMedicineFragment())
                    } else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "CaregiversPatientFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(CaregiversPatientFragmentDirections.actionCaregiversPatientFragmentToAddNewMedicineFragment())
                    }else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "PatientNotificationFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(PatientNotificationFragmentDirections.actionPatientNotificationFragmentToAddNewMedicineFragment())
                    }
                    true
                }
                R.id.home_icon -> {
                    if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "AddNewMedicineFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(
                                AddNewMedicineFragmentDirections.actionAddNewMedicineFragmentToPatientHomeFragment(
                                    "",
                                    "",
                                    ""
                                )
                            )
                    } else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "CaregiversPatientFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(
                                CaregiversPatientFragmentDirections.actionCaregiversPatientFragmentToPatientHomeFragment(
                                    "",
                                    "",
                                    ""
                                )
                            )
                    }else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "PatientNotificationFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(PatientNotificationFragmentDirections.actionPatientNotificationFragmentToPatientHomeFragment("","",""))
                    }
                    true
                }
                R.id.caregiver_icon -> {
                    if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "PatientHomeFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(PatientHomeFragmentDirections.actionPatientHomeFragmentToCaregiversPatientFragment())
                    } else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "AddNewMedicineFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(AddNewMedicineFragmentDirections.actionAddNewMedicineFragmentToCaregiversPatientFragment())
                    }else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "PatientNotificationFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(PatientNotificationFragmentDirections.actionPatientNotificationFragmentToCaregiversPatientFragment())
                    }
                    true
                }
                R.id.notification_icon ->{
                    if(findNavController(R.id.fragmentContainerView).currentDestination?.label == "PatientHomeFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(PatientHomeFragmentDirections.actionPatientHomeFragmentToPatientNotificationFragment())
                    }else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "AddNewMedicineFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(AddNewMedicineFragmentDirections.actionAddNewMedicineFragmentToPatientNotificationFragment())
                    }else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "CaregiversPatientFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(CaregiversPatientFragmentDirections.actionCaregiversPatientFragmentToPatientNotificationFragment())
                    }

                    true
                }
                else -> {
                    false
                }
            }
        }

        binding.bottomNavigation2.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_page -> {
                    if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "AllPatientsFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(AllPatientsFragmentDirections.actionAllPatientsFragmentToCaregiveHomeFragment())
                    }else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "CaregiverNotificationsFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(CaregiverNotificationsFragmentDirections.actionCaregiverNotificationsFragmentToCaregiveHomeFragment())
                    }

                    true
                }
                R.id.patients_icon ->{
                    if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "CaregiveHomeFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(CaregiveHomeFragmentDirections.actionCaregiveHomeFragmentToAllPatientsFragment())
                    }else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "CaregiverNotificationsFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(CaregiverNotificationsFragmentDirections.actionCaregiverNotificationsFragmentToAllPatientsFragment())
                    }
                    true
                }
                R.id.notifi_icon ->{
                    if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "CaregiveHomeFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(CaregiveHomeFragmentDirections.actionCaregiveHomeFragmentToCaregiverNotificationsFragment())
                    }else if (findNavController(R.id.fragmentContainerView).currentDestination?.label == "AllPatientsFragment") {
                        Navigation.findNavController(this, R.id.fragmentContainerView)
                            .navigate(AllPatientsFragmentDirections.actionAllPatientsFragmentToCaregiverNotificationsFragment())
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, do something
                } else {
                    finish()
                }
            }
        }
    }



}