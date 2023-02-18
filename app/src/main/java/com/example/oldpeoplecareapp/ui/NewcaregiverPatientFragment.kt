package com.example.oldpeoplecareapp.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentNewcaregiverPatientBinding
import com.example.oldpeoplecareapp.databinding.FragmentRegistrationBinding

class NewcaregiverPatientFragment : Fragment() {
    lateinit var binding: FragmentNewcaregiverPatientBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewcaregiverPatientBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val items = resources.getStringArray(R.array.caregivers)
        val spinnerAdapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, items) {
                override fun isEnabled(position: Int): Boolean {
                    // Disable the first item from Spinner
                    // First item will be used for hint
                    return position != 0
                }
                @RequiresApi(Build.VERSION_CODES.M)
                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
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
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner2.setBackgroundColor(getResources().getColor(R.color.grey))

        binding.spinner2.adapter = spinnerAdapter

        binding.spinner2.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val value = parent!!.getItemAtPosition(position).toString()
                if(value == items[0]){
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }
        }
    }
}