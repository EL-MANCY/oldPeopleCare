package com.example.oldpeoplecareapp.ui.PatientPath.AddNewcaregiverPatient

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAddNewcaregiverPatientBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_newcaregiver_patient.*

class AddNewcaregiverPatientFragment : Fragment() {

    val TAG="AddCareGiver"
    lateinit var binding: FragmentAddNewcaregiverPatientBinding
    lateinit var addNewcaregiverViewModel: AddNewcaregiverPatientViewModel
    lateinit var Role: String
    lateinit var Email: String
    lateinit var retrivedToken: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewcaregiverPatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val items = resources.getStringArray(R.array.caregivers)
        val spinnerAdapter = object :
            ArrayAdapter<String>(requireContext(), R.layout.spinner_items, items) {
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
                    view.setTextColor(Color.GRAY)
                    view.setTextAppearance(R.style.MyTextStyle)
                } else {
                    //here it is possible to define color for other items by
                    view.setTextColor(Color.BLACK)
                }
                return view
            }

        }
        //spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner2.setBackgroundResource(R.drawable.spinner_background)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked)
        binding.spinner2.setBackgroundColor(getResources().getColor(R.color.grey))
        binding.spinner2.adapter = spinnerAdapter

        binding.spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == items[0]) {
                    (view as TextView).setTextColor(Color.BLACK)
                }
            }
        }

        addNewcaregiverViewModel =
            ViewModelProvider(requireActivity()).get(AddNewcaregiverPatientViewModel::class.java)

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()

        val loading= LoadingDialog(requireActivity())


        binding.sendbtn.setOnClickListener {
            Email = binding.editTextEmailAddress.text.toString()
            Role = binding.spinner2.selectedItem.toString()
            if (binding.spinner2 == null || binding.editTextEmailAddress.text.isNullOrEmpty()) {
                Snackbar.make(
                    REQ,
                    "PLEASE ENTER THE CORRECT EMAIL AND THE CAREGIVER ROLE",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                addNewcaregiverViewModel.sendReq("barier " + retrivedToken, Email,Role)
                Log.i("token",retrivedToken)
                loading.startLoading()
            }
        }
        addNewcaregiverViewModel.sucessLiveData.observe(viewLifecycleOwner){
            if (it != null) {
                loading.isDismiss()
                Log.i(TAG, it.toString())
                if(it=="You have already sent it before !"){
                    Snackbar.make(
                    REQ,
                    "You have already sent it before !",
                    Snackbar.LENGTH_SHORT
                ).show()
                    addNewcaregiverViewModel.sucessLiveData==null
                }else{
                    loading.isDismiss()
                    Snackbar.make(
                        REQ,
                        "Req sent",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    addNewcaregiverViewModel.sucessLiveData==null
                }
            }else{
                Log.i(TAG, it.toString())
            }
        }

    }
}


