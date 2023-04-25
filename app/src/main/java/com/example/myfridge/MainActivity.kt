package com.example.myfridge

import android.content.res.Configuration
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.mikepenz.iconics.IconicsDrawable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.get
import androidx.navigation.ui.*
import com.example.myfridge.databinding.ActivityMainBinding
import com.example.myfridge.ui.database.DatabaseViewModel
import com.google.android.material.navigation.NavigationView
import com.mikepenz.iconics.Iconics
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.materialdrawer.holder.StringHolder
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.fastadapter.ISubItem
import com.mikepenz.iconics.dsl.iconicsDrawable
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.wrapByIconics
import com.mikepenz.materialdrawer.holder.ImageHolder
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem
import com.mikepenz.materialdrawer.model.NavigationDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.model.interfaces.nameRes
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.model.interfaces.withName
import com.mikepenz.materialdrawer.util.*
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import com.quickersilver.themeengine.ThemeEngine
import com.quickersilver.themeengine.ThemeMode


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    lateinit var slider: MaterialDrawerSliderView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private val listViewModel: DatabaseViewModel.ShoppingListInfoViewModel by viewModels()
    private var count: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeEngine.applyToActivity(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMainActivity.toolbar)
        Iconics.init(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        navController =
            findNavController(R.id.nav_host_fragment_content_main_activity)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_recipes, R.id.nav_shopping, R.id.nav_expiring
            ), drawerLayout
        )
        slider = binding.slider
        setupActionBarWithNavController(navController, appBarConfiguration)
        slider.apply {
            setSelectionAtPosition(0, false)
            inflateMenu(R.menu.activity_main_drawer)
            (getDrawerItem(R.id.nav_home.toLong()) as PrimaryDrawerItem).apply {
                iconicsIcon = GoogleMaterial.Icon.gmd_home
            }
            getDrawerItem(R.id.nav_home.toLong())?.let { updateItem(it) }
            (getDrawerItem(R.id.nav_recipes.toLong()) as PrimaryDrawerItem).apply {
                iconicsIcon = GoogleMaterial.Icon.gmd_no_food
            }
            getDrawerItem(R.id.nav_recipes.toLong())?.let { updateItem(it) }
            (getDrawerItem(R.id.nav_expiring.toLong()) as PrimaryDrawerItem).apply {
                iconicsIcon = GoogleMaterial.Icon.gmd_timer
            }
            getDrawerItem(R.id.nav_expiring.toLong())?.let { updateItem(it) }
            (getDrawerItem(R.id.nav_settings.toLong()) as PrimaryDrawerItem).apply {
                iconicsIcon = GoogleMaterial.Icon.gmd_settings
            }
            getDrawerItem(R.id.nav_settings.toLong())?.let { updateItem(it) }
            addItems(
                ExpandableDrawerItem().apply {
                    nameText = "Shopping Lists"
                    level = 1
                    iconicsIcon = GoogleMaterial.Icon.gmd_shopping_cart
                    identifier = R.id.nav_shopping.toLong()
                    isSelectable = false

                }
            )
            listViewModel.shoppingListInfoAll.observe(this@MainActivity) {
                Log.d("LIST", it.toString())
                val itemList: MutableList<ISubItem<*>> = mutableListOf()
                if(it != null && it.isNotEmpty()) {
                    it.forEach { listinfo ->
                        itemList.add(SecondaryDrawerItem().apply {
                            nameText = listinfo.name
                            level = 1
                            tag = listinfo.name
                        })
                    }
                }

                itemList.add(
                    SecondaryDrawerItem().apply {
                        nameText = "Add New List"
                        level = 1
                        tag = "adder"
                    }
                )
                slider.getDrawerItem(R.id.nav_shopping.toLong())!!.subItems = itemList
                slider.expandableExtension.notifyAdapterDataSetChanged()

            }

            onDrawerItemClickListener = {_, drawerItem, _ ->
                    val check = onNavSupportedMenuItemSelected(drawerItem)
                    if (check){
                        this.drawerLayout!!.close()
                        true
                    }
                    else{
                        false
                    }

            }

            navController.addOnDestinationChangedListener { _, destination, _ ->
                selectExtension.deselect(selectExtension.selections.toMutableSet())
                setSelection(destination.id.toLong(), false)

            }
        }



    }





    private fun onNavSupportedMenuItemSelected(menuItem: IDrawerItem<*>): Boolean{
        try {
            if(menuItem.subItems.isNotEmpty() || menuItem.identifier.toInt() == R.id.nav_shopping) {
                Log.d("Main", menuItem.isExpanded.toString())
                Log.d("Main", menuItem.subItems.size.toString())
                return false
            }
            else if(menuItem.parent?.identifier?.toInt() == R.id.nav_shopping){
                if (menuItem.tag == "adder"){
                    navController.navigate(R.id.addShoppingListFragment)
                    return true
                }
                navController.navigate(MobileNavigationDirections.actionGlobalNavShopping(menuItem.tag.toString()))
                return true
            }
            if (navController.findDestination(menuItem.identifier.toInt()) == null) {
                return false
            }
            navController.navigate(menuItem.identifier.toInt())
            return true
        }
        catch (e: Exception){
            return false
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            findNavController(R.id.nav_host_fragment_content_main_activity)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}