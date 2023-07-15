package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentPatientHomeBinding
import com.example.oldpeoplecareapp.model.entity.Medicine
import com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen.AlarmHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_all_alarms.*
import kotlinx.android.synthetic.main.fragment_patient_home.*
import java.util.*


class PatientHomeFragment : Fragment(),OnItemClickListener {
    val TAG: String = "PatientHomeFragmentTAG"
    lateinit var binding: FragmentPatientHomeBinding
    lateinit var patientHomeViewModel: PatientHomeViewModel
    lateinit var retrivedToken: String
    lateinit var retrivedID: String
    lateinit var loading: LoadingDialog
    val alarmHelper = AlarmHelper()
    var STATE: String = "Upcoming"
    val medicineRecyclerView by lazy { MedicineRecyclerView() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPatientHomeBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        navBar?.selectedItemId = R.id.home_icon
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         loading = LoadingDialog(requireActivity())

        if(medicineRecyclerView.medicineList.isEmpty()){
            binding.base.visibility=View.VISIBLE
        }else{
            binding.base.visibility=View.GONE

        }

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        retrivedID = getpreferences.getString("ID", null).toString()

        binding.medicineRecyclerView.adapter = medicineRecyclerView

        patientHomeViewModel = ViewModelProvider(requireActivity()).get(PatientHomeViewModel::class.java)
        patientHomeViewModel.getAllMedicine("barier " + retrivedToken, retrivedID.toString(), "Waiting")

        binding.emergencyBtn.setOnClickListener {
            findNavController().navigate(PatientHomeFragmentDirections.actionPatientHomeFragmentToEmergencyFragment())
        }
        patientHomeViewModel.getUserInfo("barier " + retrivedToken, retrivedID.toString())

        patientHomeViewModel.UserLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                loading.isDismiss()
                Glide.with(this).load(it.image.url).into(binding.userInfo)
                binding.userInfo.setBackgroundResource(R.drawable.circular)


            } else if(patientHomeViewModel.error!=null) {
                loading.isDismiss()
                Snackbar.make(
                    view,
                    patientHomeViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                patientHomeViewModel.error =null
            }
        })


        binding.upcommingBtn.setOnClickListener {
            STATE = "Upcoming"
            binding.upcommingBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.active))
            binding.Completed.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.inactive))
            binding.upcommingBtn.setBackgroundResource(R.drawable.buttom_bottomcorner)
            binding.Completed.setBackgroundResource(R.drawable.cornersinactive)


            val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
            // val retrivedToken = getpreferences.getString("TOKEN", null)
            val retrivedID = getpreferences.getString("ID", null)
            patientHomeViewModel.getAllMedicine("barier " + retrivedToken, retrivedID.toString(), "Waiting")
            patientHomeViewModel.allMedicinLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {

                    for (meds in it) {
                        val medId = meds.medicine._id
                        for (med in meds.medicine.time) {
                            val hour = med.substring(0, 2).toInt()
                            val mins = med.substring(3).toInt()

                            Log.i("The Time" , " hour : $hour - min : $mins ")
                            // Create a Calendar instance and set the time to the specified hour and minute
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, hour)
                            calendar.set(Calendar.MINUTE, mins)
                            calendar.set(Calendar.SECOND, 0)
                            calendar.set(Calendar.MILLISECOND, 0)

                            // Check if the time has already passed for the day
                            val currentTimeMillis = System.currentTimeMillis()
                            val medTimeMillis = calendar.timeInMillis
                            if (medTimeMillis < currentTimeMillis) {
                                calendar.add(Calendar.DAY_OF_YEAR, 1)
                            }

                            // Generate a unique requestCode for each alarm
                            val requestCode = medId.hashCode() + medTimeMillis.hashCode()

                            // Set the alarm using the AlarmHelper class
                            alarmHelper.setAlarm(requireContext(), requestCode, calendar,meds.medicine.image.url,  meds.medicine.name,  meds.medicine.audio.url,"$hour:$mins",retrivedID,medId)
                        }
                    }
                    medicineRecyclerView.setList(it)


                    if(medicineRecyclerView.medicineList.isEmpty()){
                        binding.base.visibility=View.VISIBLE
                    }else{
                        binding.base.visibility=View.GONE

                    }



                    Log.i(TAG, it.toString())
                }else if(patientHomeViewModel.error !=null){
                    Snackbar.make(
                        PATIENTHOME,
                        patientHomeViewModel.error.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    patientHomeViewModel.error=null
                }
            })
        }

        binding.Completed.setOnClickListener {
            STATE = "Completed"
            binding.Completed.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.active))
            binding.upcommingBtn.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.inactive))
            binding.Completed.setBackgroundResource(R.drawable.buttom_bottomcorner)
            binding.upcommingBtn.setBackgroundResource(R.drawable.cornersinactive)


            val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
            // val retrivedToken = getpreferences.getString("TOKEN", null)
            val retrivedID = getpreferences.getString("ID", null)
            patientHomeViewModel.getAllMedicine("barier " + retrivedToken, retrivedID.toString(), "Completed")
            patientHomeViewModel.allMedicinLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    medicineRecyclerView.setList(it)
                    if(medicineRecyclerView.medicineList.isEmpty()){
                        binding.base.visibility=View.VISIBLE
                    }else{
                        binding.base.visibility=View.GONE

                    }

                    Log.i("tttttt","it is ${it}")

                }else if(patientHomeViewModel.error !=null){
                    Snackbar.make(
                        PATIENTHOME,
                        patientHomeViewModel.error.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    patientHomeViewModel.error=null
                }
            })
        }


        binding.MissedAlarms.setOnClickListener {
            STATE = "Missed"
            binding.Completed.setBackgroundResource(R.drawable.cornersinactive)
            binding.upcommingBtn.setBackgroundResource(R.drawable.cornersinactive)

            val getpreferences =
                requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
            // val retrivedToken = getpreferences.getString("TOKEN", null)
            val retrivedID = getpreferences.getString("ID", null)
            patientHomeViewModel.getAllMedicine("barier " + retrivedToken, retrivedID.toString(), "Missed")
            patientHomeViewModel.allMedicinLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    medicineRecyclerView.setList(it)
                    if(medicineRecyclerView.medicineList.isEmpty()){
                        binding.base.visibility=View.VISIBLE
                    }else{
                        binding.base.visibility=View.GONE

                    }

                    Log.i(TAG, it.toString())
                }else if(patientHomeViewModel.error !=null){
                    Snackbar.make(
                        PATIENTHOME,
                        patientHomeViewModel.error.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    patientHomeViewModel.error=null
                }
            })
        }

        binding.AllAlarms.setOnClickListener {
            findNavController().navigate(PatientHomeFragmentDirections.actionPatientHomeFragmentToAllAlarmsFragment())
        }

        binding.userInfo.setOnClickListener {
            findNavController().navigate(PatientHomeFragmentDirections.actionPatientHomeFragmentToBasicInformationFragment())
        }
        patientHomeViewModel.allMedicinLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                medicineRecyclerView.setList(it)
                if(medicineRecyclerView.medicineList.isEmpty()){
                    binding.base.visibility=View.VISIBLE
                }else{
                    binding.base.visibility=View.GONE

                }

                Log.i(TAG, it.toString())
            }else if(patientHomeViewModel.error !=null){
                Snackbar.make(
                    PATIENTHOME,
                    patientHomeViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                patientHomeViewModel.error=null
            }
        })
        if(medicineRecyclerView.medicineList.isEmpty()){
            binding.base.visibility=View.VISIBLE
        }else{
            binding.base.visibility=View.GONE

        }

        medicineRecyclerView.onListItemClick = this

    }

    override fun onItemClick(info: Medicine) {
        findNavController().navigate(
            PatientHomeFragmentDirections.actionPatientHomeFragmentToEditMedicineFragment(
                info.medicine._id,
                info.medicine._id,
                retrivedToken
            )
        )
    }

    override fun onStateClick(info: Medicine) {
        if (STATE == "Missed") {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are You Sure")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    patientHomeViewModel.changeState(
                        "barier " + retrivedToken,
                        retrivedID,
                        info.medicine._id,
                        "Completed"
                    )
                    patientHomeViewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            patientHomeViewModel.getAllMedicine("barier " + retrivedToken, retrivedID.toString(), "Missed")
                        }
                    })

                    patientHomeViewModel.snackBarLiveData.observe(viewLifecycleOwner){
                        Snackbar.make(PATIENTHOME, it.toString(), Snackbar.LENGTH_SHORT).show()
                        loading.isDismiss()

                    }
                }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()

        } else if (STATE == "Completed") {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are You Sure")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    patientHomeViewModel.changeState(
                        "barier " + retrivedToken,
                        retrivedID,
                        info.medicine._id,
                        "Missed"
                    )
                    patientHomeViewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            patientHomeViewModel.getAllMedicine("barier " + retrivedToken, retrivedID.toString(), "Completed")
                        }
                    })

                    patientHomeViewModel.snackBarLiveData.observe(viewLifecycleOwner){
                        Snackbar.make(PATIENTHOME, it.toString(), Snackbar.LENGTH_SHORT).show()
                        loading.isDismiss()

                    }
                }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()

        } else if (STATE == "Upcoming") {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are You Sure")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    loading.startLoading()
                    patientHomeViewModel.changeState(
                        "barier " + retrivedToken,
                        retrivedID,
                        info.medicine._id,
                        "Completed"
                    )
                    patientHomeViewModel.stateLiveData.observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            loading.isDismiss()
                        }
                    })

                    patientHomeViewModel.snackBarLiveData.observe(viewLifecycleOwner){
                        Snackbar.make(PATIENTHOME, it.toString(), Snackbar.LENGTH_SHORT).show()
                        loading.isDismiss()
                    }
                }
                .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }
    }
}