package com.example.oldpeoplecareapp.ui.PatientPath.EditUserInformation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentEditUserBinding
import com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine.AddNewMedicineFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull as toMediaTypeOrNull1


class EditUserFragment : Fragment() {

    lateinit var binding: FragmentEditUserBinding
    lateinit var editUserViewModel: EditUserViewModel
    lateinit var retrivedToken:String
    lateinit var retrivedID:String
    lateinit var imgurl:String

    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditUserBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navBar2 = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation2)
        navBar.visibility=View.GONE
        navBar2.visibility=View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        retrivedID = getpreferences.getString("ID", null).toString()

        val loading = LoadingDialog(requireActivity())

        editUserViewModel = ViewModelProvider(requireActivity()).get(EditUserViewModel::class.java)
        editUserViewModel.getUserInfo("barier " + retrivedToken,retrivedID)
        loading.isDismiss()

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
/////////////////////////////////////////////////////////////////////////////////////////////////

        val genderitems = resources.getStringArray(R.array.gender)
        val spinnerAdapter2 = object :
            ArrayAdapter<String>(requireContext(), R.layout.errorspinner_item, genderitems) {
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
                    view.setTextAppearance(R.style.MyTextStyle3)
                } else {
                    //here it is possible to define color for other items by
                    val color = resources.getColor(R.color.black)
                    view.setTextAppearance(R.style.MyTextStyle3)
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
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView =
                    super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if (position == 0) {
                    val color = resources.getColor(R.color.black)
                    (view as TextView).setTextColor(color)
                    view.setTextAppearance(R.style.MyTextStyle3)
                } else {
                    //here it is possible to define color for other items by
                    val color = resources.getColor(R.color.black)
                    view.setTextAppearance(R.style.MyTextStyle3)
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
                    val color = resources.getColor(R.color.black)
                    (view as TextView).setTextColor(color)                      }
                else{
                    val color = resources.getColor(R.color.black)
                    (view as TextView).setTextColor(color)
                }
            }
        }

