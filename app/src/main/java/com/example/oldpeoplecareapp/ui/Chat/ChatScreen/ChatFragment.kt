package com.example.oldpeoplecareapp.ui.Chat.ChatScreen

import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.MySharedPreferences
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentChatBinding
import com.example.oldpeoplecareapp.databinding.FragmentChatContactsFragmentsBinding
import com.example.oldpeoplecareapp.model.entity.MessageResponse
import com.example.oldpeoplecareapp.ui.Chat.Conversations.ConversationsRecyclerView
import com.example.oldpeoplecareapp.ui.Chat.Conversations.ConversationsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


class ChatFragment : Fragment() {


    private lateinit var navController: NavController
    lateinit var binding: FragmentChatBinding
    lateinit var retrivedToken: String
    lateinit var retrivedID: String
    lateinit var chatViewModel: ChatViewModel
    val TAG = "ChatFragment"

    var MsgList :MutableList<MessageResponse> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        retrivedID = getpreferences.getString("ID", null).toString()

        val args = ChatFragmentArgs.fromBundle(requireArguments())
        val recieverId = args.id
        val fullname = args.fullname
        val image = args.image

        val sharedPreferences = MySharedPreferences(requireContext())
        val messagesRecyclerView by lazy { MessagesRecyclerView(sharedPreferences) }


        socketHandler.setSocket()
        socketHandler.establishConnection()
        val mSocket = socketHandler.getSocket()

        mSocket.emit("add-user",retrivedID)

        val handler = Handler(Looper.getMainLooper())

        mSocket.on("msg-recieve") { args ->
            if (args[0] != null) {
                val msg = args[0] as String

                Log.i("SocketIO",msg)

                handler.post {

                    MsgList.add(MessageResponse(0,"",msg,retrivedID,recieverId,
                        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time)))
                    messagesRecyclerView.setList(MsgList)

                }
            }
        }




        navController = NavHostFragment.findNavController(this)
        val bottomNavigation2: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation2)
        val bottomNavigation: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)

        if (navController.currentDestination?.label == "fragment_chat"
        ) {
            bottomNavigation.visibility = View.GONE
            bottomNavigation2.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
            bottomNavigation2.visibility = View.VISIBLE
        }




        binding.RecieverName.text=fullname

        binding.recieverImage.setBackgroundResource(R.drawable.oval)

        Glide.with(this).load(image).into(binding.recieverImage)

        chatViewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)

        chatViewModel.getConversation("barier "+retrivedToken,recieverId)

        binding.editTextMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Perform your desired action here when the "Done" button is pressed
                // For example, you can close the keyboard or submit the form
                chatViewModel.sendMessage("barier "+retrivedToken,recieverId,binding.editTextMessage.text.toString())
                mSocket.emit("send-msg",binding.editTextMessage.text.toString())

                binding.editTextMessage.setText("")
                return@setOnEditorActionListener true
            }
            false
        }


        chatViewModel.ConversationLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                messagesRecyclerView.setList(it)
                MsgList=it.toMutableList()
                Log.i(TAG, it.toString())
            } else if (chatViewModel.error != null) {
                Snackbar.make(
                    view,
                    chatViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            chatViewModel.error = null
        })

        chatViewModel.MessageLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                MsgList.add(it)

                messagesRecyclerView.setList(MsgList)
                Log.i(TAG, it.toString())
            } else if (chatViewModel.error != null) {
                Snackbar.make(
                    view,
                    chatViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            chatViewModel.error = null
        })

        binding.messagesRecyclerView.adapter = messagesRecyclerView

    }

}