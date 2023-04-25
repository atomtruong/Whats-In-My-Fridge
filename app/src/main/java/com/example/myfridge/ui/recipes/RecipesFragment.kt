package com.example.myfridge.ui.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myfridge.BuildConfig
import com.example.myfridge.R
import com.example.myfridge.data.RecyclerStatus
import com.example.myfridge.data.database.APICallInfo
import com.example.myfridge.data.recipes.RecipeItem
import com.example.myfridge.databinding.FragmentRecipesBinding
import com.example.myfridge.ui.database.DatabaseViewModel

import com.kennyc.view.MultiStateView


const val SPOONACULAR_APPID = BuildConfig.SPOONACULAR_API_KEY
class RecipesFragment : Fragment() {
    private val databaseViewModel: DatabaseViewModel.APICallInfoViewModel by viewModels()
    private val fridgedatabaseViewModel: DatabaseViewModel.FridgeItemInfoViewModel by viewModels()
    private var _binding: FragmentRecipesBinding? = null
    private var ingredients: String = "banana"
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var recipesRv: RecyclerView
    private lateinit var swipeContainer: SwipeRefreshLayout

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val recipesViewModel =
            ViewModelProvider(this)[RecipesViewModel::class.java]

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recipesRv = binding.rvRecipes
        recipesRv.layoutManager = LinearLayoutManager(container?.context)
        recipesAdapter = RecipesAdapter(::forwardDetailedRecipe)
        recipesRv.adapter = recipesAdapter

        recipesViewModel.status.observe(viewLifecycleOwner){status ->
            when(status){
                RecyclerStatus.LOADING -> {
                    binding.multiStateView.viewState = MultiStateView.ViewState.LOADING
                }
                RecyclerStatus.ERROR -> {
                    binding.multiStateView.viewState = MultiStateView.ViewState.ERROR
                }
                RecyclerStatus.CONTENT->{
                    binding.multiStateView.viewState = MultiStateView.ViewState.CONTENT
                }
            }
        }

        recipesViewModel.error.observe(viewLifecycleOwner){errorMessage ->
            if(errorMessage != null){
                requireActivity().findViewById<TextView>(R.id.errorTV).text = errorMessage
            }
        }
        recipesViewModel.recipes.observe(viewLifecycleOwner) {
            recipesAdapter.updateRecipesList(it)
        }

        fridgedatabaseViewModel.fridgeItemNameAll.observe(viewLifecycleOwner) {
            ingredients = it!!.joinToString(separator=",+")
        }

        swipeContainer = binding.swiperefresh
        swipeContainer.setOnRefreshListener {
            searchRecipe(recipesViewModel, ingredients)
            swipeContainer.setRefreshing(false)
        }


        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        onTransformationStartContainer()
//        val fragment = RecipesDetailedFragment()
//        val bundle = requireView().findViewById<TransformationLayout>(R.id.recipe_item_transformation_layout).getBundle("TransformationParams")
//        bundle.putParcelable(MainSingleDetailFragment.posterKey, poster)
//        fragment.arguments = bundle
    }
    fun searchRecipe(viewModel: RecipesViewModel, ingredients: String) {
        databaseViewModel.addAPICallInfo(APICallInfo(ingredients, System.currentTimeMillis()))
        viewModel.loadRecipeResults(SPOONACULAR_APPID, ingredients)
        Log.d("RecipeResult Parameters", "AppId: ${SPOONACULAR_APPID} , Ingredients: ${ingredients}")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun forwardDetailedRecipe(recipe: RecipeItem, position: Int, currentView: View){
//        val text = currentView.findViewById<TextView>(R.id.recipe_item_name)
//        ViewCompat.setTransitionName(text,"possible$position")
//        Log.d("setTransName", text.transitionName)
//        val extra = FragmentNavigatorExtras(text to "possible$position")
        val directions = RecipesFragmentDirections.actionNavRecipesToRecipesDetailedFragment(recipe, position)
        findNavController().navigate(directions)
        return
    }
}