/////////////////////////////////////////////////////////////////////////////////////////////////

        val calendar= Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        binding.datetxtx.setOnClickListener {
            binding.datetxtx.setHintTextColor( getResources().getColor(R.color.normal));
            binding.datetxtx.setBackgroundResource(R.drawable.et_style)
            binding.datetxtx.hint="   Date Of Birth"
            val datePickerDialog= DatePickerDialog(requireContext(),
                DatePickerDialog.OnDateSetListener { it, year, month, dayOfMonth ->
                binding.datetxtx.text = "   $year-${month+1}-$dayOfMonth"
            },year,month,day)
            datePickerDialog.show()
        }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        binding.editbtn.setOnClickListener {
            val fullname = binding.nametxtX.editText!!.text.toString()
            val email = binding.mailtxtx.editText!!.text.toString()
            val phone = binding.phonetxtx.editText!!.text.toString()
            val dateOfBirth = binding.datetxtx.text.toString()
            val gender = binding.gender.selectedItem.toString()

            //////////////////////////////////////////////////////////////////


            if(binding.datetxtx.text.toString().isNullOrEmpty()){
                binding.datetxtx.setHintTextColor( getResources().getColor(R.color.holo));
                binding.datetxtx.setBackgroundResource(R.drawable.error_style)
                binding.datetxtx.hint="  Date Of Birth"
            }

            //////////////////////////////////////////////////////////////////

            if(binding.nameTxt.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.nametxtX,requireContext(), android.R.color.holo_red_dark)
                binding.nameTxt.setBackgroundResource(R.drawable.error_style)
                binding.nametxtX.hint="Full Name"
                binding.nameTxt.setOnClickListener {
                    setTextInputLayoutHintColor(binding.nametxtX,requireContext(), R.color.normal)
                    binding.nameTxt.setBackgroundResource(R.drawable.et_style)
                    binding.nametxtX.hint="Full Name"
                }
            }

            //////////////////////////////////////////////////////////////////

            if(binding.mailtxt.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.mailtxtx,requireContext(), android.R.color.holo_red_dark)
                binding.mailtxt.setBackgroundResource(R.drawable.error_style)
                binding.mailtxtx.hint="E-Mail"
                binding.mailtxt.setOnClickListener {
                    setTextInputLayoutHintColor(binding.mailtxtx,requireContext(), R.color.normal)
                    binding.mailtxt.setBackgroundResource(R.drawable.et_style)
                    binding.mailtxtx.hint="E-Mail"
                }
            }

            //////////////////////////////////////////////////////////////////

            if(binding.phonetxt.text.toString().isNullOrEmpty()){
                setTextInputLayoutHintColor(binding.phonetxtx,requireContext(), android.R.color.holo_red_dark)
                binding.phonetxt.setBackgroundResource(R.drawable.error_style)
                binding.phonetxtx.hint="Phone"
                binding.phonetxt.setOnClickListener {
                    setTextInputLayoutHintColor(binding.phonetxtx,requireContext(), R.color.normal)
                    binding.phonetxt.setBackgroundResource(R.drawable.et_style)
                    binding.phonetxtx.hint="Phone"
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

            if (!binding.nameTxt.text.toString().isNullOrEmpty() && !binding.mailtxt.text.toString()
                    .isNullOrEmpty() &&
                !binding.phonetxt.text.toString().isNullOrEmpty() && !(binding.gender == null)
            ) {

                val file = File(imgurl)
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull1(), file)
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)


                val fullnameBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull1(),
                    binding.nameTxt.text.toString()
                )
                val emailBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull1(),
                    binding.mailtxt.text.toString()
                )
                val phoneBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull1(),
                    binding.phonetxt.text.toString()
                )
                val dateOfBirthBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull1(), dateOfBirth)
                val genderBody = RequestBody.create(
                    "text/plain".toMediaTypeOrNull1(),
                    binding.gender.selectedItem.toString()
                )

                editUserViewModel.udpateUserInfo(
                    "barier " + retrivedToken,
                    fullnameBody,
                    emailBody,
                    phoneBody,
                    dateOfBirthBody,
                    genderBody,
                    imagePart
                )
                loading.startLoading()
            }
        }

        binding.editbutton.setOnClickListener {
            pickImageGallery()
        }

        editUserViewModel.UserLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                loading.isDismiss()
                binding.userName.setText(it.fullname)
                binding.mailTitle.setText(it.email)
                binding.nametxtX.editText!!.setText(it.fullname)
                binding.phonetxtx.editText!!.setText(it.phone)
                binding.mailtxtx.editText!!.setText(it.email)
                val d = it.dateOfBirth.subSequence(0,10)

                binding.datetxtx.setText(d)
                val position = spinnerAdapter.getPosition(it.gender)
                binding.gender.setSelection(position)
                binding.userpicture.setBackgroundResource(R.drawable.oval)
                Glide.with(this).load(it.image.url).into(binding.userpicture)


            } else if(editUserViewModel.error!=null) {
                loading.isDismiss()
                Snackbar.make(
                    view,
                    editUserViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                editUserViewModel.error =null
            }
        })

        editUserViewModel.UpdatLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                loading.isDismiss()
                findNavController().navigate(EditUserFragmentDirections.actionEditUserFragmentToBasicInformationFragment())
                findNavController().popBackStack()
            } else if(editUserViewModel.error!=null) {
                loading.isDismiss()
                Snackbar.make(
                    view,
                    editUserViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                editUserViewModel.error =null
            }
        })
    }

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddNewMedicineFragment.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                imgurl = getPathFromUri(selectedImageUri)
                Log.i("picc", imgurl)
                Glide.with(this).load(imgurl).into(binding.userpicture)
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = requireContext().contentResolver.query(uri, projection, null, null, null)
        val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val path: String? = columnIndex?.let { cursor?.getString(it) }
        cursor?.close()
        return path ?: ""
    }
}