package com.example.oldpeoplecareapp.ui.Authentication.ForgetPassword

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentForgetPasswordBinding
import com.example.oldpeoplecareapp.ui.Authentication.login.LogInViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_forget_password.*

class ForgetPasswordFragment : Fragment() {
    lateinit var binding: FragmentForgetPasswordBinding
    lateinit var forgetPassViewModel: ForgetPassViewModel
    lateinit var email:String


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        forgetPassViewModel = ViewModelProvider(requireActivity()).get(ForgetPassViewModel::class.java)
        val loading = LoadingDialog(requireActivity())

        binding.loginbtn.setOnClickListener {
            findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToLogIn())
        }

        binding.sendCode.setOnClickListener {
            if (binding.forgetemailX.editText!!.text.isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.forgetemailX,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.forgetemail.setBackgroundResource(R.drawable.error_style)
                binding.forgetemailX.hint = "E-Mail is Required"
                binding.forgetemail.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.forgetemailX,
                        requireContext(),
                        R.color.normal
                    )
                    binding.forgetemail.setBackgroundResource(R.drawable.et_style)
                    binding.forgetemailX.hint = "E-Mail"
                }
            } else {
                email = binding.forgetemailX.editText!!.text.toString()
                forgetPassViewModel.resetPass(email)
                loading.startLoading()
            }

            forgetPassViewModel.responseLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    loading.isDismiss()
                    Snackbar.make(FORGET, "Check Your Mail Inbox", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToCodeNumberFragment(email))
                }else if(forgetPassViewModel.error != null){
                    loading.isDismiss()
                    Snackbar.make(FORGET, "Check Your Mail Inbox", Snackbar.LENGTH_SHORT).show()
                    forgetPassViewModel.error = null
                }
            })
        }
    }

    private fun setTextInputLayoutHintColor(
        textInputLayout: TextInputLayout,
        context: Context,
        @ColorRes colorIdRes: Int
    ) {
        textInputLayout.defaultHintTextColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }
}