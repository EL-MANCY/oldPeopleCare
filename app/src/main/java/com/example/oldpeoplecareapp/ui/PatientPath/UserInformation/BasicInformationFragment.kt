package com.example.oldpeoplecareapp.ui.PatientPath.UserInformation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentBasicInformationBinding
import com.example.oldpeoplecareapp.databinding.FragmentPatientNotificationBinding
import com.example.oldpeoplecareapp.ui.PatientPath.EditRemoveCareGiver.EditRemoveCaregiverRoleDirections
import com.example.oldpeoplecareapp.ui.PatientPath.PatientNotification.PatientNotificationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_basic_information.*
import kotlinx.android.synthetic.main.fragment_edit_remove_caregiver_role.*


class BasicInformationFragment : Fragment() {

    lateinit var binding: FragmentBasicInformationBinding
    lateinit var userInfoViewModel: UserInfoViewModel
    lateinit var retrivedToken:String
    lateinit var retrivedID:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBasicInformationBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navBar2 = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation2)
        navBar.visibility=View.GONE
        navBar2.visibility=View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        retrivedID = getpreferences.getString("ID", null).toString()

        val loading = LoadingDialog(requireActivity())

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        userInfoViewModel = ViewModelProvider(requireActivity()).get(UserInfoViewModel::class.java)
        userInfoViewModel.getUserInfo("barier " + retrivedToken,retrivedID)
        loading.isDismiss()

        binding.editbutton.setOnClickListener {
            findNavController().navigate(BasicInformationFragmentDirections.actionBasicInformationFragmentToEditUserFragment())

        }
        binding.logoutBtn.setOnClickListener {
            val navController = findNavController()
            navController.navigate(BasicInformationFragmentDirections.actionBasicInformationFragmentToLogIn())

            val sharedPreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
        }

        userInfoViewModel.UserLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                loading.isDismiss()
                binding.userName.setText(it.fullname)
                binding.mailTitle.setText(it.email)
                binding.nametxtX.editText!!.setText(it.fullname)
                binding.phonetxtx.editText!!.setText(it.phone)
                binding.mailtxtx.editText!!.setText(it.email)
                val d = it.dateOfBirth.subSequence(0,10)
                binding.datetxtx.editText!!.setText(d)
                binding.gendertxtx.editText!!.setText(it.gender)
                binding.regtextx.editText!!.setText(it.registAs)
                Glide.with(this).load(it.image.url).into(binding.userpicture)

            } else if(userInfoViewModel.error!=null) {
                loading.isDismiss()
                Snackbar.make(
                    USERINFO,
                    userInfoViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                userInfoViewModel.error =null
            }

        })


    }


}