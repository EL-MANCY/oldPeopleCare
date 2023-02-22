package com.example.oldpeoplecareapp.ui.CaregiversPatient

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAddNewcaregiverPatientBinding
import com.example.oldpeoplecareapp.databinding.FragmentCaregiversPatientBinding
import com.example.oldpeoplecareapp.databinding.FragmentPatientHomeBinding
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.ui.patientHome.MedicineRecyclerView
import com.example.oldpeoplecareapp.ui.patientHome.PatientHomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class CaregiversPatientFragment : Fragment() {
    lateinit var binding: FragmentCaregiversPatientBinding

    val TAG: String = "CaregiversFragment"
    lateinit var caregiversPatientViewModel: CaregiversPatientViewModel

    lateinit var  retrivedToken:String
    val circleRecyclerView by lazy { CircleRecyclerView() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCaregiversPatientBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        val retrivedID = getpreferences.getString("ID", null)

        caregiversPatientViewModel =
            ViewModelProvider(requireActivity()).get(CaregiversPatientViewModel::class.java)

        caregiversPatientViewModel.getCircles("barier " + retrivedToken)


        binding.circlesRecyclerView.adapter = circleRecyclerView

        caregiversPatientViewModel.CircleLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                circleRecyclerView.setList(it)
                Log.i(TAG, it.toString())
            }
        })


    }
}