package com.example.oldpeoplecareapp.ui.PatientPath.Search

import android.content.Context
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import com.example.oldpeoplecareapp.R
import com.example.oldpeoplecareapp.databinding.FragmentSearchBinding
import com.example.oldpeoplecareapp.model.entity.SearchResponseItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(),OnUserClickListener {
    val TAG = "SearchFragment"

    private lateinit var navController: NavController
    lateinit var binding: FragmentSearchBinding
    lateinit var retrivedToken: String
    lateinit var searchViewModel: SearchViewModel
    val searchRecyclerView by lazy { SearchRecyclerView() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = NavHostFragment.findNavController(this)
        val bottomNavigation2: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation2)
        val bottomNavigation: BottomNavigationView =
            requireActivity().findViewById(R.id.bottom_navigation)

        if (navController.currentDestination?.label == "SearchFragment"
        ) {
            bottomNavigation.visibility = View.GONE
            bottomNavigation2.visibility = View.GONE
        } else {
            bottomNavigation.visibility = View.VISIBLE
            bottomNavigation2.visibility = View.VISIBLE
        }

        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)


        val getpreferences = requireActivity().getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        retrivedToken = getpreferences.getString("TOKEN", null).toString()
        val retrivedID = getpreferences.getString("ID", null)

        binding.searchX.editText!!.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Perform your desired action here when the "Done" button is pressed
                // For example, you can close the keyboard or submit the form
                searchViewModel.searchUser("barier "+retrivedToken, binding.searchX.editText!!.text.toString())

                return@setOnEditorActionListener true
            }
            false
        }

        binding.SearchRecyclerView.adapter = searchRecyclerView

        searchViewModel.UsersLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                searchRecyclerView.setList(it)
                Log.i(TAG, it.toString())
            }else if(searchViewModel.error != null){
                Snackbar.make(
                    SEARCH,
                    searchViewModel.error.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            searchViewModel.error = null
        })


        binding.backBtn.setOnClickListener {
//            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCaregiversPatientFragment())
            findNavController().navigateUp()
        }

        searchRecyclerView.onListItemClick = this

    }

    override fun onItemClick(info: SearchResponseItem) {
        findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCareGiverProfileFragment(info._id))
    }

}