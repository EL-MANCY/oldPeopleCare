package com.example.oldpeoplecareapp.ui.PatientPath.EditRemoveCareGiver

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentEditRemoveCaregiverRoleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_edit_remove_caregiver_role.*


class EditRemoveCaregiverRole : Fragment() {

    lateinit var binding: FragmentEditRemoveCaregiverRoleBinding
    lateinit var editRemoveViewModel: EditRemoveViewModel
    lateinit var retrivedToken: String

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditRemoveCaregiverRoleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        val retrivedID = getpreferences.getString("ID", null)

        val args = EditRemoveCaregiverRoleArgs.fromBundle(requireArguments())
        val caregiverId = args.id
        //Retrieve token wherever necessary
        val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val retrivedToken = preferences.getString("TOKEN", null)
        editRemoveViewModel =
            ViewModelProvider(requireActivity()).get(EditRemoveViewModel::class.java)
        val loading = LoadingDialog(requireActivity())

        ////////////////////////////////////////////////////////////////////////////////////
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        navController = NavHostFragment.findNavController(this)

        val bottomNavigation: BottomNavigationView =requireActivity().findViewById(R.id.bottom_navigation)

        if (navController.currentDestination?.label == "fragment_registration"
            || navController.currentDestination?.label == "fragment_log_in"
            || navController.currentDestination?.label=="EditMedicineFragment"
            || navController.currentDestination?.label=="EditRemoveCaregiverRole"
        ) {
            bottomNavigation.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
        }


        ///////////////////////////////////////////////////////////////////////////////////////
        val roles = resources.getStringArray(R.array.caregivers)

        val spinnerAdapter2 = object :
            ArrayAdapter<String>(requireContext(), R.layout.errorspinner_item, roles) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

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
                    (view as TextView).setTextColor(color)
                }
                return view
            }
        }
        val spinnerAdapter = object :
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item, roles) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

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
                    (view as TextView).setTextColor(color)
                }
                return view
            }
        }

        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_checked)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked)
        binding.editCareGiveSpinner.adapter = spinnerAdapter
        binding.editCareGiveSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    val value = parent!!.selectedItem.toString()
                    if (value == roles[0]) {
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
                    if (value == roles[0]) {
                        val color = resources.getColor(R.color.bbbb)
                        (view as TextView).setTextColor(color)
                    } else {
                        val color = resources.getColor(R.color.bbbb)
                        (view as TextView).setTextColor(color)

                    }
                }
            }
        ///////////////////////////////////////////////////////////////////////////////////////
        editRemoveViewModel.getUserInfo("barier " + retrivedToken, retrivedID.toString())


        editRemoveViewModel.UserLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                loading.isDismiss()
                binding.userInfo.setBackgroundResource(R.drawable.oval)
                Glide.with(this).load(it.image.url).into(binding.userInfo)

            } else if(editRemoveViewModel.error!=null) {
                loading.isDismiss()
                Snackbar.make(
                    view,
                    editRemoveViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                editRemoveViewModel.error =null
            }
        })
        binding.editbtn.setOnClickListener {
            val role = binding.editCareGiveSpinner.selectedItem.toString()

            if (binding.editCareGiveSpinner.selectedItem == roles[0]) {
                binding.editCareGiveSpinner.adapter = spinnerAdapter2
                binding.editCareGiveSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            val value = parent!!.selectedItem.toString()
                            if (value == roles[0]) {
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
                            if (value == roles[0]) {
                                val color = resources.getColor(android.R.color.holo_red_dark)
                                (view as TextView).setTextColor(color)
                            } else {
                                val color = resources.getColor(R.color.bbbb)
                                (view as TextView).setTextColor(color)

                            }
                        }
                    }
            } else {
                editRemoveViewModel.updateRole("barier ${retrivedToken}", caregiverId, role)
                loading.startLoading()

                editRemoveViewModel.snackBarLiveData.observe(viewLifecycleOwner){
                    it.let {
                        Snackbar.make(view, it.toString(), Snackbar.LENGTH_SHORT).show()
                        loading.isDismiss()
                    }

                    editRemoveViewModel.updateCaregiverLiveData.observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            editRemoveViewModel.Empty()
                            loading.isDismiss()
                            Snackbar.make(
                                Role,
                                "Role Updated",
                                Snackbar.LENGTH_SHORT
                            ).show()

                            findNavController().navigate(EditRemoveCaregiverRoleDirections.actionEditRemoveCaregiverRoleToCaregiversPatientFragment())
                        } else if(editRemoveViewModel.error!=null) {
                            loading.isDismiss()
                            Snackbar.make(
                                Role,
                                editRemoveViewModel.error.toString(),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        editRemoveViewModel.error =null

                    })

                }
            }

        }

//        editRemoveViewModel.snackBarLiveData.observe(viewLifecycleOwner){
//            it.let {
//                Snackbar.make(view, it.toString(), Snackbar.LENGTH_SHORT).show()
//                loading.isDismiss()
//                loading.isDismiss()
//            }
//        }

        binding.userInfo.setOnClickListener {
            findNavController().navigate(EditRemoveCaregiverRoleDirections.actionEditRemoveCaregiverRoleToBasicInformationFragment())
        }
        binding.delbtn.setOnClickListener {
            editRemoveViewModel.deleteCaregiver("barier ${retrivedToken}",caregiverId )
            loading.startLoading()

            editRemoveViewModel.snackBarLiveData.observe(viewLifecycleOwner) {
                it.let {
                    Snackbar.make(view, it.toString(), Snackbar.LENGTH_SHORT).show()
                    loading.isDismiss()
                }

                editRemoveViewModel.deleteCaregiverLiveData.observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        editRemoveViewModel.Empty()
                        loading.isDismiss()
                        Snackbar.make(
                            Role,
                            "CareGiver Deleted",
                            Snackbar.LENGTH_SHORT
                        ).show()

                        findNavController().navigate(EditRemoveCaregiverRoleDirections.actionEditRemoveCaregiverRoleToCaregiversPatientFragment())
                    } else if (editRemoveViewModel.error != null) {
                        loading.isDismiss()
                        Snackbar.make(
                            Role,
                            editRemoveViewModel.error.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    editRemoveViewModel.error = null

                })

            }
        }

        editRemoveViewModel.deleteCaregiverLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                editRemoveViewModel.Empty()
                loading.isDismiss()
                Snackbar.make(
                    Role,
                    "CareGiver Deleted",
                    Snackbar.LENGTH_SHORT
                ).show()

                findNavController().navigate(EditRemoveCaregiverRoleDirections.actionEditRemoveCaregiverRoleToCaregiversPatientFragment())
            } else if (editRemoveViewModel.error != null) {
                loading.isDismiss()
                Snackbar.make(
                    Role,
                    editRemoveViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            editRemoveViewModel.error = null

        })

        editRemoveViewModel.updateCaregiverLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                editRemoveViewModel.Empty()
                loading.isDismiss()
                Snackbar.make(
                    Role,
                    "Role Updated",
                    Snackbar.LENGTH_SHORT
                ).show()

                findNavController().navigate(EditRemoveCaregiverRoleDirections.actionEditRemoveCaregiverRoleToCaregiversPatientFragment())
            } else if(editRemoveViewModel.error!=null) {
                loading.isDismiss()
                Snackbar.make(
                    Role,
                    editRemoveViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            editRemoveViewModel.error =null

        })


    }
}