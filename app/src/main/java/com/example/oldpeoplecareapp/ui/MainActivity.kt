package com.example.oldpeoplecareapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.oldpeoplecareapp.MyFirebaseMessagingService
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.ActivityMainBinding
import com.example.oldpeoplecareapp.ui.AddNewMedicine.AddNewMedicineFragmentDirections
import com.example.oldpeoplecareapp.ui.CaregiversPatient.CaregiversPatientFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientNotification.PatientNotificationFragmentDirections
import com.example.oldpeoplecareapp.ui.patientHome.PatientHomeFragmentDirections
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
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

        ) {
            binding.bottomNavigation.visibility = View.GONE
        } else {
            binding.bottomNavigation.visibility = View.VISIBLE
        }

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


    }


}