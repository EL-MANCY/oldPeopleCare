package com.example.oldpeoplecareapp.ui.Chat.Conversations

import com.example.oldpeoplecareapp.model.entity.AllConversationsResponseItem
import com.example.oldpeoplecareapp.model.entity.SearchResponseItem

interface OnContactClickListener {
    fun onItemClick(info: AllConversationsResponseItem)

}