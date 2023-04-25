package com.example.myfridge.ui.home

import android.animation.Animator
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import androidx.cardview.widget.CardView
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.swipe.SimpleSwipeListener
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter
import com.example.myfridge.R
import com.example.myfridge.data.database.APICallInfo
import com.example.myfridge.data.database.FridgeItemInfo
import com.example.myfridge.data.fridge.FridgeContent
import com.example.myfridge.ui.database.DatabaseViewModel
import com.example.myfridge.data.recipes.RecipeItem
import jp.wasabeef.recyclerview.animators.holder.AnimateViewHolder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

class HomeAdapter(private val onShopClick: (String) -> Unit, private val onDeleteClick: (String) -> Unit): RecyclerSwipeAdapter<HomeAdapter.ViewHolder>() {
    var homeList: List<FridgeItemInfo> = listOf()
    override fun getItemCount(): Int = homeList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.home_item, parent, false)
        return ViewHolder(view, onShopClick, onDeleteClick)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
        holder.swipeLayout.isClickToClose = true
        holder.swipeLayout.addSwipeListener(object:SimpleSwipeListener(){

            override fun onOpen(layout: SwipeLayout) {
                layout.requestFocus()
                mItemManger.closeAllExcept(layout)
                layout.rootView.onFocusChangeListener =
                    View.OnFocusChangeListener { p0, p1 ->
                        if (p0?.id != layout.id){
                            mItemManger.closeAllItems()
                        }
                    }

            }
        })

        holder.bind(this.homeList[position], position)
    }
    fun updateHomeList(contents: FridgeContent?){
        homeList = contents?.items ?: listOf()
        notifyItemRangeChanged(0, homeList.size-1)
    }

    class ViewHolder(view: View, private val onShopClick: (String) -> Unit, private val onDeleteClick: (String) -> Unit): RecyclerView.ViewHolder(view), AnimateViewHolder{
        private val itemIMG: ImageView = view.findViewById(R.id.home_item_img)
        private val itemName: TextView = view.findViewById(R.id.home_item_name)
        private val itemExp: TextView = view.findViewById(R.id.home_item_expr)
        val swipeLayout = view.findViewById<SwipeLayout>(R.id.swipe)
        val shopButton = view.findViewById<Button>(R.id.to_list_button)
        val delButton = view.findViewById<Button>(R.id.delete_button)
        private var currentPosition: Int? = null
        private var currentView: View = view
        private lateinit var currentItemInfo: FridgeItemInfo

        init {
            shopButton.setOnClickListener{
                onShopClick(itemName.text.toString())
            }
            delButton.setOnClickListener {
                onDeleteClick(itemName.text.toString())
            }
        }
        override fun preAnimateRemoveImpl(holder: RecyclerView.ViewHolder) {
            // do something
        }

        override fun animateRemoveImpl(
            holder: RecyclerView.ViewHolder,
            listener: Animator.AnimatorListener
        ) {
            itemView.animate().apply {
                translationY(-itemView.height * 0.3f)
                alpha(0f)
                duration = 300
                setListener(listener)
            }.start()
        }

        override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
            itemView.setTranslationY(-itemView.height * 0.3f)
            itemView.setAlpha(0f)
        }

        override fun animateAddImpl(
            holder: RecyclerView.ViewHolder,
            listener: Animator.AnimatorListener
        ) {
            itemView.animate().apply {
                translationY(0f)
                alpha(1f)
                duration = 300
                setListener(listener)
            }.start()
        }

        fun bind(listItem: FridgeItemInfo, position:Int){
            currentItemInfo = listItem
            currentPosition = position
            val bytes: ByteArray = listItem.img!!
            val newBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, BitmapFactory.Options())
            itemIMG.setImageBitmap(newBitmap)
            itemName.text = listItem.name
            itemExp.text = SimpleDateFormat("MM/dd/yyyy").format(Date(listItem.exp!!))
        }

    }
    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe
    }
}