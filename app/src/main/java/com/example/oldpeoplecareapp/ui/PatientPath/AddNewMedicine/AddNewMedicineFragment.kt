package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaRecorder
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
import kotlinx.android.synthetic.main.fragment_add_newcaregiver_patient.*
import java.io.File
import java.io.IOException
import java.util.*

class AddNewMedicineFragment : Fragment() {
    var TAG="AddNewMedicineFragxxx"
    lateinit var binding:FragmentAddNewMedicineBinding
    lateinit var mediaRecorder: MediaRecorder
    lateinit var addNewMedicineViewModel: AddNewMedicineViewModel
    val timeRecyclerView by lazy { TimeRecyclerView() }
    var  TimeList: MutableList<String> = mutableListOf()
    var selectedAlarmTimes:MutableList<Calendar> = mutableListOf()
    val r=0
    var daysList= arrayOf("Sunday","Monday")

    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddNewMedicineBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        navBar?.selectedItemId = R.id.add_icon
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        RecordPermissions()
        binding.timeRecycle.adapter = timeRecyclerView
        addNewMedicineViewModel = ViewModelProvider(requireActivity()).get(AddNewMedicineViewModel::class.java)
        val loading= LoadingDialog(requireActivity())

        //------------------------------------------------------//

        //Retrieve token wherever necessary
        val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val retrivedToken = preferences.getString("TOKEN", null)
        val retrivedID = preferences.getString("ID", null)

        //------------------------------------------------------//

        val typesMed = resources.getStringArray(R.array.medicines)
       //When there is an empty field that adapter will be assigned
        val ErrorAdapter = object :
            ArrayAdapter<String>(requireContext(), R.layout.errorspinner_item, typesMed) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
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
        ErrorAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked)

        //------------------------------------------------------//

        //if there is no error thats the main adapter
        val MedApapter = object :
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item, typesMed) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
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
        MedApapter.setDropDownViewResource(android.R.layout.simple_list_item_checked)

        //------------------------------------------------------//

        binding.medicineType.adapter = MedApapter
        binding.medicineType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val value = parent!!.selectedItem.toString()
                if (value == typesMed[0]) {
                    val color = resources.getColor(android.R.color.holo_red_dark)
                    (view as TextView).setTextColor(color)                      }
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == typesMed[0]) {
                    val color = resources.getColor(R.color.bbbb)
                   // (view as TextView).setTextColor(color)
                } else{
                    val color = resources.getColor(R.color.bbbb)
               //     (view as TextView).setTextColor(color)
                }
            }
        }

        //------------------------------------------------------//

        mediaRecorder = MediaRecorder()
        val fileName = "medicine.3gp"
        var output: String
        val appDir = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path}/MyRecording/")
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

        //------------------------------------------------------//

        //Add rec btn listener
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

        //------------------------------------------------------//

        //Cancel rec btn listener
        binding.cancelbtn.setOnClickListener {
            if (isRecording) {
                mediaRecorder.stop()
            }
            binding.recordX.editText!!.setText(output)
            isRecording = false
            binding.cancelbtn.visibility = View.GONE
            binding.addrec.visibility=View.VISIBLE

        }

        //------------------------------------------------------//

        binding.Time.setOnClickListener { clickTimePicker(it) }

        //------------------------------------------------------//

        binding.addpicbtn.setOnClickListener { pickImageGallery()
            setAlarm()
        }

        //------------------------------------------------------//

        binding.backBtn.setOnClickListener {
            findNavController().navigate(
                AddNewMedicineFragmentDirections.actionAddNewMedicineFragmentToPatientHomeFragment(
                    "",
                    "",
                    ""
                )
            )
        }

        //------------------------------------------------------//

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

            //------------------------------------------------------//

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

            //------------------------------------------------------//

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

            //------------------------------------------------------//

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

            //------------------------------------------------------//

            if(binding.medicineType.selectedItem==typesMed[0]){
                binding.medicineType.adapter = ErrorAdapter
                binding.medicineType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        val value = parent!!.selectedItem.toString()
                        if (value == typesMed[0]) {
                            val color = resources.getColor(android.R.color.holo_red_dark)
                            (view as TextView).setTextColor(color)
                        }
                    }
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
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

            //------------------------------------------------------//

            if (binding.Time.text.toString().isNullOrEmpty()) {
                binding.Time.setHintTextColor(getResources().getColor(R.color.holo));
                binding.Time.setBackgroundResource(R.drawable.error_style)
                binding.Time.hint = "  Required"
            }

            //------------------------------------------------------//

            val name= binding.MedicineName.text.toString()
            val photo=binding.UploadPhoto.text.toString()
            val record=binding.recordX.editText!!.text.toString()
            val type=binding.medicineType.selectedItem.toString()
            val description=binding.description.text.toString()

            if (!binding.MedicineName.text.toString()
                    .isNullOrEmpty() && !binding.description.text.toString().isNullOrEmpty() &&
                !binding.UploadPhoto.text.toString()
                    .isNullOrEmpty() && !binding.record.text.toString().isNullOrEmpty() &&
                !binding.medicineType.selectedItem.toString().isNullOrEmpty()
            ) {
                Log.i("data", retrivedID.toString() +"/"+ "barier ${retrivedToken}"+ "/"
                       + name + "/"+ photo +"/"+record + "/" +type+"/"+description+"/"+TimeList)
                addNewMedicineViewModel.addMedicine(
                    retrivedID.toString(),
                    "barier ${retrivedToken}",
                    name,
                    "https://images.theconversation.com/files/369567/original/file-20201116-23-18wlnv.jpg?ixlib=rb-1.1.0&q=45&auto=format&w=1356&h=668&fit=crop",
                    record,
                    type,
                    description,
                    TimeList,
                    daysList,
                )
                Log.i(TAG,"THE ARRAY IS ${TimeList} +")
                loading.startLoading()

            }
        }

        //------------------------------------------------------//

        addNewMedicineViewModel.AddLiveData.observe(viewLifecycleOwner){
            if (it != null) {
                reset()
                loading.isDismiss()
                timeRecyclerView.setList(emptyList())
                Log.i(TAG, "yes")
            } else {
                Log.i(TAG, "not")
                loading.isDismiss()
                Snackbar.make(
                    MED,
                    addNewMedicineViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()

            }
        }       
    }

    ///////////////////////////////////////////////////////////////////////////////////
    private fun RecordPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            binding.recordX.isEnabled=true
        }
    }

    fun clickTimePicker(view: View) {
        binding.Time.setHintTextColor( getResources().getColor(R.color.normal))
        binding.Time.setBackgroundResource(R.drawable.et_style)

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minuteOfHour ->
                c.set(Calendar.HOUR_OF_DAY, hourOfDay)
                c.set(Calendar.MINUTE, minuteOfHour)
                val timeText = String.format("%02d:%02d", hourOfDay, minuteOfHour)
                binding.Time.text = timeText
                TimeList.add(timeText)
                timeRecyclerView.setList(TimeList)
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minuteOfHour)
                selectedAlarmTimes.add(calendar)

            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun setAlarm() {
        // Set the alarms using the AlarmHelper
        val alarmHelper = AlarmHelper()
        alarmHelper.setAlarm(requireContext(), selectedAlarmTimes)
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
}