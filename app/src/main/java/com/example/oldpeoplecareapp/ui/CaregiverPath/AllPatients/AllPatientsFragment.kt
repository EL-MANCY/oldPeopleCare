package com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAllPatientsBinding
import com.example.oldpeoplecareapp.model.entity.Circles
import com.google.android.material.bottomnavigation.BottomNavigationView


class AllPatientsFragment : Fragment(),OnItemClickListener2 {
    val TAG = "AllPatientsFragment"
    lateinit var binding:FragmentAllPatientsBinding
    lateinit var allPatientViewModel: AllPatientViewModel
    lateinit var  retrivedToken:String
    val patientsRecyclerView by lazy { PatientsRecyclerView() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAllPatientsBinding.inflate(inflater, container, false)
        val navBar2 = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation2)
        navBar2.visibility = View.VISIBLE
        navBar2?.selectedItemId = R.id.patients_icon
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()

        allPatientViewModel =
            ViewModelProvider(requireActivity()).get(AllPatientViewModel::class.java)
        allPatientViewModel.getCircles("barier " + retrivedToken)

        binding.caregiverPatientsRecyclerView.adapter = patientsRecyclerView

        allPatientViewModel.CircleLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                patientsRecyclerView.setList(it)
                Log.i(TAG, it.toString())
            }
        })
        patientsRecyclerView.onListItemClick2 = this

    }
    override fun onItemClick2(info: Circles) {
        val phoneNumber = info.id.phone // Replace with the phone number you want to call
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
        view?.context?.startActivity(intent)
    }


}