package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentAddNewMedicineBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_add_new_medicine.*
import java.io.File
import java.io.IOException
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
class AddNewMedicineFragment : Fragment() {

    var TAG="AddNewMedicineFragmentLOG"
    lateinit var binding:FragmentAddNewMedicineBinding
    lateinit var mediaRecorder: MediaRecorder
    lateinit var addNewMedicineViewModel: AddNewMedicineViewModel


    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }
   private fun reset(){
       if(!binding.MedicineName.text.isNullOrEmpty()){
           Snackbar.make(MED,"YOUR MEDICINE IS ADDED SUCCESSFULLY",Snackbar.LENGTH_SHORT).show()}
        binding.MedicineName.text=null
        binding.UploadPhoto.text=null
        binding.recordX.editText!!.text=null
        binding.medicineType.setSelection(0)
        binding.Time.text=null
        binding.description.text=null
   }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNewMedicineBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        navBar?.selectedItemId =R.id.add
        return binding.root   }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 111
            )
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

        val typesMed = resources.getStringArray(R.array.medicines)
        val spinnerAdapter2 = object :
            ArrayAdapter<String>(requireContext(), R.layout.errorspinner_item, typesMed) {
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
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item, typesMed) {
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
        binding.medicineType.adapter = spinnerAdapter
        binding.medicineType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val value = parent!!.selectedItem.toString()
                if (value == typesMed[0]) {
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
                if (value == typesMed[0]) {
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)                      }
                else{
                    val color = resources.getColor(R.color.bbbb)
                    (view as TextView).setTextColor(color)

                }
            }
        }

