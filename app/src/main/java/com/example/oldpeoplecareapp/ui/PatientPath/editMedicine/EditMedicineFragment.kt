package com.example.oldpeoplecareapp.ui.PatientPath.editMedicine

import android.Manifest
import android.app.Activity
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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentEditMedicineBinding
import com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine.AddNewMedicineFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_edit_medicine.*
import java.io.File
import java.io.IOException
import java.util.*

@RequiresApi(Build.VERSION_CODES.N)
class EditMedicineFragment : Fragment() {

    private lateinit var navController: NavController

    val TAG="EditMedicineFragmentTAG"
    lateinit var binding: FragmentEditMedicineBinding
    lateinit var mediaRecorder: MediaRecorder
//  lateinit var remoteRepositoryImp: RemoteRepositoryImp
    lateinit var editMedicineViewModel: EditMedicineViewModel

    companion object {
        const val IMAGE_REQUEST_CODE = 100
    }
    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }

    private fun reset(){
        if(!binding.MedicineName.text.isNullOrEmpty()){
            Snackbar.make(MEDX,"YOUR MEDICINE IS EDITED SUCCESSFULLY", Snackbar.LENGTH_SHORT).show()}
        binding.MedicineName.text=null
        binding.UploadPhoto.text=null
        binding.recordX.editText!!.text=null
        binding.medicineType.editText!!.text=null
        binding.Date.text=null
        binding.Time.text=null
        binding.description.text=null
        binding.RepeatTimesX.editText!!.text=null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditMedicineBinding.inflate(inflater, container, false)
        return binding.root
    }

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
        ///////////////////////////////////////////////////////////////////////////////////////////
