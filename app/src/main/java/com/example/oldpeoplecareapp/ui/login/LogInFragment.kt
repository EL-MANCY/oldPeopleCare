package com.example.oldpeoplecareapp.ui.login

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
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentLogInBinding
import com.example.oldpeoplecareapp.model.remote.RemoteRepositoryImp
import com.example.oldpeoplecareapp.model.remote.RetroBuilder
import com.example.oldpeoplecareapp.ui.registration.RegViewModel
import com.example.oldpeoplecareapp.ui.registration.RegistrationFragmentDirections
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LogInFragment : Fragment() {
    lateinit var binding: FragmentLogInBinding
    lateinit var logInViewModel: LogInViewModel
    lateinit var fcm_token:String

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val loading= LoadingDialog(requireActivity())

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
                setTextInputLayoutHintColor(binding.emailXLogin,requireContext(), android.R.color.holo_red_dark)
                binding.emailLogin.setBackgroundResource(R.drawable.error_style)
                binding.emailXLogin.hint="E-Mail is Required"
                binding.emailLogin.setOnClickListener {
                    setTextInputLayoutHintColor(binding.emailXLogin,requireContext(), R.color.normal)
                    binding.emailLogin.setBackgroundResource(R.drawable.et_style)
                    binding.emailXLogin.hint="E-Mail"
                }
            }
            if (binding.passwordLogin.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(binding.passwordXLogin,requireContext(), android.R.color.holo_red_dark)
                binding.passwordLogin.setBackgroundResource(R.drawable.error_style)
                binding.passwordXLogin.hint="Password is Required"

                binding.passwordLogin.setOnClickListener {
                    setTextInputLayoutHintColor(binding.passwordXLogin,requireContext(), R.color.normal)
                    binding.passwordLogin.setBackgroundResource(R.drawable.et_style)
                    binding.passwordXLogin.hint="Password"}
                }


            if (!binding.emailLogin.text.toString().isNullOrEmpty() &&
                !binding.passwordLogin.text.toString().isNullOrEmpty()
                    ) {
                logInViewModel.logIn("email", email, password,fcm_token)
                loading.startLoading()

            }

        }
        logInViewModel.tokenLiveData.observe(viewLifecycleOwner){
            if (it != null) {
                loading.isDismiss()
                val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
                preferences.edit().putString("ID", logInViewModel.tokenLiveData.value!!.id).apply()
                preferences.edit().putString("TOKEN", logInViewModel.tokenLiveData.value!!.token).apply()
                preferences.edit().putString("REGIST", logInViewModel.tokenLiveData.value!!.registAs).apply()

                findNavController()
                    .navigate(LogInFragmentDirections.actionLogInToPatientHomeFragment(logInViewModel.tokenLiveData.value!!.token,logInViewModel.tokenLiveData.value!!.registAs,logInViewModel.tokenLiveData.value!!.id))
                Log.i("ifObserve", "yes")
            } else {
                Log.i("elseObserve", "not")
            }
        }
    }

}










//            GlobalScope.launch(Dispatchers.IO) {
//                val token=remoteRepositoryImp.logIn("email",email,password)
//                if(token.isSuccessful){
//                    Log.i("success",token.body().toString())
//                }else{
//                    Log.i("failed",token.toString())
//                }
//
//            }