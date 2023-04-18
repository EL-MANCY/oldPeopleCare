package com.example.oldpeoplecareapp.ui.Authentication.Registration

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_registration.*
import java.util.*

class RegistrationFragment : Fragment() {
    val TAG = "RegistrationFragment"
    lateinit var binding: FragmentRegistrationBinding
    lateinit var regViewModel: RegViewModel
    lateinit var fcm_token:String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val loading=LoadingDialog(requireActivity())

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        fcm_token = getpreferences.getString("FCMTOKEN", null).toString()

        regViewModel= ViewModelProvider(requireActivity()).get(RegViewModel::class.java)

/////////////////////////////////////////////////////////////////////////////////////////////

        val genderitems = resources.getStringArray(R.array.gender)
        val spinnerAdapter2 = object :
            ArrayAdapter<String>(requireContext(), R.layout.errorspinner_item, genderitems) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if (position == 0) {
                    val color = resources.getColor(android.R.color.holo_red_dark)
                    (view as TextView).setTextColor(color)
                    view.setTextAppearance(R.style.MyTextStyle2)
                } else {
                    //here it is possible to define color for other items by
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)                }
                return view
            }
        }
        val spinnerAdapter = object :
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item, genderitems) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if (position == 0) {
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)
                    view.setTextAppearance(R.style.MyTextStyle)
                } else {
                    //here it is possible to define color for other items by
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)                 }
                return view
            }
        }
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_checked)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked)
        binding.gender.adapter = spinnerAdapter
        binding.gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val value = parent!!.selectedItem.toString()
                if (value == genderitems[0]) {
                    val color = resources.getColor(android.R.color.holo_red_dark)
                    (view as TextView).setTextColor(color)                      }
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == genderitems[0]) {
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)                      }
                else{
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)

                }
            }
        }

/////////////////////////////////////////////////////////////////////////////////////////////////

        val registitems = resources.getStringArray(R.array.regist)
        val spinnerAdaptererror = object :
            ArrayAdapter<String>(requireContext(), R.layout.errorspinner_item, registitems) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if (position == 0) {
                    val color = resources.getColor(android.R.color.holo_red_dark)
                    (view as TextView).setTextColor(color)
                    view.setTextAppearance(R.style.MyTextStyle2)
                } else {
                    //here it is possible to define color for other items by
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)                }
                return view
            }
        }
        val spinnerAdaptertrue = object :
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item, registitems) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if (position == 0) {
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)
                    view.setTextAppearance(R.style.MyTextStyle)
                } else {
                    //here it is possible to define color for other items by
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)                 }
                return view
            }
        }
        spinnerAdaptertrue.setDropDownViewResource(android.R.layout.simple_list_item_checked)
        spinnerAdaptererror.setDropDownViewResource(android.R.layout.simple_list_item_checked)
        binding.registerAsX.adapter = spinnerAdaptertrue
        binding.registerAsX.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val value = parent!!.selectedItem.toString()
                if (value == registitems[0]) {
                    val color = resources.getColor(android.R.color.holo_red_dark)
                    (view as TextView).setTextColor(color)                      }
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == registitems[0]) {
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)                      }
                else{
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)

                }
            }
        }

