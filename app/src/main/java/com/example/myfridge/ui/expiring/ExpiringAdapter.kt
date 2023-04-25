package com.example.myfridge.ui.expiring

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfridge.R
import com.example.myfridge.data.fridge.FridgeContent
import com.example.myfridge.data.database.FridgeItemInfo
import java.text.SimpleDateFormat
import java.util.*

class ExpiringAdapter : RecyclerView.Adapter<ExpiringAdapter.ViewHolder>(){
    var expiringList: List<FridgeItemInfo> = listOf()

    override fun getItemCount(): Int = expiringList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expiring_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.expiringList[position])
    }

    fun updateExpiringList(contents: FridgeContent?){
        expiringList = contents?.items ?: listOf()
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val expiringIMG: ImageView = view.findViewById(R.id.expiring_item_img)
        private val expiringName: TextView = view.findViewById(R.id.expiring_item_name)
        private val expiringExp: TextView = view.findViewById(R.id.expiring_item_expr)

        private lateinit var currentExpiringItemInfo: FridgeItemInfo

        fun bind(listItem: FridgeItemInfo){
            currentExpiringItemInfo = listItem

            val bytes: ByteArray = listItem.img!!
            val newBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, BitmapFactory.Options())
            expiringIMG.setImageBitmap(newBitmap)
            expiringName.text = listItem.name
            expiringExp.text = SimpleDateFormat("MM/dd/yyyy").format(Date(listItem.exp!!))
        }
    }
}