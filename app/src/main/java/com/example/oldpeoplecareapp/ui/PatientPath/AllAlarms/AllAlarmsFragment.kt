package com.example.oldpeoplecareapp.ui.PatientPath.AllAlarms

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAllAlarmsBinding
import com.example.oldpeoplecareapp.databinding.FragmentPatientHomeBinding
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.MedicineRecyclerView
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.PatientHomeFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.PatientHomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_all_alarms.*
import kotlinx.android.synthetic.main.fragment_edit_remove_caregiver_role.*


class AllAlarmsFragment : Fragment(),OnAlarmClickListener {
    private  val TAG = "AllAlarmsFragment"
    private lateinit var navController: NavController
    lateinit var binding:FragmentAllAlarmsBinding
    lateinit var  retrivedToken:String
    lateinit var allAlarmsViewModel: AllAlarmsViewModel
    val allMedicineRV by lazy { AllMedicineRV() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllAlarmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        navController = NavHostFragment.findNavController(this)

        val bottomNavigation:BottomNavigationView=requireActivity().findViewById(R.id.bottom_navigation)

        if (navController.currentDestination?.label == "fragment_registration"
            || navController.currentDestination?.label == "fragment_log_in"
            || navController.currentDestination?.label=="EditMedicineFragment"
            || navController.currentDestination?.label=="AllAlarmsFragment"
        ) {
            bottomNavigation.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }


        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        val retrivedID = getpreferences.getString("ID", null)

        allAlarmsViewModel = ViewModelProvider(requireActivity()).get(AllAlarmsViewModel::class.java)
        allAlarmsViewModel.getAllMedicine(retrivedID.toString(), "barier " + retrivedToken)
        binding.allMedicinRV.adapter = allMedicineRV

        allAlarmsViewModel.allMedicinLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                allMedicineRV.setList(it)

                if(allMedicinRV.isEmpty()){
                    binding.base.visibility=View.VISIBLE
                }else{
                    binding.base.visibility=View.GONE

                }

                Log.i(TAG, it.toString())
            }else if(allAlarmsViewModel.error !=null){
                Snackbar.make(
                    ALLALARMS,
                    allAlarmsViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                allAlarmsViewModel.error =null
            }
        })

        if(allMedicinRV.isEmpty()){
            binding.base.visibility=View.VISIBLE
        }else{
            binding.base.visibility=View.GONE

        }
        allMedicineRV.onListItemClick = this

    }


    override fun onItemClick(info: AllMedicineResponseItem) {
        findNavController().navigate(AllAlarmsFragmentDirections.actionAllAlarmsFragmentToEditMedicineFragment(info._id,info.lastUpdatedUserID,retrivedToken))
        Log.i(TAG,info.name)
    }

}