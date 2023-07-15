package com.example.oldpeoplecareapp.ui.Chat.ChatScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.oldpeoplecareapp.MySharedPreferences
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.MessageResponse
import com.example.oldpeoplecareapp.ui.PatientPath.Search.OnUserClickListener

class MessagesRecyclerView(private val sharedPreferences: MySharedPreferences) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messages: List<MessageResponse> = emptyList()
    var onListItemClick: OnUserClickListener? = null

    fun setList(msgs: List<MessageResponse>) {
        this.messages = msgs
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_SENDER = 1
        private const val VIEW_TYPE_RECEIVER = 2
    }

    inner class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val msgSend: TextView = itemView.findViewById(R.id.messageSend)
        private val time: TextView = itemView.findViewById(R.id.ttime)

        fun bind(item: MessageResponse) {
            msgSend.text = item.content
            val d = item.timestamp.subSequence(11,16)
            time.text=d

//            itemView.setOnClickListener {
//                onListItemClick?.onItemClick(item)
//            }
        }
    }

    inner class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val msgReceive: TextView = itemView.findViewById(R.id.messageRecieve)
        private val time: TextView = itemView.findViewById(R.id.ttime2)

        fun bind(item: MessageResponse) {
            msgReceive.text = item.content
            val d = item.timestamp.subSequence(11,16)
            time.text=d

//            itemView.setOnClickListener {
//                onListItemClick?.onItemClick(item)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_SENDER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sender_msg, parent, false)
                SenderViewHolder(view)
            }
            VIEW_TYPE_RECEIVER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.reciever_msg, parent, false)
                ReceiverViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: MessageResponse = messages[position]

        when (holder) {
            is SenderViewHolder -> holder.bind(item)
            is ReceiverViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val item: MessageResponse = messages[position]
        return if (item.sender == sharedPreferences.getValue("ID", "")) {
            VIEW_TYPE_SENDER
        } else {
            VIEW_TYPE_RECEIVER
        }
    }


}



