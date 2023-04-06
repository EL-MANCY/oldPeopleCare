package com.example.oldpeoplecareapp.ui.Authentication.login

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentLogInBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {
    lateinit var binding: FragmentLogInBinding
    lateinit var logInViewModel: LogInViewModel
    lateinit var fcm_token: String
    val TAG = "LogInFragment"

    private fun setTextInputLayoutHintColor(
        textInputLayout: TextInputLayout,
        context: Context,
        @ColorRes colorIdRes: Int
    ) {
        textInputLayout.defaultHintTextColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loading = LoadingDialog(requireActivity())

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        fcm_token = getpreferences.getString("FCMTOKEN", null).toString()

        logInViewModel = ViewModelProvider(requireActivity()).get(LogInViewModel::class.java)

        binding.RegisterPath.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInToRegistration())
        }

        binding.LogInBtn.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()

            if (binding.emailLogin.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.emailXLogin,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.emailLogin.setBackgroundResource(R.drawable.error_style)
                binding.emailXLogin.hint = "E-Mail is Required"
                binding.emailLogin.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.emailXLogin,
                        requireContext(),
                        R.color.normal
                    )
                    binding.emailLogin.setBackgroundResource(R.drawable.et_style)
                    binding.emailXLogin.hint = "E-Mail"
                }
            }
            if (binding.passwordLogin.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.passwordXLogin,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.passwordLogin.setBackgroundResource(R.drawable.error_style)
                binding.passwordXLogin.hint = "Password is Required"

                binding.passwordLogin.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.passwordXLogin,
                        requireContext(),
                        R.color.normal
                    )
                    binding.passwordLogin.setBackgroundResource(R.drawable.et_style)
                    binding.passwordXLogin.hint = "Password"
                }
            }


            if (!binding.emailLogin.text.toString().isNullOrEmpty() &&
                !binding.passwordLogin.text.toString().isNullOrEmpty()
            ) {
                logInViewModel.logIn("email", email, password, fcm_token)
                loading.startLoading()
            }
        }
        binding.forgetpath.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInToForgetPasswordFragment())
        }

        logInViewModel.tokenLiveData.observe(viewLifecycleOwner) {status->
           when(status){
               is LogInViewModel.LogInStatus.error->{
                   Log.i(TAG, status.errormessage)
                   loading.isDismiss()
                   Snackbar.make(LOG, status.errormessage, Snackbar.LENGTH_SHORT).show()
               }

               is LogInViewModel.LogInStatus.sucess->{
                   loading.isDismiss()

                   val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
                   preferences.edit().putString("ID", status.result!!.id).apply()
                   preferences.edit().putString("TOKEN", status.result!!.token).apply()
                   preferences.edit().putString("REGIST", status.result!!.registAs).apply()

                   if (status.result!!.registAs == "patient") {
                       findNavController().navigate(LogInFragmentDirections.actionLogInToPatientHomeFragment(status.result!!.token, status.result!!.registAs, status.result!!.id))
                   } else {
                       findNavController().navigate(LogInFragmentDirections.actionLogInToCaregiveHomeFragment())
                   }
               }

           }
        }
    }
}