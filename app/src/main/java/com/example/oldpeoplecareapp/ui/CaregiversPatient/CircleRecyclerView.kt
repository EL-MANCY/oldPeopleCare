package com.example.oldpeoplecareapp.ui.CaregiversPatient

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.AllMedicineRespone
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.model.entity.Image

class CircleRecyclerView:RecyclerView.Adapter<CircleRecyclerView.CircleViewHolder>() {
    var  CircleList: List<Circles> = emptyList()
    fun setList(CircleItems: List<Circles>) {
        this.CircleList = CircleItems
        notifyDataSetChanged()
    }

    inner class CircleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView =itemView.findViewById(R.id.patientImg)
        val fullname:TextView=itemView.findViewById(R.id.fullname_txtfield)
        val role:TextView=itemView.findViewById(R.id.role_txtfield)

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


