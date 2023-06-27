package com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.Circles

class CircleRecyclerView:RecyclerView.Adapter<CircleRecyclerView.CircleViewHolder>() {
    var  CircleList: List<Circles> = emptyList()
    var onListItemClick: OnCaregiverClickListener?=null

    fun setList(CircleItems: List<Circles>) {
        this.CircleList = CircleItems
        notifyDataSetChanged()
    }

    inner class CircleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView =itemView.findViewById(R.id.patientImg)
        val fullname:TextView=itemView.findViewById(R.id.fullname_txtfield)
        val role:TextView=itemView.findViewById(R.id.role_txtfield)
        val edit_icon :ImageView = itemView.findViewById(R.id.editButton)
        val chat_icon :ImageView = itemView.findViewById(R.id.chat)

        fun bind(item: Circles) {

            val imageUrl = item.id.image.url

            image.setBackgroundResource(R.drawable.oval)

            fullname.text=item.id.fullname
            role.text=item.role
//            if (item.role=="editor"){
//                itemView.findViewById<ImageView>(R.id.editbtn).visibility=View.VISIBLE
//            }else{
//                itemView.findViewById<ImageView>(R.id.editbtn).visibility=View.GONE
//            }
            Glide.with(itemView).load(imageUrl).into(image)

            chat_icon.setOnClickListener {
                onListItemClick?.onChatClick(item)
            }
            edit_icon.setOnClickListener {
                onListItemClick?.onItemClick(item)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CircleViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.caregiver_item, parent, false)
        return CircleViewHolder(view)    }

    override fun onBindViewHolder(holder: CircleViewHolder, position: Int) {
        var item: Circles = CircleList.get(position)
        holder.bind(item)    }

    override fun getItemCount(): Int {
        return CircleList.size
    }
}


