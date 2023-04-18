package com.example.oldpeoplecareapp.ui.Authentication.CodeNo

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
import com.example.oldpeoplecareapp.databinding.FragmentCodeNumberBinding
import com.example.oldpeoplecareapp.databinding.FragmentForgetPasswordBinding
import com.example.oldpeoplecareapp.ui.Authentication.ForgetPassword.ForgetPassViewModel
import com.example.oldpeoplecareapp.ui.Authentication.ForgetPassword.ForgetPasswordFragmentDirections
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_code_number.*
import kotlinx.android.synthetic.main.fragment_forget_password.*

class CodeNumberFragment : Fragment() {
    lateinit var binding:FragmentCodeNumberBinding
    lateinit var codeNumViewModel: CodeNumViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentCodeNumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        codeNumViewModel = ViewModelProvider(requireActivity()).get(CodeNumViewModel::class.java)
        val loading = LoadingDialog(requireActivity())

        binding.backBtn.setOnClickListener {
            findNavController().navigate(CodeNumberFragmentDirections.actionCodeNumberFragmentToForgetPasswordFragment())
        }
        val args =CodeNumberFragmentArgs.fromBundle(requireArguments())
        binding.resendbtn.setOnClickListener {
            codeNumViewModel.resetPass(args.email)
            loading.startLoading()
        }
        binding.resetpassbtn.setOnClickListener {
            if (binding.codeX.editText!!.text.isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.codeX,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.code.setBackgroundResource(R.drawable.error_style)
                binding.codeX.hint = "Code is Required"
                binding.code.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.codeX,
                        requireContext(),
                        R.color.normal
                    )
                    binding.code.setBackgroundResource(R.drawable.et_style)
                    binding.codeX.hint = "Code"
                }
            } else {
                val token = binding.codeX.editText!!.text.toString()
                codeNumViewModel.sendCode(token)
                loading.startLoading()
            }

            codeNumViewModel.codeLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    loading.isDismiss()
                    Snackbar.make(CODE, "The Code Is Correct", Snackbar.LENGTH_SHORT).show()
                }else if(codeNumViewModel.error != null){
                    loading.isDismiss()
                    Snackbar.make(CODE, "The Code Is Not Correct", Snackbar.LENGTH_SHORT).show()
                    codeNumViewModel.error = null
                }
            })

            codeNumViewModel.responseLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    loading.isDismiss()
                    Snackbar.make(CODE, "Check Your Mail Inbox", Snackbar.LENGTH_SHORT).show()
                }else if(codeNumViewModel.error != null){
                    loading.isDismiss()
                    Snackbar.make(CODE, "Check Your Mail Inbox", Snackbar.LENGTH_SHORT).show()
                    codeNumViewModel.error = null
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