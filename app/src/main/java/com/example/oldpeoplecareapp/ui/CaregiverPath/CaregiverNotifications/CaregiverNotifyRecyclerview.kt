package com.example.oldpeoplecareapp.ui.CaregiverPath.CaregiverNotifications

import android.app.Activity
import android.graphics.Color
import android.graphics.Color.red
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.LoadingDialog
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.MessageResponse
import com.example.oldpeoplecareapp.model.entity.notificationData
import com.example.oldpeoplecareapp.ui.CaregiverPath.AllPatients.OnItemClickListener2
import com.example.oldpeoplecareapp.ui.Chat.ChatScreen.MessagesRecyclerView
import com.example.oldpeoplecareapp.ui.PatientPath.PatientNotification.NotificationRecyclerView
import java.text.SimpleDateFormat
import java.util.*

class CaregiverNotifyRecyclerview(val mActivity: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onListItemClick3: OnItemClickListner3? = null
    var NotificationList: List<notificationData> = emptyList()
    val loading = LoadingDialog(mActivity)


    fun setList(NotificationItems: List<notificationData>) {
        this.NotificationList = NotificationItems
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_NORMAL = 1
        private const val VIEW_TYPE_REQUEST = 2
    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.senderImg)
        val notification: TextView = itemView.findViewById(R.id.notification_txt)
        val date: TextView = itemView.findViewById(R.id.dateNotify)
        val time: TextView = itemView.findViewById(R.id.TimeNotify)

        fun bind(item: notificationData) {
            val imageUrl = item.sender.id.image.url
            image.setBackgroundResource(R.drawable.oval)
            Glide.with(itemView).load(imageUrl).into(image)
            notification.text =
                Html.fromHtml("<b>${item.sender.name}</b>" + " " + "${item.description}")

            //  notification.setTypeface(null,Typeface.BOLD)
            date.text = item.createdAt.subSequence(0, 10)
            var time24 = item.createdAt.subSequence(11, 16).toString()

            var time12 = convertTimeTo12HourFormat(time24)
            time.text = time12
        }
    }

    inner class RequestHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.senderPic)
        val notification: TextView = itemView.findViewById(R.id.notify_txt)
        val date: TextView = itemView.findViewById(R.id.dateNotification)
        val time: TextView = itemView.findViewById(R.id.TimeNotifification)
        val acceptBtn: Button = itemView.findViewById(R.id.acceptBtn)
        val rejectBtn: Button = itemView.findViewById(R.id.rejectBtn)
        val status: TextView = itemView.findViewById(R.id.status)


        fun bind(item: notificationData) {
            val imageUrl = item.sender.id.image.url
            image.setBackgroundResource(R.drawable.oval)
            Glide.with(itemView).load(imageUrl).into(image)
            notification.text =
                Html.fromHtml("<b>${item.sender.name}</b>" + " " + "${item.description}")
            //  notification.setTypeface(null,Typeface.BOLD)
            date.text = item.createdAt.subSequence(0, 10)
            var time24 = item.createdAt.subSequence(11, 16).toString()

            var time12 = convertTimeTo12HourFormat(time24)
            time.text = time12

            acceptBtn.setOnClickListener {
                acceptBtn.visibility = View.GONE
                rejectBtn.visibility = View.GONE
                status.visibility = View.VISIBLE
                status.text = "Accepted"
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.active))
                onListItemClick3?.accepted(item)

            }

            rejectBtn.setOnClickListener {
                acceptBtn.visibility = View.GONE
                rejectBtn.visibility = View.GONE
                status.visibility = View.VISIBLE
                status.text = "Rejected"
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.darkRed))
                onListItemClick3?.rejected(item)

            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.notification_item, parent, false)
                NormalViewHolder(view)
            }
            VIEW_TYPE_REQUEST -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.caregiver_notifiy_item, parent, false)
                RequestHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var item: notificationData = NotificationList.get(position)
        when (holder) {
            is NormalViewHolder -> holder.bind(item)
            is RequestHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return NotificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item: notificationData = NotificationList[position]
        return if (item.type == "request") {
            VIEW_TYPE_REQUEST
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    fun convertTimeTo12HourFormat(time24: String): String {
        val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = inputFormat.parse(time24)
        return outputFormat.format(date)
    }
}