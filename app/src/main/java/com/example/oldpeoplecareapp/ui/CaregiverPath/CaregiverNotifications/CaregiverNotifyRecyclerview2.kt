package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications

import android.app.Activity
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.notificationData
import com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients.OnItemClickListener2
import com.example.oldpeoplecareapp.ui.PatientPath.PatientNotification.NotificationRecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CaregiverNotifyRecyclerview2(val mActivity: Activity) : RecyclerView.Adapter<CaregiverNotifyRecyclerview2.NotificationCaregiveViewHolder>() {
    var onListItemClick3: OnItemClickListner3? =null
    var  NotificationList: List<notificationData> = emptyList()
    val loading= LoadingDialog(mActivity)


    fun setList(NotificationItems: List<notificationData>) {
        this.NotificationList = NotificationItems
        notifyDataSetChanged()
    }

    inner class NotificationCaregiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView =itemView.findViewById(R.id.senderPic)
        val notification: TextView =itemView.findViewById(R.id.notify_txt)
        val date: TextView =itemView.findViewById(R.id.dateNotification)
        val time: TextView =itemView.findViewById(R.id.TimeNotifification)
        val acceptBtn: Button =itemView.findViewById(R.id.acceptBtn)
        val rejectBtn: Button =itemView.findViewById(R.id.rejectBtn)
        val status :TextView = itemView.findViewById(R.id.status)


        fun bind(item: notificationData) {
            val imageUrl = item.sender.id.image.url
            image.setBackgroundResource(R.drawable.oval)
            Glide.with(itemView).load(imageUrl).into(image)
            notification.text= Html.fromHtml("<b>${item.sender.name}</b>"+" "+"${item.description}")
            //  notification.setTypeface(null,Typeface.BOLD)
            date.text=item.createdAt.subSequence(0,10)
            var time24=item.createdAt.subSequence(11,16).toString()

            var time12 = convertTimeTo12HourFormat(time24)
            time.text = time12

            if(item.type == "request"){
                acceptBtn.visibility = View.VISIBLE
                rejectBtn.visibility = View.VISIBLE
            }else {
                acceptBtn.visibility = View.GONE
                rejectBtn.visibility = View.GONE
            }

            acceptBtn.setOnClickListener {
                acceptBtn.visibility=View.GONE
                rejectBtn.visibility=View.GONE
                status.visibility = View.VISIBLE
                status.text = "Accepted"
                status.setTextColor(Color.GREEN)
                onListItemClick3?.accepted(item)

            }

            rejectBtn.setOnClickListener {
                acceptBtn.visibility=View.GONE
                rejectBtn.visibility=View.GONE
                status.visibility = View.VISIBLE
                status.text = "Rejected"
                status.setTextColor(Color.RED)
                onListItemClick3?.rejected(item)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationCaregiveViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.caregiver_notifiy_item, parent, false)
        return NotificationCaregiveViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationCaregiveViewHolder, position: Int) {
        var item: notificationData = NotificationList.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return NotificationList.size
    }

    fun convertTimeTo12HourFormat(time24: String): String {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(time24)
        return outputFormat.format(date)
    }
}