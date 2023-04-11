package com.example.oldpeoplecareapp.ui.PatientPath.AllAlarms

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAllAlarmsBinding
import com.example.oldpeoplecareapp.databinding.FragmentPatientHomeBinding
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.MedicineRecyclerView
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.PatientHomeFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.PatientHomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_all_alarms.*


class AllAlarmsFragment : Fragment(),OnAlarmClickListener {
    private  val TAG = "AllAlarmsFragment"
    lateinit var binding:FragmentAllAlarmsBinding
    lateinit var  retrivedToken:String
    lateinit var allAlarmsViewModel: AllAlarmsViewModel
    val allMedicineRV by lazy { AllMedicineRV() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllAlarmsBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        navBar?.selectedItemId =R.id.home_icon
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        val retrivedID = getpreferences.getString("ID", null)

        allAlarmsViewModel = ViewModelProvider(requireActivity()).get(AllAlarmsViewModel::class.java)
        allAlarmsViewModel.getAllMedicine(retrivedID.toString(), "barier " + retrivedToken)

        binding.allMedicinRV.adapter = allMedicineRV

        allAlarmsViewModel.allMedicinLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                allMedicineRV.setList(it)
                Log.i(TAG, it.toString())
            }
        })

        allMedicineRV.onListItemClick = this

    }


    override fun onItemClick(info: AllMedicineResponseItem) {
        findNavController().navigate(AllAlarmsFragmentDirections.actionAllAlarmsFragmentToEditMedicineFragment(info._id,info.lastUpdatedUserID,retrivedToken))
        Log.i(TAG,info.name)
    }

}