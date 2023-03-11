package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverHome

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.MedicineRecyclerView
import com.example.oldpeoplecareapp.ui.PatientPath.patientHome.OnItemClickListener
import java.io.InputStream
import java.net.URL

class CaregiverHomeRecyclerView: RecyclerView.Adapter<CaregiverHomeRecyclerView.PatientViewHolder>() {

    var  PaientList: List<Any> = emptyList()
    fun setList(PatientItems: List<Any>) {
        this.PaientList = PatientItems
        notifyDataSetChanged()
    }


    inner class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(medicineInfo: Any) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.caregiverhome_item, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        var item: Any = PaientList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return PaientList.size

    }

}