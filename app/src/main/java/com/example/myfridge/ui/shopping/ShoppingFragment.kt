package com.example.myfridge.ui.shopping

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfridge.R
import com.example.myfridge.data.database.ShoppingListItemInfo
import com.example.myfridge.databinding.FragmentShoppingBinding
import com.example.myfridge.ui.database.DatabaseViewModel
import com.example.myfridge.ui.recipes.ShoppingAdapter


class ShoppingFragment : Fragment() {
    private val viewModel: DatabaseViewModel.ShoppingListItemInfoViewModel by viewModels()
    private var _binding: FragmentShoppingBinding? = null
    private lateinit var shoppingAdapter: ShoppingAdapter
    private lateinit var shoppingRv: RecyclerView
    private val args : ShoppingFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater){
                menuInflater.inflate(R.menu.shoppinglist_add_bar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId){
                    R.id.action_add_shoppingList_item -> {
                        Log.d("ShoppingFrag", "Re-routing")
                        val destination = ShoppingFragmentDirections.actionNavShoppingListToAddShoppingListItemFragment()
                        findNavController().navigate(destination)
                        true
                    }
                    else -> {
                        (activity as AppCompatActivity).onOptionsItemSelected(menuItem)
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        //grab/bind to the xml file
        _binding = FragmentShoppingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //recycler view for the shopping list
        shoppingRv = binding.rvShopping
        shoppingRv.layoutManager = LinearLayoutManager(container?.context)
        shoppingAdapter = ShoppingAdapter(::onDeleteButtonClick)
        shoppingRv.adapter = shoppingAdapter

        Log.d("Shopping Fragment", args.shoppingListName)
        viewModel.getItemsForList(args.shoppingListName).observe(viewLifecycleOwner){
            Log.d("Shopping Fragment", it.toString())
            shoppingAdapter.updateShoppingList(it)
        }

        return root
    }

    fun onDeleteButtonClick(shoppingListItemInfo: ShoppingListItemInfo){
        viewModel.deleteShoppingListItemInfo(shoppingListItemInfo)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}