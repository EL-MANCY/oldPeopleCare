package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.ui.UiModel.MedicineUiModel

class CaregiverHomeRecyclerView(val mActivity: Activity): RecyclerView.Adapter<CaregiverHomeRecyclerView.CaregiverHomeViewHolder>() {

    var  PaientList: List<MedicineUiModel> = emptyList()

    fun setList(PatientItems: List<MedicineUiModel>) {
        this.PaientList = PatientItems
        notifyDataSetChanged()
    }


    inner class CaregiverHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val patientPic: ImageView =itemView.findViewById(R.id.patientPic)
        val MedImg: ImageView =itemView.findViewById(R.id.MedImg)
        val time: TextView =itemView.findViewById(R.id.Time_txtView)
        val medName:TextView=itemView.findViewById(R.id.med_nametxtview)
        val patientName:TextView=itemView.findViewById(R.id.patient_name_txtview)

        fun bind(medicineInfo: MedicineUiModel) {
            time.text= medicineInfo.time
            medName.text=medicineInfo.med
            patientName.text= medicineInfo.name

            val imageUrlPatient = medicineInfo.imgUrlUser
            Glide.with(itemView).load(imageUrlPatient).into(patientPic)
            val imageUrlMed = medicineInfo.imgUrlMed
            Glide.with(itemView).load(imageUrlMed).into(MedImg)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaregiverHomeViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.caregiverhome_item, parent, false)
        return CaregiverHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaregiverHomeViewHolder, position: Int) {
          var item: MedicineUiModel = PaientList.get(position)

                holder.bind(item)


    }

    override fun getItemCount(): Int {
        return PaientList.size

    }

}