package com.example.oldpeoplecareapp.ui.Chat.ChatScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.MySharedPreferences
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.MessageResponse
import com.example.oldpeoplecareapp.model.entity.Reciever
import com.example.oldpeoplecareapp.model.entity.SearchResponseItem
import com.example.oldpeoplecareapp.ui.PatientPath.Search.OnUserClickListener

class MessagesRecyclerView(private val sharedPreferences: MySharedPreferences) :
    RecyclerView.Adapter<MessagesRecyclerView.MessageViewHolder>() {
    var messages: List<MessageResponse> = emptyList()
    var onListItemClick: OnUserClickListener? = null
    var Reciever: Boolean = false

    val id = sharedPreferences.getValue("ID", "")

    fun setList(msgs: List<MessageResponse>) {
        this.messages = msgs
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val msgSend: TextView = itemView.findViewById(R.id.messageSend)


        fun bind(item: MessageResponse) {

            if (item.sender == id) {
                Reciever = false
                msgSend.text = item.content

            } else {
                Reciever = true
                val specialLayout: View =
                    LayoutInflater.from(itemView.context).inflate(R.layout.reciever_msg, null)
                val msgRecieve: TextView = specialLayout.findViewById(R.id.messageRecieve)

                msgRecieve.text = item.content

            }


//            itemView.setOnClickListener {
//                onListItemClick?.onItemClick(item)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutResId = if (Reciever) {
            R.layout.reciever_msg
        } else {
            R.layout.sender_msg
        }
        val view: View = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        var item: MessageResponse = messages.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}


