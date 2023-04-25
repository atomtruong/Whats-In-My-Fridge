package com.example.myfridge.data.database

import androidx.room.*
import com.example.myfridge.data.recipes.RecipeDetailedItem
import kotlinx.coroutines.flow.Flow


class DatabaseDAO{
    @Dao
    interface APICallInfoDAO{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(callInfo: APICallInfo)

        @Delete
        suspend fun delete(city: APICallInfo)

        @Update
        suspend fun update(city: APICallInfo)

        @Query("SELECT * FROM APICallInfo WHERE ingredients = :ingredients LIMIT 1")
        fun query(ingredients:String): Flow<APICallInfo?>

        @Query("SELECT * FROM APICallInfo ORDER BY timestamp DESC")
        fun queryAll(): Flow<List<APICallInfo>?>
    }

    @Dao
    interface FridgeItemInfoDAO{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(fridgeInfo: FridgeItemInfo)

        @Delete
        suspend fun delete(fridgeInfo: FridgeItemInfo)

        @Update
        suspend fun update(fridgeInfo: FridgeItemInfo)

        @Query("SELECT * FROM FridgeItemInfo WHERE name = :name LIMIT 1")
        fun query(name:String): Flow<FridgeItemInfo?>

        @Query("SELECT * FROM FridgeItemInfo")
        fun queryAll(): Flow<List<FridgeItemInfo>?>

        @Query("SELECT * FROM FridgeItemInfo WHERE exp < :soonDate")
        fun expiringSoon(soonDate:Long): Flow<List<FridgeItemInfo>?>

        @Query("SELECT name FROM FridgeItemInfo")
        fun queryAllNames(): Flow<List<String>?>
    }

    @Dao
    interface ShoppingListItemDAO{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(shoppingListItemInfo : ShoppingListItemInfo)

        @Delete
        suspend fun delete(shoppingListItemInfo: ShoppingListItemInfo)

        @Query("SELECT * FROM ShoppingListItemInfo")
        fun queryAll() : Flow<List<ShoppingListItemInfo>?>

        @Query("SELECT * FROM ShoppingListItemInfo WHERE listName = :listName")
        fun getItemsForList(listName: String): Flow<List<ShoppingListItemInfo>?>
    }

    @Dao
    interface ShoppingListDAO{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(shoppingListInfo: ShoppingListInfo)

        @Query("SELECT * FROM ShoppingListInfo")
        fun queryAll() : Flow<List<ShoppingListInfo>?>

        @Query("Select name FROM ShoppingListInfo")
        fun queryAllNames() : Flow<List<String>?>
    }

}