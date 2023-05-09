package com.example.oldpeoplecareapp.ui.PatientPath.AlarmScreen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAlarmBinding
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.PatientHomeViewModel


class AlarmFragment : Fragment() {

    lateinit var binding:FragmentAlarmBinding
    lateinit var alarmScreenViewModel: AlarmScreenViewModel

    lateinit var loading:LoadingDialog
    lateinit var retrivedToken: String
    var medImageUrl:String =""
    var medName:String=""
    var alarmSoundPath:String=""
    var medTime:String=""
    var retrivedID:String=""
    var medId:String=""
    var reqCode:Long=0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        medImageUrl = arguments?.getString("medImageUrl").toString()
        medName = arguments?.getString("medName").toString()
        alarmSoundPath = arguments?.getString("alarmSoundPath").toString()
        medTime = arguments?.getString("medTime").toString()
        retrivedID = arguments?.getString("retrivedID").toString()
        medId = arguments?.getString("medId").toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val alarmHelper = AlarmHelper()
        loading = LoadingDialog(requireActivity())
        alarmScreenViewModel = ViewModelProvider(requireActivity()).get(AlarmScreenViewModel::class.java)

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        //retrivedID = getpreferences.getString("ID", null).toString()

        binding.cancelBtn.setOnClickListener {
            loading.startLoading()
            alarmScreenViewModel.changeState(
                "barier " + retrivedToken,
                retrivedID,
                medId,
                "Missed"
            )
            alarmHelper.cancelAlarm(requireContext(),reqCode)
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right)

        }

        binding.doneBtn.setOnClickListener {
            loading.startLoading()
            alarmScreenViewModel.changeState(
                "barier " + retrivedToken,
                retrivedID,
                medId,
                "Completed"
            )
            alarmHelper.cancelAlarm(requireContext(),reqCode)
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right)
        }

        binding.medTxtfield.text=medName

        binding.timeTxtfield.text=medTime

        binding.medImgview.setBackgroundResource(R.drawable.oval)
        Glide.with(requireContext()).load(medImageUrl).into(binding.medImgview)

    }

}