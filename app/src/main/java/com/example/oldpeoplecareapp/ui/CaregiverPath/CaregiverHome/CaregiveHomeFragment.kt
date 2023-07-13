package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome

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
import com.example.oldpeoplecareapp.databinding.FragmentCaregiveHomeBinding
import com.example.oldpeoplecareapp.databinding.FragmentCaregiverNotificationsBinding
import com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients.AllPatientsFragmentDirections
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications.CaregiverNotifyRecyclerview
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications.CaregiverNotifyViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_new_medicine.*

class CaregiveHomeFragment : Fragment() {
    lateinit var binding: FragmentCaregiveHomeBinding
    private val TAG = "CaregiveHomeFragment"
    lateinit var caregiverHomeViewModel: CareGiverHomeViewModel
    lateinit var retrivedToken: String
    val caregiverHomeRecyclerview by lazy { CaregiverHomeRecyclerView(requireActivity()) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCaregiveHomeBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.GONE
        val navBar2 = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation2)
        navBar2.visibility = View.VISIBLE
        navBar2?.selectedItemId =R.id.home_page
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.userInfo.setOnClickListener {
           // findNavController().navigate(CaregiveHomeFragmentDirections.actionCaregiveHomeFragmentToBasicInformationFragment())
        }


        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()

        caregiverHomeViewModel = ViewModelProvider(requireActivity()).get(CareGiverHomeViewModel::class.java)
        caregiverHomeViewModel.getPatients("barier " + retrivedToken)

        binding.medicineRecyclerView.adapter = caregiverHomeRecyclerview

        caregiverHomeViewModel.MedItemLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                caregiverHomeRecyclerview.setList(it)
                Log.i(TAG, it.toString())
            }
        })
        binding.userInfo.setOnClickListener {
            findNavController().navigate(CaregiveHomeFragmentDirections.actionCaregiveHomeFragmentToBasicInformationFragment())
        }
        binding.searchIconX.editText!!.setOnClickListener{
            findNavController().navigate(CaregiveHomeFragmentDirections.actionCaregiveHomeFragmentToSearchFragment())

        }

    }


}