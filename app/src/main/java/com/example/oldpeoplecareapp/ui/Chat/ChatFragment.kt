package com.example.oldpeoplecareapp.ui.Chat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentChatBinding
import com.example.oldpeoplecareapp.ui.PatientPath.CaregiversPatient.CaregiversPatientViewModel
import com.example.oldpeoplecareapp.ui.PatientPath.EditRemoveCareGiver.EditRemoveCaregiverRoleArgs
import com.google.android.material.bottomnavigation.BottomNavigationView


class ChatFragment : Fragment() {

    private lateinit var navController: NavController
    lateinit var binding: FragmentChatBinding
    lateinit var retrivedToken: String
    lateinit var chatViewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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

        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        val retrivedID = getpreferences.getString("ID", null)

        val args = ChatFragmentArgs.fromBundle(requireArguments())
        val recieverId = args.id
        val fullname = args.fullname
        val image = args.image

        binding.RecieverName.text=fullname

        binding.recieverImage.setBackgroundResource(R.drawable.oval)

        Glide.with(this).load(image).into(binding.recieverImage)

        chatViewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)

        chatViewModel.getConversation(retrivedToken,recieverId)

        binding.editTextMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Perform your desired action here when the "Done" button is pressed
                // For example, you can close the keyboard or submit the form
                chatViewModel.sendMessage("barier "+retrivedToken,recieverId,binding.editTextMessage.text.toString())

                return@setOnEditorActionListener true
            }
            false
        }
    }

}