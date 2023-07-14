package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.Medicine


class MedicineRecyclerView: RecyclerView.Adapter<MedicineRecyclerView.MedicineViewHolder>() {
    var onListItemClick:OnItemClickListener?=null
    var  medicineList: List<Medicine> = emptyList()
    fun setList(medicineItems: List<Medicine>) {
        this.medicineList = medicineItems
        notifyDataSetChanged()
    }
    fun refresh() {
        notifyDataSetChanged()
    }

    inner class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var medicineImage: ImageView = itemView.findViewById(R.id.medddImg)
        var time_txtView: TextView = itemView.findViewById(R.id.time_txtView)
        var medicineNameTxtView: TextView = itemView.findViewById(R.id.fullname_txtfield)
        var edit_icon: ImageView = itemView.findViewById(R.id.edit_btnx)
        var mark_icon: ImageView = itemView.findViewById(R.id.comp_img)

        fun bind(medicineInfo: Medicine) {

            time_txtView.text = medicineInfo.medicine.time[0]

            medicineNameTxtView.text = medicineInfo.medicine.name

            val imageUrl = medicineInfo.medicine.image.url
            medicineImage.setBackgroundResource(R.drawable.oval)
            Glide.with(itemView).load(imageUrl).into(medicineImage)


            if (medicineInfo.state == "Completed") {
                    mark_icon.setImageResource(R.drawable.yes_comp)
                } else if (medicineInfo.state == "Missed") {
                    mark_icon.setImageResource(R.drawable.no_compe_24)
                } else if (medicineInfo.state == "Waiting") {
                    mark_icon.setImageResource(R.drawable.no_compe_24)
                }
                edit_icon.setOnClickListener {
                    onListItemClick?.onItemClick(medicineInfo)
                }
                mark_icon.setOnClickListener {
                    onListItemClick?.onStateClick(medicineInfo)
                }

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.medicine_list_item, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        var item: Medicine = medicineList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return medicineList.size

    }
}