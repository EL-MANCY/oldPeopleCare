package com.example.oldpeoplecareapp.ui.PatientPath.patientHome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone


class MedicineRecyclerView: RecyclerView.Adapter<MedicineRecyclerView.MedicineViewHolder>() {
    var onListItemClick:OnItemClickListener?=null
    var  medicineList: List<AllMedicineRespone> = emptyList()
    fun setList(medicineItems: List<AllMedicineRespone>) {
        this.medicineList = medicineItems
        notifyDataSetChanged()
    }


    inner class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var medicineImage: ImageView = itemView.findViewById(R.id.medicineImage)
        var time_txtView: TextView = itemView.findViewById(R.id.time_txtView)
        var medicineNameTxtView: TextView = itemView.findViewById(R.id.fullname_txtfield)
        var edit_icon: ImageView = itemView.findViewById(R.id.edit_icon)
        var mark_icon: ImageView = itemView.findViewById(R.id.comp_img)

        fun bind(medicineInfo: AllMedicineRespone) {

            time_txtView.text = medicineInfo.time
            medicineNameTxtView.text = medicineInfo.name
            edit_icon.setImageResource(R.drawable.edit_icon)
            mark_icon.setImageResource(R.drawable.correct_mark)

            edit_icon.setOnClickListener {
                onListItemClick?.onItemClick(medicineInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.medicine_list_item, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        var item: AllMedicineRespone = medicineList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return medicineList.size

    }
}