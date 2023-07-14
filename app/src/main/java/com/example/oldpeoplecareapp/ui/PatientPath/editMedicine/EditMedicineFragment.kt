package com.example.oldpeoplecareapp.ui.PatientPath.editMedicine

import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentEditMedicineBinding
import com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine.AddNewMedicineFragment
import com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine.AddNewMedicineFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine.TimeRecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_edit_medicine.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditMedicineFragment : Fragment() {

    private lateinit var navController: NavController

    val TAG = "EditMedicineFragmentTAG"
    lateinit var binding: FragmentEditMedicineBinding
    lateinit var mediaRecorder: MediaRecorder
    lateinit var editMedicineViewModel: EditMedicineViewModel
    val timeRecyclerView by lazy { TimeRecyclerView() }
    var TimeList: MutableList<String> = mutableListOf()
    var selectedAlarmTimes: MutableList<Calendar> = mutableListOf()
    var days: MutableList<String> = mutableListOf()
    lateinit var imgurl: String


    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        navController = NavHostFragment.findNavController(this)

        val bottomNavigation:BottomNavigationView=requireActivity().findViewById(R.id.bottom_navigation)

        if (navController.currentDestination?.label == "fragment_registration"
            || navController.currentDestination?.label == "fragment_log_in"
            || navController.currentDestination?.label=="EditMedicineFragment"
        ) {
            bottomNavigation.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
        }
        //------------------------------------------------------//

        val args = EditMedicineFragmentArgs.fromBundle(requireArguments())

        val medId=args.medId
        val userId=args.userId
        val token=args.token

        //------------------------------------------------------//

        RecordPermissions()
        binding.timeRecycle.adapter = timeRecyclerView
        editMedicineViewModel = ViewModelProvider(requireActivity()).get(EditMedicineViewModel::class.java)
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
        //------------------------------------------------------//

        val Repeats = resources.getStringArray(R.array.repeats)
        //When there is an empty field that adapter will be assigned
        val ErrorAdapter2 = object :
            ArrayAdapter<String>(requireContext(), R.layout.errorspinner_item, Repeats) {
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
        ErrorAdapter2.setDropDownViewResource(android.R.layout.simple_list_item_checked)

        //------------------------------------------------------//
        //------------------------------------------------------//

        //if there is no error thats the main adapter
        val RepeatAdapter = object :
            ArrayAdapter<String>(requireContext(), R.layout.spinner_item, Repeats) {
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
        RepeatAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked)

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
        //------------------------------------------------------//

        binding.Repeated.adapter = RepeatAdapter
        binding.Repeated.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                val value = parent!!.selectedItem.toString()
                if (value == Repeats[0]) {
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
                if (value == Repeats[0]) {
                    val color = resources.getColor(R.color.bbbb)
                    // (view as TextView).setTextColor(color)
                } else if (value == Repeats[2]) {
                    val color = resources.getColor(R.color.bbbb)
                    binding.linearLayout.visibility = View.VISIBLE
                    days.clear()
                    //     (view as TextView).setTextColor(color)
                } else {
                    val color = resources.getColor(R.color.bbbb)
                    binding.linearLayout.visibility = View.GONE
                    days.clear()
                    days.add("Saturday")
                    days.add("Sunday")
                    days.add("Monday")
                    days.add("Tuesday")
                    days.add("Wednesday")
                    days.add("Thursday")
                    days.add("Friday")
                    Log.i("days", days.toString())
                }
            }
        }

        binding.SAT.setOnClickListener {
            handleButtonClick(binding.SAT, "Saturday")
        }
        binding.SUN.setOnClickListener {
            handleButtonClick(binding.SUN, "Sunday")
        }
        binding.MON.setOnClickListener {
            handleButtonClick(binding.MON, "Monday")
        }
        binding.TUES.setOnClickListener {
            handleButtonClick(binding.TUES, "Tuesday")
        }
        binding.WED.setOnClickListener {
            handleButtonClick(binding.WED, "Wednesday")
        }
        binding.THU.setOnClickListener {
            handleButtonClick(binding.THU, "Thursday")
        }
        binding.FRI.setOnClickListener {
            handleButtonClick(binding.FRI, "Friday")
        }

        //------------------------------------------------------//

        mediaRecorder = MediaRecorder()
        val fileName = "medicine.mp4"
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val uniqueFileName = "${timeStamp}_${UUID.randomUUID()}_$fileName"
        var output: String

        val appDir = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path}/MyRecording/")
        appDir.mkdirs()
        if (appDir.exists()) {
            Log.d(TAG, "startRecording: dir is exist")
            output = appDir.path + "/" + uniqueFileName
        } else {
            Log.d(TAG, "startRecording: dir is not exist")
            appDir.mkdirs()
            output = appDir.path + "/" + uniqueFileName
        }
        var isRecording = false

        //------------------------------------------------------//
        //Add rec btn listener
        binding.addrec.setOnClickListener {
            try {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
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
            binding.addrec.visibility = View.GONE
        }

        //------------------------------------------------------//

        //Cancel rec btn listener
        binding.cancelbtn.setOnClickListener {
            if (isRecording) {
                mediaRecorder.stop()
            }
            binding.recordX.editText!!.setText("Record Saved")
            isRecording = false
            binding.cancelbtn.visibility = View.GONE
            binding.addrec.visibility = View.VISIBLE

        }


        //------------------------------------------------------//

        binding.Time.setOnClickListener { clickTimePicker(it) }

        //------------------------------------------------------//

        binding.addpicbtn.setOnClickListener { pickImageGallery() }

        //------------------------------------------------------//

        binding.userInfo.setOnClickListener {
            findNavController().navigate(EditMedicineFragmentDirections.actionEditMedicineFragmentToBasicInformationFragment())
        }

        //------------------------------------------------------//



        binding.backBtn.setOnClickListener {
//            findNavController().navigate(
//                AddNewMedicineFragmentDirections.actionAddNewMedicineFragmentToPatientHomeFragment(
//                    "",
//                    "",
//                    ""
//                )
//            )
            findNavController().navigateUp()
        }


        //------------------------------------------------------//
        binding.removeMed.setOnClickListener {
            Log.i(TAG, "clicked")
            editMedicineViewModel.DeleteAllMedicine(medId,retrivedID.toString(), "barier ${retrivedToken}")
            Log.i(TAG,"med id: $medId  userId:$userId userId:$retrivedID  token: $retrivedToken")
            loading.startLoading()
            editMedicineViewModel.deleteMedicineLiveData.observe(viewLifecycleOwner){
                if (it!=null) {
                    loading.isDismiss()
                    editMedicineViewModel.EmptyMedicine()
                    findNavController().navigate(  EditMedicineFragmentDirections.actionEditMedicineFragmentToPatientHomeFragment("","",""))
                    Snackbar.make(MEDX,"YOUR MEDICINE IS DELETED SUCCESSFULLY", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, "deleted")
                } else if(editMedicineViewModel.error!=null) {
                    Snackbar.make(
                        MEDX,
                        editMedicineViewModel.error.toString(),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Log.i(TAG, editMedicineViewModel.error.toString())
                    editMedicineViewModel.error=null
                }
            }

            editMedicineViewModel.snackBarLiveData.observe(viewLifecycleOwner){
                it.let {
                    Snackbar.make(view, it.toString(), Snackbar.LENGTH_SHORT).show()
                    loading.isDismiss()
                }
            }

        }
        editMedicineViewModel.deleteMedicineLiveData.observe(viewLifecycleOwner){
            if (it!=null) {
              //  loading.isDismiss()
                // findNavController().navigate(  EditMedicineFragmentDirections.actionEditMedicineFragmentToPatientHomeFragment("","",""))
                Snackbar.make(MEDX,"YOUR MEDICINE IS DELETED SUCCESSFULLY", Snackbar.LENGTH_SHORT).show()
                Log.i(TAG, "deleted out")
                Log.i(TAG, it.toString())
            } else if(editMedicineViewModel.error!=null) {
                Snackbar.make(
                    MEDX,
                    editMedicineViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                Log.i(TAG, it.toString())
                editMedicineViewModel.error=null
            }
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

            if (binding.Repeated.selectedItem == Repeats[0]) {
                binding.Repeated.adapter = ErrorAdapter2
                binding.Repeated.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            val value = parent!!.selectedItem.toString()
                            if (value == Repeats[0]) {
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
                            if (value == Repeats[0]) {
                                val color = resources.getColor(android.R.color.holo_red_dark)
                                (view as TextView).setTextColor(color)
                            } else {
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
            val repeated = binding.Repeated.selectedItem.toString()

            if (!binding.MedicineName.text.toString()
                    .isNullOrEmpty() && !binding.description.text.toString().isNullOrEmpty() &&
                !binding.UploadPhoto.text.toString()
                    .isNullOrEmpty() && !binding.record.text.toString().isNullOrEmpty() &&
                !binding.medicineType.selectedItem.toString().isNullOrEmpty()&&
                !binding.Repeated.selectedItem.toString().isNullOrEmpty()
            ) {
                val nameRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
                val typeRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), type)
                val descriptionRequestBody =
                    RequestBody.create("text/plain".toMediaTypeOrNull(), description)
///////////////////////////////////////////////////////////////////////////////////////
                val timeRequestBodyList = TimeList.map { time ->
                    RequestBody.create("text/plain".toMediaTypeOrNull(), time)
                }

                val builder2 = MultipartBody.Builder().setType(MultipartBody.FORM)
                val partName2 = "time" // Change this to a suitable name
                for ((index, timeRequestBody) in timeRequestBodyList.withIndex()) {
                    builder2.addFormDataPart("$partName2[$index]", "", timeRequestBody)
                }
                val multipartTime = builder2.build()

// Convert MultipartBody to MultipartBody.Part
                val timePart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("time", "", multipartTime)


///////////////////////////////////////////////////////////////////////////////////////

                val weeklyRequestBodyList = days.map { day ->
                    RequestBody.create("text/plain".toMediaTypeOrNull(), day)
                }

                val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                val partName = "weakly"
                for ((index, weeklyRequestBody) in weeklyRequestBodyList.withIndex()) {
                    builder.addFormDataPart("$partName[$index]", "", weeklyRequestBody)
                }
                val multipartWeek = builder.build()

// Convert MultipartBody to MultipartBody.Part
                val weeklyPart: MultipartBody.Part =
                    MultipartBody.Part.createFormData("weakly", "", multipartWeek)

///////////////////////////////////////////////////////////////////////////////////////


                val file = File(imgurl)
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)


                val audioFile = File(output)
                val audioRequestBody = audioFile.asRequestBody("audio/mp4".toMediaTypeOrNull())
                val recordUrlPart2 = MultipartBody.Part.createFormData("audio", audioFile.name, audioRequestBody)


                editMedicineViewModel.updateAllMedicine(
                    medId,
                    userId,
                    "barier ${retrivedToken}",
                    nameRequestBody,
                    imagePart,
                    recordUrlPart2,
                    typeRequestBody,
                    descriptionRequestBody,
                    timePart,
                    weeklyPart
                )

                loading.startLoading()
                //------------------------------------------------------//

                editMedicineViewModel.snackBarLiveData.observe(viewLifecycleOwner){
                    it.let {
                        Snackbar.make(view, it.toString(), Snackbar.LENGTH_SHORT).show()
                        loading.isDismiss()
                    }
                }
                //------------------------------------------------------//

                editMedicineViewModel.updateMedicinLiveData.observe(viewLifecycleOwner){
                    if (it != null) {
                        reset()
                        loading.isDismiss()
                        Log.i("ifObserve", "yes")
                    } else if(editMedicineViewModel.error !=null) {
                        loading.isDismiss()
                        Snackbar.make(
                            MEDX,
                            editMedicineViewModel.error.toString(),
                            Snackbar.LENGTH_SHORT
                        ).show()
                        editMedicineViewModel.error=null
                        Log.i(TAG, "not")
                    }
                }


            }
        }
        editMedicineViewModel.updateMedicinLiveData.observe(viewLifecycleOwner){
            if (it != null) {
                reset()
                loading.isDismiss()
                Log.i("ifObserve", "yes")
            } else if(editMedicineViewModel.error !=null) {
                loading.isDismiss()
                Snackbar.make(
                    MEDX,
                    editMedicineViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                editMedicineViewModel.error=null
                Log.i(TAG, "not")
            }
        }
        editMedicineViewModel.getUserInfo("barier " + retrivedToken, retrivedID.toString())


        editMedicineViewModel.UserLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                loading.isDismiss()
                binding.userInfo.setBackgroundResource(R.drawable.oval)
                Glide.with(this).load(it.image.url).into(binding.userInfo)

            } else if(editMedicineViewModel.error!=null) {
                loading.isDismiss()
                Snackbar.make(
                    view,
                    editMedicineViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                editMedicineViewModel.error =null
            }
        })
    }

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
              //  binding.Time.text = timeText
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


    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, AddNewMedicineFragment.IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddNewMedicineFragment.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                imgurl = getPathFromUri(selectedImageUri)
                Log.i("picc", imgurl)
                binding.UploadPhoto.setText("Picture Uploaded")
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

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }

    private fun reset(){
        if(!binding.MedicineName.text.isNullOrEmpty()){
            Snackbar.make(MEDX,"YOUR MEDICINE IS EDITED SUCCESSFULLY",Snackbar.LENGTH_SHORT).show()}
        binding.MedicineName.text=null
        binding.UploadPhoto.text=null
        binding.recordX.editText!!.text=null
        binding.medicineType.setSelection(0)
        binding.Time.text=null
        binding.description.text=null
    }
    // Declare a map to store button click counts
    val buttonClickCounts = mutableMapOf<Button, Int>()

    // Function to handle button clicks and alternate background colors
    fun handleButtonClick(button: Button, day: String) {
        if (!buttonClickCounts.containsKey(button)) {
            buttonClickCounts[button] = 0
        }

        val clickCount = buttonClickCounts[button] ?: 0
        buttonClickCounts[button] = clickCount + 1

        if (clickCount % 2 == 0) {
            //   button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.active)) // Replace "your_color_resource" with the actual color resource ID
            button.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.active))
            button.setBackgroundResource(R.drawable.clickedactive)
            days.add(day)
            Log.i("days", days.toString())
        } else {
            //   button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey)) // Replace "your_color_resource" with the actual color resource ID
            button.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.inactive))
            button.setBackgroundResource(R.drawable.clickedunactive)
            days.remove(day)
            Log.i("days", days.toString())

        }
    }
}




