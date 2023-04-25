package com.example.myfridge.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kennyc.view.MultiStateView
import com.daimajia.swipe.util.Attributes
import com.example.myfridge.R
import com.example.myfridge.data.database.FridgeItemInfo
import com.example.myfridge.data.fridge.FridgeContent
import com.example.myfridge.databinding.FragmentHomeBinding
import com.example.myfridge.ui.database.DatabaseViewModel
import com.quickersilver.themeengine.ThemeEngine
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter


class HomeFragment : Fragment() {
    private lateinit var themeEngine: ThemeEngine
    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var homeRv: RecyclerView
    private val viewModel: DatabaseViewModel.FridgeItemInfoViewModel by viewModels()
//    private val shoppingViewModel: DatabaseViewModel.ShoppingListViewModel by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        themeEngine = ThemeEngine.getInstance(requireContext())
        (activity as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater){
                menuInflater.inflate(R.menu.main_activity, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId){
                    R.id.action_add_fridge_item -> {
                        val destination = HomeFragmentDirections.actionNavHomeToAddFoodFragment()
                        findNavController().navigate(destination)
                        true
                    }
                    else -> (activity as AppCompatActivity).onOptionsItemSelected(menuItem)
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        homeRv = root.findViewById(R.id.rv_home)
        homeRv.layoutManager = LinearLayoutManager(context)
        homeAdapter = HomeAdapter(::onShopClick, ::onDeleteClick)
        homeAdapter.mode = Attributes.Mode.Single
        homeRv.adapter = SlideInLeftAnimationAdapter(homeAdapter).apply {
            // Change the durations.
            setDuration(4000)
            // Change the interpolator.
            setInterpolator(OvershootInterpolator())
            // Disable the first scroll mode.
            setFirstOnly(false)
        }
        viewModel.fridgeItemInfoAll.observe(viewLifecycleOwner) {
            binding.multiStateView.viewState = MultiStateView.ViewState.CONTENT
            homeAdapter.updateHomeList(FridgeContent(it!!))

        }
        binding.multiStateView.viewState = MultiStateView.ViewState.LOADING

        return root
    }

    fun onShopClick(name: String){
        val destination = HomeFragmentDirections.actionNavHomeToAddShoppingFragment(name)
        val navController = findNavController()
        navController.navigate(destination)
    }

    fun onDeleteClick(name:String) {
        viewModel.deleteFridgeItemInfo(FridgeItemInfo(null, name, null))
        val navController = findNavController()
        navController.navigate(R.id.nav_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}