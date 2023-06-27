package com.example.oldpeoplecareapp.ui.PatientPath.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.model.entity.SearchResponseItem
import com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient.OnCaregiverClickListener

class SearchRecyclerView : RecyclerView.Adapter<SearchRecyclerView.SerachViewHolder>() {
    var users: List<SearchResponseItem> = emptyList()
    var onListItemClick: OnUserClickListener? = null

    fun setList(users: List<SearchResponseItem>) {
        this.users = users
        notifyDataSetChanged()
    }

    inner class SerachViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.user)
        val fullname: TextView = itemView.findViewById(R.id.userName)

        fun bind(item: SearchResponseItem) {

            if (item.image !=null){
            val imageUrl = item.image.url
            image.setBackgroundResource(R.drawable.oval)
            Glide.with(itemView).load(imageUrl).into(image)}
            fullname.text = item.fullname
            itemView.setOnClickListener {
                onListItemClick?.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerachViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return SerachViewHolder(view)
    }

    override fun onBindViewHolder(holder: SerachViewHolder, position: Int) {
        var item: SearchResponseItem = users.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}