//        val serviceInstant = RetroBuilder.builder
//        remoteRepositoryImp = RemoteRepositoryImp(serviceInstant)
        editMedicineViewModel =
            ViewModelProvider(requireActivity()).get(EditMedicineViewModel::class.java)
        val loading= LoadingDialog(requireActivity())

        val args = EditMedicineFragmentArgs.fromBundle(requireArguments())

        val medId=args.medId
        val userId=args.userId
        val token=args.token

        //Retrieve token wherever necessary
        val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val retrivedToken = preferences.getString("TOKEN", null)
        val retrivedID = preferences.getString("ID", null)


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
        binding.recordBtn.setOnClickListener {
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
            binding.recordBtn.visibility = View.GONE
            binding.stopBtn.visibility = View.VISIBLE
        }
        binding.stopBtn.setOnClickListener {
            if (isRecording) {
                mediaRecorder.stop()
            }
            binding.recordX.editText!!.setText(output)
            isRecording = false
            binding.stopBtn.visibility = View.GONE
            binding.recordBtn.visibility = View.VISIBLE
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

        var M = 0
        val medicines = resources.getStringArray(R.array.medicines)
        val medicine_adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, medicines)
        binding.spinner.adapter = medicine_adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (M == 0) {
                    binding.medicineType.editText?.getText()?.clear()
                    M++
                } else {
                    binding.medicineType.editText!!.setText(medicines[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }



        ///////////////////////////////////////////////////////////////////////////////////////////

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        binding.Date.setOnClickListener {
            binding.Date.setHintTextColor(getResources().getColor(R.color.normal));
            binding.Date.setBackgroundResource(R.drawable.et_style)
            binding.Date.hint = "   Date"
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { it, year, month, dayOfMonth ->
                    binding.Date.text = "$year-${month+1}-$dayOfMonth"
                }, year, month, day
            )
            datePickerDialog.show()
        }

        ///////////////////////////////////////////////////////////////////////////////////////////

        binding.Time.setOnClickListener { clickTimePicker(it) }

        ///////////////////////////////////////////////////////////////////////////////////////////

        binding.imageBtn.setOnClickListener { pickImageGallery() }

        ///////////////////////////////////////////////////////////////////////////////////////////
//HERE
        binding.backBtn.setOnClickListener {
            findNavController().navigate(
                EditMedicineFragmentDirections.actionEditMedicineFragmentToPatientHomeFragment("","","")
            )
        }

        binding.deleteMedicine.setOnClickListener {
                editMedicineViewModel.DeleteAllMedicine(medId,userId, "barier ${retrivedToken}")

             loading.startLoading()

            editMedicineViewModel.deleteMedicineLiveData.observe(viewLifecycleOwner){
                if (it!=null) {
                    loading.isDismiss()
                    editMedicineViewModel.EmptyMedicine()
                   findNavController().navigate(  EditMedicineFragmentDirections.actionEditMedicineFragmentToPatientHomeFragment("","",""))
                    Snackbar.make(MEDX,"YOUR MEDICINE IS DELETED SUCCESSFULLY", Snackbar.LENGTH_SHORT).show()
                    Log.i(TAG, it.toString())
                } else {
                    Log.i(TAG, it.toString())
                }
            }
        }


        editMedicineViewModel.deleteMedicineLiveData.observe(viewLifecycleOwner){
            if (it!=null) {
                loading.isDismiss()
               // findNavController().navigate(  EditMedicineFragmentDirections.actionEditMedicineFragmentToPatientHomeFragment("","",""))
                Snackbar.make(MEDX,"YOUR MEDICINE IS DELETED SUCCESSFULLY", Snackbar.LENGTH_SHORT).show()
                Log.i(TAG, it.toString())
            } else {
                Log.i(TAG, it.toString())
            }
        }


        binding.editMedicine.setOnClickListener {
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

            if (binding.RepeatTimes.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.RepeatTimesX,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.RepeatTimesX.hint = "Times is Required"
                binding.RepeatTimes.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.RepeatTimesX,
                        requireContext(),
                        R.color.normal
                    )
                    binding.RepeatTimesX.hint = "Repeated Times"
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

            if (binding.medicineType.editText!!.text.toString().isNullOrEmpty()) {
                setTextInputLayoutHintColor(
                    binding.medicineType,
                    requireContext(),
                    android.R.color.holo_red_dark
                )
                binding.medicineType.hint = "Medicine Type is Required"
                binding.medicineType.editText!!.setOnClickListener {
                    setTextInputLayoutHintColor(
                        binding.medicineType,
                        requireContext(),
                        R.color.normal
                    )
                    binding.medicineType.hint = "Medicine Type"
                }
            }


            if (binding.Date.text.toString().isNullOrEmpty()) {
                binding.Date.setHintTextColor(getResources().getColor(R.color.holo));
                binding.Date.setBackgroundResource(R.drawable.error_style)
                binding.Date.hint = "  Required"
            }
            if (binding.Time.text.toString().isNullOrEmpty()) {
                binding.Time.setHintTextColor(getResources().getColor(R.color.holo));
                binding.Time.setBackgroundResource(R.drawable.error_style)
                binding.Time.hint = "  Required"
            }


            val name= binding.MedicineName.text.toString()
            val photo=binding.UploadPhoto.text.toString()
            val record=binding.recordX.editText!!.text.toString()
            val type=binding.medicineType.editText!!.text.toString()
            val date=binding.Date.text.toString()
            val time=binding.Time.text.toString()
            val description=binding.description.text.toString()
            val repeated =binding.RepeatTimesX.editText!!.text.toString()

            if (!binding.MedicineName.text.toString()
                    .isNullOrEmpty() && !binding.description.text.toString().isNullOrEmpty() &&
                !binding.UploadPhoto.text.toString()
                    .isNullOrEmpty() && !binding.record.text.toString().isNullOrEmpty() &&
                !binding.medicineType.editText!!.text.toString()
                    .isNullOrEmpty() && !binding.RepeatTimesX.editText!!.text.toString()
                    .isNullOrEmpty()
            ) {
                editMedicineViewModel.updateAllMedicine(medId,userId, "barier ${retrivedToken}",
                    name,
                    "https://th.bing.com/th/id/R.f536c7f2f88aab7047dd18f34245ac96?rik=%2bIszp%2bhbl5%2bDKQ&pid=ImgRaw&r=0",
                    record,
                    type,
                    date,
                    time,
                    repeated.toInt(),
                    description)
                loading.startLoading()

            }
        }
        editMedicineViewModel.updateMedicinLiveData.observe(viewLifecycleOwner){
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
            binding.recordBtn.isEnabled=true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickTimePicker(view: View) {
        binding.Time.setHintTextColor( getResources().getColor(R.color.normal))
        binding.Time.setBackgroundResource(R.drawable.et_style)
        val c = Calendar.getInstance()
        val hour =c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)
        val tpd = TimePickerDialog(requireContext(),
            TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

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
        val intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent, AddNewMedicineFragment.IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== AddNewMedicineFragment.IMAGE_REQUEST_CODE &&resultCode == Activity.RESULT_OK){
            binding.UploadPhoto.setText(data?.data?.path)
            // binding.UploadPhoto.setText("Uploaded Image")
        }
    }




}