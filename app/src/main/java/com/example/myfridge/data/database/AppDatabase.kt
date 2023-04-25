package com.example.myfridge.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities=[APICallInfo::class, FridgeItemInfo::class, ShoppingListItemInfo::class, ShoppingListInfo::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
//    abstract fun shoppingListDao(): DatabaseDAO.ShoppingListDAO

    abstract fun apiCallInfoDao(): DatabaseDAO.APICallInfoDAO

    abstract fun fridgeItemInfoDao(): DatabaseDAO.FridgeItemInfoDAO

    abstract fun shoppingListItemInfoDao(): DatabaseDAO.ShoppingListItemDAO

    abstract fun shoppingListInfoDao(): DatabaseDAO.ShoppingListDAO

    companion object{
        @Volatile private var instance: AppDatabase? = null

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "MyFridge.db").build()

        fun getInstance(context: Context): AppDatabase{
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context).also{
                    instance = it
                }
            }
        }
    }
}