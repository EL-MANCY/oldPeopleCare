package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentPatientHomeBinding
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.google.android.material.bottomnavigation.BottomNavigationView


class PatientHomeFragment : Fragment(),OnItemClickListener {
    val TAG: String = "PatientHomeFragmentTAG"
    lateinit var binding: FragmentPatientHomeBinding
    lateinit var remoteRepositoryImp: RemoteRepositoryImp
    lateinit var patientHomeViewModel: PatientHomeViewModel
    lateinit var  retrivedToken:String
    val medicineRecyclerView by lazy { MedicineRecyclerView() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPatientHomeBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        navBar?.selectedItemId =R.id.home_icon
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
         retrivedToken = getpreferences.getString("TOKEN", null).toString()
        val retrivedID = getpreferences.getString("ID", null)

        patientHomeViewModel = ViewModelProvider(requireActivity()).get(PatientHomeViewModel::class.java)
      //  patientHomeViewModel.getAllMedicine(retrivedID.toString(), "barier " + retrivedToken)

        binding.emergencyBtn.setOnClickListener {
            findNavController().navigate(PatientHomeFragmentDirections.actionPatientHomeFragmentToEmergencyFragment())
        }

        binding.medicineRecyclerView.adapter = medicineRecyclerView

//        binding.allMedicineBtn.setOnClickListener {
//            val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
//            val retrivedToken = getpreferences.getString("TOKEN", null)
//            val retrivedID = getpreferences.getString("ID", null)
//            patientHomeViewModel.getAllMedicine(retrivedID.toString(), "barier " + retrivedToken)
//            patientHomeViewModel.allMedicinLiveData.observe(viewLifecycleOwner, Observer {
//                if (it != null) {
//                    medicineRecyclerView.setList(it)
//                    Log.i(TAG, it.toString())
//                }
//            })
//        }

        patientHomeViewModel.allMedicinLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                medicineRecyclerView.setList(it)
                Log.i(TAG, it.toString())
            }
        })

        medicineRecyclerView.onListItemClick = this

    }

    override fun onItemClick(info: AllMedicineRespone) {
        findNavController().navigate(PatientHomeFragmentDirections.actionPatientHomeFragmentToEditMedicineFragment(info._id,info.userId,retrivedToken))
        Log.i(TAG,info.name)
    }
}