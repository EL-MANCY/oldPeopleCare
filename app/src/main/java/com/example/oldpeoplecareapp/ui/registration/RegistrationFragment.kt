package com.example.oldpeoplecareapp.ui.registration

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.ColorRes
import androidx.compose.ui.text.android.InternalPlatformTextApi
import androidx.compose.ui.text.android.TextLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentRegistrationBinding
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class RegistrationFragment : Fragment() {
    lateinit var binding: FragmentRegistrationBinding
    // lateinit var remoteRepositoryImp: RemoteRepositoryImp
    lateinit var regViewModel:RegViewModel

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loading=LoadingDialog(requireActivity())

//        val serviceInstant = RetroBuilder.builder
//        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
        regViewModel= ViewModelProvider(requireActivity()).get(RegViewModel::class.java)

        val gender = listOf("Male", "Female")
        val adapterG = ArrayAdapter(requireContext(), R.layout.spinner_item, gender)
        (binding.genderX.editText as? AutoCompleteTextView)?.setAdapter(adapterG)

        val register = listOf("Patient", "Care Giver")
        val adapterR = ArrayAdapter(requireContext(), R.layout.spinner_item, register)
        (binding.registerAsX.editText as? AutoCompleteTextView)?.setAdapter(adapterR)

        val calendar=Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        binding.DateOfBirth.setOnClickListener {
            binding.DateOfBirth.setHintTextColor( getResources().getColor(R.color.normal));
            binding.DateOfBirth.setBackgroundResource(R.drawable.et_style)
            binding.DateOfBirth.hint="   Date Of Birth"
            val datePickerDialog=DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { it, year, month, dayOfMonth ->
                binding.DateOfBirth.text = "   $year-$month-$dayOfMonth"
            },year,month,day)
            datePickerDialog.show()
        }
        binding.LoginPath.setOnClickListener {
            it.findNavController().navigate(RegistrationFragmentDirections.actionRegistrationToLogIn())
        }
        binding.add.setOnClickListener {
            val fullname = binding.fullname.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val dateOfBirth = binding.DateOfBirth.text.toString()
            val gender = binding.genderX.editText!!.text.toString()
            val registerAs = binding.registerAsX.editText!!.text.toString()
            val password = binding.password.text.toString()


            if(binding.DateOfBirth.text.toString().isNullOrEmpty()){
                binding.DateOfBirth.setHintTextColor( getResources().getColor(R.color.holo));
                binding.DateOfBirth.setBackgroundResource(R.drawable.error_style)
                binding.DateOfBirth.hint="   Date Of Birth is Required"
            }


            if(binding.fullname.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.fullnameX,requireContext(), android.R.color.holo_red_dark)
                binding.fullname.setBackgroundResource(R.drawable.error_style)
                binding.fullnameX.hint="Full Name is Required"
                binding.fullname.setOnClickListener {
                    setTextInputLayoutHintColor(binding.fullnameX,requireContext(), R.color.normal)
                    binding.fullname.setBackgroundResource(R.drawable.et_style)
                    binding.fullnameX.hint="Full Name"                }
            }
            if(binding.email.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.emailX,requireContext(), android.R.color.holo_red_dark)
                binding.email.setBackgroundResource(R.drawable.error_style)
                binding.emailX.hint="E-Mail is Required"
                binding.email.setOnClickListener {
                    setTextInputLayoutHintColor(binding.emailX,requireContext(), R.color.normal)
                    binding.email.setBackgroundResource(R.drawable.et_style)
                    binding.emailX.hint="E-Mail"
                }
            }
            if(binding.phone.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.phoneX,requireContext(), android.R.color.holo_red_dark)
                binding.phone.setBackgroundResource(R.drawable.error_style)
                binding.phoneX.hint="Phone is Required"
                binding.phone.setOnClickListener {
                    setTextInputLayoutHintColor(binding.phoneX,requireContext(), R.color.normal)
                    binding.phone.setBackgroundResource(R.drawable.et_style)
                    binding.phoneX.hint="Phone"                }
            }
            if(binding.genderX.editText!!.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.genderX,requireContext(), android.R.color.holo_red_dark)
                binding.genderX.editText!!.setBackgroundResource(R.drawable.error_style)
                binding.genderX.hint="Gender is Required"
                binding.genderX.editText!!.setOnClickListener {
                    setTextInputLayoutHintColor(binding.genderX,requireContext(), R.color.normal)
                    binding.genderX.editText!!.setBackgroundResource(R.drawable.et_style)
                    binding.phoneX.hint="Gender"                      }
            }
            if(binding.registerAsX.editText!!.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.registerAsX,requireContext(), android.R.color.holo_red_dark)
                binding.registerAsX.editText!!.setBackgroundResource(R.drawable.error_style)
                binding.registerAsX.hint="User Type is Required"
                binding.registerAsX.editText!!.setOnClickListener {
                    setTextInputLayoutHintColor(binding.registerAsX,requireContext(), R.color.normal)
                    binding.registerAsX.editText!!.setBackgroundResource(R.drawable.et_style)
                    binding.registerAsX.hint="Register As"                          }
            }
            if(binding.password.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.passwordX,requireContext(), android.R.color.holo_red_dark)
                binding.password.setBackgroundResource(R.drawable.error_style)
                binding.passwordX.hint="Password is Required"
                binding.password.setOnClickListener {
                    setTextInputLayoutHintColor(binding.passwordX,requireContext(), R.color.normal)
                    binding.password.setBackgroundResource(R.drawable.et_style)
                    binding.passwordX.hint="Password"                          }
            }
            if(!binding.confirmpassword.text.toString().equals(binding.password.text.toString()) || binding.confirmpassword.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.confirmpasswordX,requireContext(), android.R.color.holo_red_dark)
                binding.confirmpassword.setBackgroundResource(R.drawable.error_style)
                binding.confirmpasswordX.hint="Pasword Doesn't Match"
                binding.confirmpassword.setOnClickListener {
                    setTextInputLayoutHintColor(binding.confirmpasswordX,requireContext(), R.color.normal)
                    binding.confirmpassword.setBackgroundResource(R.drawable.et_style)
                    binding.confirmpasswordX.hint="Confirm Password"                                    }
            }
            if (!binding.fullname.text.toString().isNullOrEmpty() && !binding.email.text.toString().isNullOrEmpty() &&
                !binding.phone.text.toString().isNullOrEmpty() && !binding.genderX.editText!!.text.toString().isNullOrEmpty() &&
                !binding.registerAsX.editText!!.text.toString().isNullOrEmpty() && !binding.password.text.toString().isNullOrEmpty() &&
                !binding.confirmpassword.text.toString().isNullOrEmpty() && binding.confirmpassword.text.toString().equals(binding.password.text.toString())) {
//                it.findNavController()
//                    .navigate(RegistrationDirections.actionRegistrationToLogIn())
                regViewModel.addUsersAPI(
                    fullname,
                    email,
                    phone,
                    dateOfBirth,
                    gender,
                    registerAs,
                    password
                )
                Log.i("go","done")
                loading.startLoading()


            }
        }
        regViewModel.addUserAPILiveData.observe(viewLifecycleOwner) {
            Log.i("outIf", "yes")
            if (it != null) {
                loading.isDismiss()
                findNavController()
                    .navigate(RegistrationFragmentDirections.actionRegistrationToLogIn())
                Log.i("ifObserve", "yes")
            } else {
                Log.i("elseObserve", "not")

            }
        }
    }

}













//                val result = remoteRepositoryImp.addNewUser(
//                        "mancy",
//                        "abdel@gmail.com",
//                        "01554500089",
//                        "2002-12-09",
//                        "Male",
//                        "Patient",
//                        "12AAA4599789Abdl@"
//
//                )


//        binding.DateOfBirth.setOnClickListener {
//            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
//        }