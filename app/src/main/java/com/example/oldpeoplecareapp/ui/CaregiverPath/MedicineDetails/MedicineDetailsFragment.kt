package com.example.oldpeoplecareapp.ui.CaregiverPath.MedicineDetails

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentMedicineDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


class MedicineDetailsFragment : Fragment() {

    var TAG = "MedicineDetailsFragment"
    lateinit var binding: FragmentMedicineDetailsBinding
    lateinit var medicineDetailVieModel: MedicineDetailVieModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMedicineDetailsBinding.inflate(inflater, container, false)
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navBar.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val preferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val retrivedToken = preferences.getString("TOKEN", null)
        val retrivedID = preferences.getString("ID", null)
        var recordPath: String=""
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

        medicineDetailVieModel.getSingleMed(medId, retrivedID.toString(), "barier " + retrivedToken)

        medicineDetailVieModel.SingleMedLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.timeTxtV.text = it.time[0]
                binding.typeTxt.text = it.type
                binding.repeatTxt.text = it.weakly[0]
                binding.descTxt.text = it.description
                recordPath = it.recordUrl.toString()
                Glide.with(this).load(it.imgUrl).into(binding.medImg)

            } else if (medicineDetailVieModel.error != null) {
                Snackbar.make(
                    view,
                    medicineDetailVieModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
                medicineDetailVieModel.error = null
            }
        })


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