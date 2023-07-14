package com.example.oldpeoplecareapp.ui.CaregiverPath.CareGiverProfile

import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
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
import com.example.oldpeoplecareapp.databinding.FragmentCareGiverProfileBinding
import com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient.CaregiversPatientFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.EditRemoveCareGiver.EditRemoveCaregiverRoleArgs
import com.example.oldpeoplecareapp.ui.PatientPath.UserInformation.BasicInformationFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.UserInformation.UserInfoViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_newcaregiver_patient.*
import kotlinx.android.synthetic.main.fragment_basic_information.*
import kotlinx.android.synthetic.main.fragment_care_giver_profile.*
import kotlinx.android.synthetic.main.fragment_chat.*


class CareGiverProfileFragment : Fragment() {

    lateinit var binding: FragmentCareGiverProfileBinding
    lateinit var careGiverProfileViewModel: CareGiverProfileViewModel
    lateinit var retrivedToken: String
    lateinit var Role: String
    lateinit var Email: String
    lateinit var retrivedID: String
    lateinit var RecieverId: String
    lateinit var imageUrl: String
    lateinit var Fullname: String
    val TAG = "CareGiverProfileFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCareGiverProfileBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navBar2 = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation2)
        navBar.visibility = View.GONE
        navBar2.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        retrivedID = getpreferences.getString("ID", null).toString()

        val args = CareGiverProfileFragmentArgs.fromBundle(requireArguments())
        val caregiverId = args.id

        val loading = LoadingDialog(requireActivity())

        careGiverProfileViewModel =
            ViewModelProvider(requireActivity()).get(CareGiverProfileViewModel::class.java)
        careGiverProfileViewModel.getUserInfo("barier " + retrivedToken, caregiverId)
        loading.isDismiss()

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        careGiverProfileViewModel.UserLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                loading.isDismiss()
                if (it.isInCircle == true) {
                    binding.addReq.text = "Friends"
                    binding.addReq.isEnabled = false
                }

                if( !it.image.equals(null)) {
                    imageUrl = it.image.url
                    binding.userpicture.setBackgroundResource(R.drawable.oval)
                    Glide.with(this).load(imageUrl).into(binding.userpicture)
                }

                binding.userName.setText(it.fullname)
                binding.phonetxtx.editText!!.setText(it.phone)
                binding.mailtxtx.editText!!.setText(it.email)
                binding.datetxtx.editText!!.setText(it.dateOfBirth)
                binding.gendertxtx.editText!!.setText(it.gender)

                Email = it.email
                Fullname = it.fullname
                RecieverId = it._id
                imageUrl = it.image.url


            } else if (careGiverProfileViewModel.error != null) {
                loading.isDismiss()
                Snackbar.make(
                    CAREGIVERPROF,
                    careGiverProfileViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                careGiverProfileViewModel.error = null
            }

        })


        binding.addReq.setOnClickListener {
            Role = "viewer"
            careGiverProfileViewModel.sendReq("barier " + retrivedToken, Email, Role)
            Log.i("token", retrivedToken)
            loading.startLoading()

            careGiverProfileViewModel.snackBarLiveData.observe(viewLifecycleOwner) {
                Snackbar.make(view, it.toString(), Snackbar.LENGTH_SHORT).show()
                loading.isDismiss()
            }

            careGiverProfileViewModel.sucessLiveData.observe(viewLifecycleOwner) {
                if (it != null) {
                    loading.isDismiss()
                    Log.i(TAG, it.toString())
                    loading.isDismiss()
                    Snackbar.make(
                        view,
                        "Reqest sent",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else if (careGiverProfileViewModel.error != null) {
                    loading.isDismiss()
                    Snackbar.make(
                        view,
                        careGiverProfileViewModel.error.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    careGiverProfileViewModel.error = null
                }
            }
        }

        binding.chat.setOnClickListener {
            findNavController().navigate(
                CareGiverProfileFragmentDirections.actionCareGiverProfileFragmentToChatFragment(
                    RecieverId,
                    imageUrl,
                    Fullname
                )
            )

        }
    }
}


