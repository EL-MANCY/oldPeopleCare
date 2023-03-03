package com.example.oldpeoplecareapp.ui.PatientNotification

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.Circles
import com.example.oldpeoplecareapp.model.entity.notificationData

class NotificationRecyclerView: RecyclerView.Adapter<NotificationRecyclerView.NotificationViewHolder>() {

    var  NotificationList: List<notificationData> = emptyList()

    fun setList(NotificationItems: List<notificationData>) {
        this.NotificationList = NotificationItems
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView =itemView.findViewById(R.id.senderImg)
        val notification: TextView =itemView.findViewById(R.id.notification_txt)
        val date: TextView =itemView.findViewById(R.id.dateNotify)
        val time: TextView =itemView.findViewById(R.id.TimeNotify)

        fun bind(item: notificationData) {
            val imageUrl = item.sender.id.image.url
            image.setBackgroundResource(R.drawable.oval)
            Glide.with(itemView).load(imageUrl).into(image)
            notification.text=item.sender.name +" "+ item.type +" "+ item.description
            notification.setTypeface(null,Typeface.BOLD)
            date.text=item.createdAt.subSequence(0,9)
            time.text=item.createdAt.subSequence(11,15)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        var item: notificationData = NotificationList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return NotificationList.size
    }
}