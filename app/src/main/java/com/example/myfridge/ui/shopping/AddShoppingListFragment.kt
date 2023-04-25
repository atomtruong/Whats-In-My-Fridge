package com.example.myfridge.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myfridge.data.database.ShoppingListInfo
import com.example.myfridge.data.database.ShoppingListItemInfo
import com.example.myfridge.databinding.FragmentAddShoppingListBinding
import com.example.myfridge.databinding.FragmentAddShoppingListItemBinding
import com.example.myfridge.ui.database.DatabaseViewModel

class AddShoppingListFragment : Fragment() {
    private val itemViewModel: DatabaseViewModel.ShoppingListItemInfoViewModel by viewModels()
    private val listViewModel: DatabaseViewModel.ShoppingListInfoViewModel by viewModels()

    private var _binding: FragmentAddShoppingListBinding? = null

    private val binding get() = _binding!!
    private lateinit var addName: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddShoppingListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.title.text = "Create a New Shopping List"
        addName = binding.addName
        val submitButton = _binding!!.addShoppingListButton

        submitButton.setOnClickListener() {
            val name = addName.text.toString()
            listViewModel.addShoppingListInfo(ShoppingListInfo(name))
            findNavController().navigateUp()
        }
        return root
    }
}