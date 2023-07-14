package com.example.oldpeoplecareapp.ui.PatientPath.AllAlarms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.AllMedicineResponseItem
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.OnItemClickListener

class AllMedicineRV: RecyclerView.Adapter<AllMedicineRV.MedicineViewHolder>() {

    var onListItemClick: OnAlarmClickListener?=null
    var  medicineList: List<AllMedicineResponseItem> = emptyList()

    fun setList(medicineItems: List<AllMedicineResponseItem>) {
        this.medicineList = medicineItems
        notifyDataSetChanged()
    }


    inner class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var medicineImage: ImageView = itemView.findViewById(R.id.medicineImage)
        var time_txtView: TextView = itemView.findViewById(R.id.time_txtx)
        var date_txtView: TextView = itemView.findViewById(R.id.date_txtx)
        var medicineNameTxtView: TextView = itemView.findViewById(R.id.fullname_txt)
        var edit_icon: ImageView = itemView.findViewById(R.id.edit_btnxx)

        fun bind(medicineInfo: AllMedicineResponseItem) {

            val imageUrl = medicineInfo.image?.url
            medicineImage.setBackgroundResource(R.drawable.oval)
            Glide.with(itemView).load(imageUrl).into(medicineImage)

            time_txtView.text = medicineInfo.time[0]
            date_txtView.text = medicineInfo.createdAt.subSequence(11,16).toString()

            medicineNameTxtView.text = medicineInfo.name

            edit_icon.setOnClickListener {
                onListItemClick?.onItemClick(medicineInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.all_alarms_items, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        var item: AllMedicineResponseItem = medicineList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return medicineList.size

    }
}