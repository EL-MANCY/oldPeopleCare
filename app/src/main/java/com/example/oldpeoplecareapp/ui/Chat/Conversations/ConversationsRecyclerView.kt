package com.example.oldpeoplecareapp.ui.Chat.Conversations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.MySharedPreferences
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.model.entity.AllConversationsResponseItem
import com.example.oldpeoplecareapp.model.entity.SearchResponseItem
import com.example.oldpeoplecareapp.ui.PatientPath.Search.OnUserClickListener
import com.example.oldpeoplecareapp.ui.PatientPath.Search.SearchRecyclerView

class ConversationsRecyclerView(private val sharedPreferences: MySharedPreferences): RecyclerView.Adapter<ConversationsRecyclerView.ConversationsViewHolder>() {
    var users: List<AllConversationsResponseItem> = emptyList()
    var onListItemClick: OnContactClickListener? = null

    fun setList(users: List<AllConversationsResponseItem>) {
        this.users = users
        notifyDataSetChanged()
    }

    inner class ConversationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.contactImg)
        val fullname: TextView = itemView.findViewById(R.id.NameTxt)
        val msg: TextView = itemView.findViewById(R.id.msg)
        val date: TextView = itemView.findViewById(R.id.date)

        fun bind(item: AllConversationsResponseItem) {

            val id=sharedPreferences.getValue("ID", "")
            msg.text=item.messages[0].content
            val d = item.messages[0].timestamp.subSequence(11,16)
            date.text=d
            if(item.participants[0].user._id ==id ) {
                fullname.text = item.participants[1].user.fullname
                val imageUrl = item.participants[1].user.image.url
                image.setBackgroundResource(R.drawable.oval)
                Glide.with(itemView).load(imageUrl).into(image)
            }else{
                fullname.text = item.participants[0].user.fullname
                val imageUrl = item.participants[0].user.image.url
                image.setBackgroundResource(R.drawable.oval)
                Glide.with(itemView).load(imageUrl).into(image)
            }

            itemView.setOnClickListener {
                onListItemClick?.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationsViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ConversationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationsViewHolder, position: Int) {
        var item: AllConversationsResponseItem = users.get(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}


