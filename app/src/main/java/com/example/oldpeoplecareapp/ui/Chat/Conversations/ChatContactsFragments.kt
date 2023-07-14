package com.example.oldpeoplecareapp.ui.Chat.Conversations

import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.MySharedPreferences
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentChatBinding
import com.example.oldpeoplecareapp.databinding.FragmentChatContactsFragmentsBinding
import com.example.oldpeoplecareapp.model.entity.AllConversationsResponseItem
import com.example.oldpeoplecareapp.ui.Chat.ChatScreen.ChatViewModel
import com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient.CaregiversPatientFragmentDirections
import com.example.oldpeoplecareapp.ui.PatientPath.Search.SearchRecyclerView
import com.example.oldpeoplecareapp.ui.PatientPath.Search.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*


class ChatContactsFragments : Fragment(), OnContactClickListener {

    val TAG = "ChatContactsFragments"
    private lateinit var navController: NavController
    lateinit var binding: FragmentChatContactsFragmentsBinding
    lateinit var retrivedToken: String
    lateinit var retrivedID: String
    lateinit var conversationsViewModel: ConversationsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatContactsFragmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPreferences = MySharedPreferences(requireContext())
        val conversationRecyclerView by lazy { ConversationsRecyclerView(sharedPreferences) }

        navController = NavHostFragment.findNavController(this)
        val bottomNavigation2: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation2)
        val bottomNavigation: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)

        if (navController.currentDestination?.label == "ChatContactsFragments"
        ) {
            bottomNavigation.visibility = View.GONE
            bottomNavigation2.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
            bottomNavigation2.visibility = View.VISIBLE
        }

        conversationsViewModel =
            ViewModelProvider(requireActivity()).get(ConversationsViewModel::class.java)

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        retrivedID = getpreferences.getString("ID", "") ?: ""

        conversationsViewModel.getAllConversations("barier " + retrivedToken)
        binding.convRV.adapter = conversationRecyclerView


        conversationsViewModel.ConversationLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                conversationRecyclerView.setList(it)
                Log.i(TAG, it.toString())
            } else if (conversationsViewModel.error != null) {
                Snackbar.make(
                    view,
                    conversationsViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            conversationsViewModel.error = null
        })

        binding.backBtn.setOnClickListener {
//            findNavController().navigate(ChatContactsFragmentsDirections.actionChatContactsFragmentsToCaregiversPatientFragment())
            findNavController().navigateUp()
        }
        conversationRecyclerView.onListItemClick = this

    }

    override fun onItemClick(info: AllConversationsResponseItem) {
        val fullname: String
        val id: String
        val image: String
        if (info.participants[0].user._id == retrivedID) {
            fullname = info.participants[1].user.fullname
            image = info.participants[1].user.image.url
            id=info.participants[1].user._id
        } else {
            fullname = info.participants[0].user.fullname
            image = info.participants[0].user.image.url
            id=info.participants[0].user._id

        }
        findNavController().navigate(ChatContactsFragmentsDirections.actionChatContactsFragmentsToChatFragment(id,image,fullname))
    }

}