/////////////////////////////////////////////////////////////////////////////////////////////////

        addNewMedicineViewModel = ViewModelProvider(requireActivity()).get(AddNewMedicineViewModel::class.java)
        val loading= LoadingDialog(requireActivity())

        mediaRecorder = MediaRecorder()
        val fileName = "medicine.3gp"
        var output: String
        val appDir =
            File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path}/MyRecordings/")
        appDir.mkdirs()
        if (appDir.exists()) {
            Log.d(TAG, "startRecording: dir is exist")
            output = appDir.path + "/" + fileName
        } else {
            Log.d(TAG, "startRecording: dir is not exist")
            appDir.mkdirs()
            output = appDir.path + "/" + fileName
        }
        var isRecording = false
        binding.addrec.setOnClickListener {

                try {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                mediaRecorder.setOutputFile(output)
                mediaRecorder.prepare()
                mediaRecorder.start()
                isRecording = true
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: IOException) {

                e.printStackTrace()
            }
            binding.recordX.editText!!.setText("Recording!")
            binding.cancelbtn.visibility = View.VISIBLE
            binding.addrec.visibility=View.GONE
        }

        binding.cancelbtn.setOnClickListener {
            if (isRecording) {
                mediaRecorder.stop()
            }
            binding.recordX.editText!!.setText(output)
            isRecording = false
            binding.cancelbtn.visibility = View.GONE
            binding.addrec.visibility=View.VISIBLE

        }


        ///////////////////////////////////////////////////////////////////////////////////////////

        binding.Time.setOnClickListener { clickTimePicker(it) }

        ///////////////////////////////////////////////////////////////////////////////////////////

        binding.UploadPhoto.setOnClickListener { pickImageGallery() }

        ///////////////////////////////////////////////////////////////////////////////////////////

        binding.backBtn.setOnClickListener {
            findNavController().navigate(
                AddNewMedicineFragmentDirections.actionAddNewMedicineFragmentToPatientHomeFragment(
                    "",
                    "",
                    ""
                )
            )
        }



        binding.addMedicine.setOnClickListener {
            if (binding.MedicineName.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.MedicineNameX,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.MedicineNameX.hint = "Medicine Name is Required"
                binding.MedicineName.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.MedicineNameX,
                        requireContext(),
                        R.color.normal
                    )
                    binding.MedicineNameX.hint = "Medicine Name"
                }
            }
            if (binding.description.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.descriptionX,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.descriptionX.hint = "Description is Required"
                binding.description.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.descriptionX,
                        requireContext(),
                        R.color.normal
                    )
                    binding.descriptionX.hint = "Description"
                }
            }

            if (binding.UploadPhoto.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.UploadPhotoX,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.UploadPhotoX.hint = "Photo is Required"
                binding.UploadPhoto.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.UploadPhotoX,
                        requireContext(),
                        R.color.normal
                    )
                    binding.UploadPhotoX.hint = "Upload Photo"
                }
            }
            if (binding.record.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.recordX,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.recordX.hint = "Record is Required"
                binding.record.setOnClickListener {
                    setTextInputLayoutHintColor(binding.recordX, requireContext(), R.color.normal)
                    binding.recordX.hint = "Record"
                }
            }

            if(binding.medicineType.selectedItem==typesMed[0]){
                binding.medicineType.adapter = spinnerAdapter2
                binding.medicineType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        val value = parent!!.selectedItem.toString()
                        if (value == typesMed[0]) {
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
                        if (value == typesMed[0]) {
                            val color = resources.getColor(android.R.color.holo_red_dark)
                            (view as TextView).setTextColor(color)                        }
                        else{
                            val color = resources.getColor(R.color.bbbb)
                            (view as TextView).setTextColor(color)

                        }
                    }
                }
            }


            if (binding.Time.text.toString().isNullOrEmpty()) {
                binding.Time.setHintTextColor(getResources().getColor(R.color.holo));
                binding.Time.setBackgroundResource(R.drawable.error_style)
                binding.Time.hint = "  Required"
            }
            //Retrieve token wherever necessary
            val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
            val retrivedToken = preferences.getString("TOKEN", null)
            val retrivedID = preferences.getString("ID", null)

            val name= binding.MedicineName.text.toString()
            val photo=binding.UploadPhoto.text.toString()
            val record=binding.recordX.editText!!.text.toString()
            val type=binding.medicineType.selectedItem.toString()
            val time=binding.Time.text.toString()
            val description=binding.description.text.toString()

            if (!binding.MedicineName.text.toString()
                    .isNullOrEmpty() && !binding.description.text.toString().isNullOrEmpty() &&
                !binding.UploadPhoto.text.toString()
                    .isNullOrEmpty() && !binding.record.text.toString().isNullOrEmpty() &&
                !binding.medicineType.selectedItem.toString().isNullOrEmpty()
            ) {
                addNewMedicineViewModel.addMedicine(
                    retrivedID.toString(),
                    "barier ${retrivedToken}",
                    name,
                    photo,
                    record,
                    type,
                    time,
                    time,
                    2,
                    description
                )
                loading.startLoading()

            }
        }
        addNewMedicineViewModel.AddLiveData.observe(viewLifecycleOwner){
            if (it != null) {
                reset()
                loading.isDismiss()
                Log.i("ifObserve", "yes")
            } else {
                Log.i("elseObserve", "not")
            }
        }       
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            binding.recordX.isEnabled=true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(view: View) {
        binding.Time.setHintTextColor( getResources().getColor(R.color.normal))
        binding.Time.setBackgroundResource(R.drawable.et_style)
        val c = Calendar.getInstance()
        val hour =c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val tpd = TimePickerDialog(requireContext(),TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

            if(h<10){
                if (m>=10){
                    binding.Time.text="0${h}:$m"
                }else{
                    binding.Time.text="0${h}:0$m"
                }
            }else{
                if (m>=10){
                    binding.Time.text="${h}:$m"
                }else{
                    binding.Time.text="${h}:0$m"
                }

            }
        }),hour,minute,false)
        tpd.show()
        Log.i(TAG,"hour = "+hour.toString())
    }

    private fun pickImageGallery() {
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== IMAGE_REQUEST_CODE &&resultCode ==RESULT_OK ){
            binding.UploadPhoto.setText(data?.data?.path)
        }
    }
}























//                GlobalScope.launch(Dispatchers.IO) {
//                    val value = remoteRepositoryImp.postMedicine(
//                        retrivedID.toString(),
//                        "barier ${retrivedToken}",
//                        "PanadolX",
//                        "https://images.theconversation.com/files/369567/original/file-20201116-23-18wlnv.jpg?ixlib=rb-1.1.0&q=45&auto=format&w=1356&h=668&fit=crop",
//                        "https://images.theconversation.com/files/369567/original/file-20201116-23-18wlnv.jpg?ixlib=rb-1.1.0&q=45&auto=format&w=1356&h=668&fit=crop",
//                        "injection",
//                        "2002-12-09",
//                        "21:55",
//                        1,
//                        "Good"
//                    )
//                    if (value.isSuccessful) {
//                        Log.i(TAG, value.body().toString())
//                    } else {
//                        Log.i(TAG, value.toString())
//                    }
//
//                }