package com.example.oldpeoplecareapp.ui.CaregiverPath.MedicineDetails

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentMedicineDetailsBinding
import com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome.CaregiveHomeFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


class MedicineDetailsFragment : Fragment() {

    var TAG = "MedicineDetailsFragment"
    lateinit var binding: FragmentMedicineDetailsBinding
    lateinit var medicineDetailVieModel: MedicineDetailVieModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMedicineDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val retrivedToken = preferences.getString("TOKEN", null)
        val retrivedID = preferences.getString("ID", null)
        var recordPath: String=""


        navController = NavHostFragment.findNavController(this)

        val bottomNavigation: BottomNavigationView =requireActivity().findViewById(R.id.bottom_navigation2)

        if (navController.currentDestination?.label == "fragment_registration"
            || navController.currentDestination?.label == "fragment_log_in"
            || navController.currentDestination?.label=="EditMedicineFragment"
            || navController.currentDestination?.label=="MedicineDetailsFragment"
        ) {
            bottomNavigation.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
        }

        medicineDetailVieModel =
            ViewModelProvider(requireActivity()).get(MedicineDetailVieModel::class.java)

        medicineDetailVieModel.getUserInfo("barier " + retrivedToken, retrivedID.toString())
        medicineDetailVieModel.UserLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.userInfo.setBackgroundResource(R.drawable.oval)
                Glide.with(this).load(it.image.url).into(binding.userInfo)
            } else if (medicineDetailVieModel.error != null) {
                Snackbar.make(
                    view,
                    medicineDetailVieModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                medicineDetailVieModel.error = null
            }
        })

        val args = MedicineDetailsFragmentArgs.fromBundle(requireArguments())
        val medId = args.medId
        val userId = args.userId

        Log.i(TAG, retrivedID.toString() + "    " + medId)

        medicineDetailVieModel.getSingleMed(medId, userId, "barier " + retrivedToken)

        medicineDetailVieModel.SingleMedLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.typeTxt.text = it.type
                binding.medNaame.text=it.name
                var time = ""
                for (i in it.time) {
                    time += i + " "
                }
                binding.timeTxtV.text = time

                if (it.weakly.contains("Saturday")) {
                    binding.SAT.visibility = View.VISIBLE
                }
                if (it.weakly.contains("Sunday")) {
                    binding.SUN.visibility = View.VISIBLE
                }
                if (it.weakly.contains("Monday")) {
                    binding.MON.visibility = View.VISIBLE
                }
                if (it.weakly.contains("Tuesday")) {
                    binding.TUES.visibility = View.VISIBLE
                }
                if (it.weakly.contains("Wednesday")) {
                    binding.WED.visibility = View.VISIBLE
                }
                if (it.weakly.contains("Thursday")) {
                    binding.THU.visibility = View.VISIBLE
                }
                if (it.weakly.contains("Friday")) {
                    binding.FRI.visibility = View.VISIBLE
                }

                binding.descTxt.text = it.description
                recordPath = it.audio?.url.toString()
                Glide.with(this).load(it.image?.url).into(binding.medImg)

            } else if (medicineDetailVieModel.error != null) {
                Snackbar.make(
                    view,
                    medicineDetailVieModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                medicineDetailVieModel.error = null
            }
        })

        binding.userInfo.setOnClickListener {
            findNavController().navigate(MedicineDetailsFragmentDirections.actionMedicineDetailsFragmentToBasicInformationFragment())
        }
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.recImg.setOnClickListener {
            val mediaPlayer = MediaPlayer()
            try {
                mediaPlayer.setDataSource(recordPath)
                mediaPlayer.prepare()
                mediaPlayer.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}