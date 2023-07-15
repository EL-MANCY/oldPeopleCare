package com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient

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
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentCaregiversPatientBinding
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.PatientHomeFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_all_alarms.*
import kotlinx.android.synthetic.main.fragment_caregivers_patient.*
import kotlinx.android.synthetic.main.fragment_edit_remove_caregiver_role.*

class CaregiversPatientFragment : Fragment(),OnCaregiverClickListener {

    val TAG: String = "CaregiversFragment"
    lateinit var binding: FragmentCaregiversPatientBinding
    lateinit var caregiversPatientViewModel: CaregiversPatientViewModel
    lateinit var  retrivedToken:String
    val circleRecyclerView by lazy { CircleRecyclerView() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCaregiversPatientBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        navBar?.selectedItemId =R.id.caregiver_icon
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
        binding.addCaregiverBtn.setOnClickListener {
            findNavController().navigate(CaregiversPatientFragmentDirections.actionCaregiversPatientFragmentToAddNewcaregiverPatientFragment())
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        caregiversPatientViewModel.CircleLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                circleRecyclerView.setList(it)
                if(circleRecyclerView.CircleList.isEmpty()){
                    binding.base.visibility=View.VISIBLE
                }else{
                    binding.base.visibility=View.GONE

                }

                Log.i(TAG, it.toString())
            }else if(caregiversPatientViewModel.error != null){
                Snackbar.make(
                    CAREGIVERP,
                    caregiversPatientViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            caregiversPatientViewModel.error = null
        })
        if(circleRecyclerView.CircleList.isEmpty()){
            binding.base.visibility=View.VISIBLE
        }else{
            binding.base.visibility=View.GONE

        }

        circleRecyclerView.onListItemClick = this

        binding.Search.setOnClickListener {
            findNavController().navigate(CaregiversPatientFragmentDirections.actionCaregiversPatientFragmentToSearchFragment())
        }
        binding.chatIcon.setOnClickListener {
            findNavController().navigate(CaregiversPatientFragmentDirections.actionCaregiversPatientFragmentToChatContactsFragments())

        }
    }

    override fun onItemClick(info: Circles) {
        findNavController().navigate(CaregiversPatientFragmentDirections.actionCaregiversPatientFragmentToEditRemoveCaregiverRole(info.id._id))
        Log.i(TAG,"clicked")
    }

    override fun onChatClick(info: Circles) {
        findNavController().navigate(CaregiversPatientFragmentDirections.actionCaregiversPatientFragmentToChatFragment(info.id._id,info.id.image.url,info.id.fullname))
    }
}