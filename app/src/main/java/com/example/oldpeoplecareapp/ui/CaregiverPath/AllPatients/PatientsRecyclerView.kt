package com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.Circles


class PatientsRecyclerView: RecyclerView.Adapter<PatientsRecyclerView.PatientViewHolder>() {
    var  CircleList: List<Circles> = emptyList()
    var onListItemClick2: OnItemClickListener2?=null

    fun setList(CircleItems: List<Circles>) {
        this.CircleList = CircleItems
        notifyDataSetChanged()
    }



    inner class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView =itemView.findViewById(R.id.patientPicture)
        val fullname: TextView =itemView.findViewById(R.id.patientFullname_txtfield)
        val phone: TextView =itemView.findViewById(R.id.phNo_txtfield)
        var phonebtn: ImageView = itemView.findViewById(R.id.phoneBtn)
        var chat: ImageView = itemView.findViewById(R.id.chatx)



        fun bind(item: Circles) {
            val imageUrl = item.id.image.url
            image.setBackgroundResource(R.drawable.oval)
            fullname.text=item.id.fullname
            phone.text=item.id.phone


            Glide.with(itemView).load(imageUrl).into(image)

            phonebtn.setOnClickListener {
                onListItemClick2?.onItemClick2(item)
            }
            chat.setOnClickListener {
                onListItemClick2?.onItemClick1(item)
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.all_patients_item, parent, false)
        return PatientViewHolder(view)    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        var item: Circles = CircleList.get(position)
        holder.bind(item)    }

    override fun getItemCount(): Int {
        return CircleList.size
    }
}


