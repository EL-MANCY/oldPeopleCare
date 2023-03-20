package com.example.oldpeoplecareapp.ui.PatientPath.Emergency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.MedicineRecyclerView
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.OnItemClickListener

class EmergencyRecyclerview: RecyclerView.Adapter<EmergencyRecyclerview.EmergencyViewHolder>() {
    var onListItemClick: OnEmergyClickListener?=null
    var  phoneList: List<Circles> = emptyList()
    fun setList(medicineItems: List<Circles>) {
        this.phoneList = medicineItems
        notifyDataSetChanged()
    }


    inner class EmergencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var callbtn: ImageView = itemView.findViewById(R.id.call_btn)
        var name: TextView = itemView.findViewById(R.id.name_txt)

        fun bind(info: Circles) {
            name.text=info.id.fullname

            callbtn.setOnClickListener {
                onListItemClick?.onItemClick(info)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.emergy_item, parent, false)
        return EmergencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        var item: Circles = phoneList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return phoneList.size

    }
}