package com.example.oldpeoplecareapp.ui.PatientPath.AddNewMedicine

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone

class TimeRecyclerView: RecyclerView.Adapter<TimeRecyclerView.TimeViewHolder>() {

    var  TimeList: List<String> = emptyList()

    fun setList(TimeItems: List<String>) {
        this.TimeList = TimeItems
        notifyDataSetChanged()
    }

    inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time:TextView = itemView.findViewById(R.id.time_txt)
        val img:ImageView = itemView.findViewById(R.id.delete_btn)

        fun bind(MedTime: String) {
            time.text= MedTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.time_item, parent, false)
        return TimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        var item: String = TimeList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return TimeList.size
    }
}