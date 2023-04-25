package com.example.myfridge.fridgewidget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.preference.PreferenceManager
import com.example.myfridge.R
import com.example.myfridge.data.database.AppDatabase
import com.example.myfridge.data.database.DatabaseDAO
import com.example.myfridge.data.database.DatabaseRepository
import com.example.myfridge.data.database.FridgeItemInfo
import kotlinx.coroutines.flow.toSet
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class WidgetAdapter:BaseAdapter() {
    private var itemList : MutableList<FridgeItemInfo> = mutableListOf()

    override fun getCount(): Int = itemList.size

    override fun getItem(p0: Int): FridgeItemInfo = itemList[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
        var newView: TextView
        if (convertView == null) {
            val temp: View = LayoutInflater.from(container!!.context).inflate(R.layout.fridge_widget, container, false);
            newView = temp.findViewById(R.id.widget_listView)
        }
        else{
            newView = convertView.findViewById(R.id.widget_listView)
        }
        val currentItem: FridgeItemInfo = getItem(position)
        newView.text = currentItem.name
        return newView;
    }
}

class ListService : RemoteViewsService(){
    lateinit var context: Context
    lateinit var intent: Intent

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ListRemoteViewsFactory(this.applicationContext, intent)
    }
}

class ListRemoteViewsFactory(
    private val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

// See the RemoteViewsFactory API reference for the full list of methods to
// implement.
    private var databaseRepository: DatabaseRepository.FridgeItemInfoRepository
    private lateinit var allFridgeItems: LiveData<List<FridgeItemInfo>?>
    private var fridgeItemDAO = AppDatabase.getInstance(context)
    private var itemList : MutableList<FridgeItemInfo> = mutableListOf()

    init {
        databaseRepository = DatabaseRepository.FridgeItemInfoRepository(fridgeItemDAO.fridgeItemInfoDao())
    }
    override fun onCreate() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefDays = prefs.getString(context.getString(R.string.pref_expiration_key), "7")
        val expDate = LocalDateTime.now().plusDays(Integer.parseInt(prefDays!!).toLong()).toInstant(ZoneOffset.UTC).toEpochMilli()
        allFridgeItems = databaseRepository.getExpiringSoon(expDate).asLiveData()
        allFridgeItems.observeForever{
            itemList.clear()
            itemList.addAll(it.orEmpty())
            onDataSetChanged()
        }
    }

    override fun onDataSetChanged() {
        allFridgeItems = databaseRepository.getAllFridgeItems.asLiveData()
        if (allFridgeItems.value.orEmpty().isNotEmpty()){
            getViewAt(0)
        }
    }

    override fun onDestroy() {
        return
    }

    override fun getCount(): Int = itemList.size

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            setTextViewText(R.id.widget_item_name, itemList[position].name)

            setTextViewText(R.id.widget_item_expr, SimpleDateFormat("MM/dd/yyyy").format(Date(itemList[position].exp!!)))
            val fillInIntent = Intent().apply {
                Bundle().also { extras ->
                    extras.putInt(EXTRA_ITEM, position)
                    putExtras(extras)
                }
            }
            // Make it possible to distinguish the individual on-click
            // action of a given item.
            Log.d("getViewAt", "Was Executed")
            setOnClickFillInIntent(R.id.widget_item_name, fillInIntent)
        }
    }

    override fun getLoadingView(): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_item)
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}