///////////////////////////////////////////////////////////////////////////////////////////////////

        val calendar=Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        binding.DateOfBirth.setOnClickListener {
            binding.DateOfBirth.setHintTextColor( getResources().getColor(R.color.normal));
            binding.DateOfBirth.setBackgroundResource(R.drawable.et_style)
            binding.DateOfBirth.hint="   Date Of Birth"
            val datePickerDialog=DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { it, year, month, dayOfMonth ->
                binding.DateOfBirth.text = "   $year-${month+1}-$dayOfMonth"
            },year,month,day)
            datePickerDialog.show()
        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        binding.LoginPath.setOnClickListener {
            it.findNavController().navigate(RegistrationFragmentDirections.actionRegistrationToLogIn())
        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        binding.add.setOnClickListener {
            val fullname = binding.fullname.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val dateOfBirth = binding.DateOfBirth.text.toString()
            val gender = binding.gender.selectedItem.toString()
            val registerAs = binding.registerAsX.selectedItem.toString()
            val password = binding.password.text.toString()

            //////////////////////////////////////////////////////////////////

            if(binding.DateOfBirth.text.toString().isNullOrEmpty()){
                binding.DateOfBirth.setHintTextColor( getResources().getColor(R.color.holo));
                binding.DateOfBirth.setBackgroundResource(R.drawable.error_style)
                binding.DateOfBirth.hint="  Date Of Birth"
            }

            //////////////////////////////////////////////////////////////////

            if(binding.fullname.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.fullnameX,requireContext(), android.R.color.holo_red_dark)
                binding.fullname.setBackgroundResource(R.drawable.error_style)
                binding.fullnameX.hint="Full Name"
                binding.fullname.setOnClickListener {
                    setTextInputLayoutHintColor(binding.fullnameX,requireContext(), R.color.normal)
                    binding.fullname.setBackgroundResource(R.drawable.et_style)
                    binding.fullnameX.hint="Full Name"
                }
            }

            //////////////////////////////////////////////////////////////////

            if(binding.email.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.emailX,requireContext(), android.R.color.holo_red_dark)
                binding.email.setBackgroundResource(R.drawable.error_style)
                binding.emailX.hint="E-Mail"
                binding.email.setOnClickListener {
                    setTextInputLayoutHintColor(binding.emailX,requireContext(), R.color.normal)
                    binding.email.setBackgroundResource(R.drawable.et_style)
                    binding.emailX.hint="E-Mail"
                }
            }

            //////////////////////////////////////////////////////////////////

            if(binding.phone.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.phoneX,requireContext(), android.R.color.holo_red_dark)
                binding.phone.setBackgroundResource(R.drawable.error_style)
                binding.phoneX.hint="Phone"
                binding.phone.setOnClickListener {
                    setTextInputLayoutHintColor(binding.phoneX,requireContext(), R.color.normal)
                    binding.phone.setBackgroundResource(R.drawable.et_style)
                    binding.phoneX.hint="Phone"
                }
            }

            //////////////////////////////////////////////////////////////////

            if(binding.gender.selectedItem==genderitems[0]){
                binding.gender.adapter = spinnerAdapter2
                binding.gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        val value = parent!!.selectedItem.toString()
                        if (value == genderitems[0]) {
                            val color = resources.getColor(android.R.color.holo_red_dark)
                            (view as TextView).setTextColor(color)
                        }
                    }
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val value = parent!!.getItemAtPosition(position).toString()
                        if (value == genderitems[0]) {
                            val color = resources.getColor(android.R.color.holo_red_dark)
                            (view as TextView).setTextColor(color)                        }
                        else{
                            val color = resources.getColor(R.color.bbbb)
                            (view as TextView).setTextColor(color)

                        }
                    }
                }
            }

            //////////////////////////////////////////////////////////////////

            if(binding.registerAsX.selectedItem==registitems[0]){
                binding.registerAsX.adapter = spinnerAdaptererror
                binding.registerAsX.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        val value = parent!!.selectedItem.toString()
                        if (value == registitems[0]) {
                            val color = resources.getColor(android.R.color.holo_red_dark)
                            (view as TextView).setTextColor(color)
                        }
                    }
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val value = parent!!.getItemAtPosition(position).toString()
                        if (value == registitems[0]) {
                            val color = resources.getColor(android.R.color.holo_red_dark)
                            (view as TextView).setTextColor(color)                        }
                        else{
                            val color = resources.getColor(R.color.bbbb)
                            (view as TextView).setTextColor(color)

                        }
                    }
                }
            }

            //////////////////////////////////////////////////////////////////

            if(binding.password.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.passwordX,requireContext(), R.color.holo)
                binding.password.setBackgroundResource(R.drawable.error_style)
                binding.passwordX.hint="Password"
                binding.password.setOnClickListener {
                    setTextInputLayoutHintColor(binding.passwordX,requireContext(), R.color.normal)
                    binding.password.setBackgroundResource(R.drawable.et_style)
                    binding.passwordX.hint="Password"
                }
            }

            //////////////////////////////////////////////////////////////////

            if(!binding.confirmpassword.text.toString().equals(binding.password.text.toString()) || binding.confirmpassword.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.confirmpasswordX,requireContext(), android.R.color.holo_red_dark)
                binding.confirmpassword.setBackgroundResource(R.drawable.error_style)
                binding.confirmpasswordX.hint="Pasword Doesn't Match"
                binding.confirmpassword.setOnClickListener {
                    setTextInputLayoutHintColor(binding.confirmpasswordX,requireContext(), R.color.normal)
                    binding.confirmpassword.setBackgroundResource(R.drawable.et_style)
                    binding.confirmpasswordX.hint="Confirm Password"
                }
            }

            //////////////////////////////////////////////////////////////////

            if (!binding.fullname.text.toString().isNullOrEmpty() && !binding.email.text.toString().isNullOrEmpty() &&
                !binding.phone.text.toString().isNullOrEmpty() && !(binding.gender == null) &&
                !(binding.registerAsX == null) && !binding.password.text.toString().isNullOrEmpty() &&
                !binding.confirmpassword.text.toString().isNullOrEmpty() && binding.confirmpassword.text.toString().equals(binding.password.text.toString())) {
                regViewModel.addUsersAPI(
                    fullname,
                    email,
                    phone,
                    dateOfBirth,
                    gender,
                    registerAs,
                    password,
                    fcm_token
                )
                loading.startLoading()
            }
        }

        //////////////////////////////////////////////////////////////////

        regViewModel.addUserAPILiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                loading.isDismiss()
                findNavController()
                    .navigate(RegistrationFragmentDirections.actionRegistrationToLogIn())
            } else if(regViewModel.error !=null) {
                Log.i(TAG, "not")
                Snackbar.make(
                    REGIS,
                    regViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                regViewModel.error =null
            }
        }

    }

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }

}