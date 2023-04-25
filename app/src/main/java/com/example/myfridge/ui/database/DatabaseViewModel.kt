package com.example.myfridge.ui.database

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfridge.data.database.*
import kotlinx.coroutines.launch



class DatabaseViewModel {

    class APICallInfoViewModel(application: Application): AndroidViewModel(application){
        private val repository = DatabaseRepository.APICallInfoRepository(
            AppDatabase.getInstance(application).apiCallInfoDao()
        )
        val apiCallInfoHistory: LiveData<List<APICallInfo>?> = repository.getAPICallHistory.asLiveData()

        fun addAPICallInfo(city: APICallInfo){
            viewModelScope.launch{
                repository.insertAPICallInfo(city)
            }
        }

        fun deleteAPICallInfo(city: APICallInfo){
            viewModelScope.launch{
                repository.deleteAPICallInfo(city)
            }
        }

        fun updateAPICallInfo(city: APICallInfo){
            viewModelScope.launch{
                repository.updateAPICallInfo(city)
            }
        }

        fun getAPICallInfo(callInfo: String) = repository.getAPICallInfo(callInfo).asLiveData()
    }

    class FridgeItemInfoViewModel(application: Application): AndroidViewModel(application){
        private val repository = DatabaseRepository.FridgeItemInfoRepository(
            AppDatabase.getInstance(application).fridgeItemInfoDao()
        )
        val fridgeItemInfoAll: LiveData<List<FridgeItemInfo>?> = repository.getAllFridgeItems.asLiveData()
        val fridgeItemNameAll: LiveData<List<String>?> = repository.getAllFridgeItemNames.asLiveData()

        fun addFridgeItemInfo(fridgeItem: FridgeItemInfo){
            viewModelScope.launch{
                repository.insertFridgeItemInfo(fridgeItem)
            }
        }

        fun deleteFridgeItemInfo(fridgeItem: FridgeItemInfo){
            viewModelScope.launch{
                repository.deleteFridgeItemInfo(fridgeItem)
            }
        }

        fun updateFridgeItemInfo(fridgeItem: FridgeItemInfo){
            viewModelScope.launch{
                repository.updateFridgeItemInfo(fridgeItem)
            }
        }

        fun getExpiringSoon(soonDate:Long) = repository.getExpiringSoon(soonDate).asLiveData()
        fun getFridgeItemInfo(fridgeInfo: String) = repository.getFridgeItemInfo(fridgeInfo).asLiveData()
    }

    class ShoppingListItemInfoViewModel(application: Application): AndroidViewModel(application){
        private val repository = DatabaseRepository.ShoppingListItemInfoRepository(
            AppDatabase.getInstance(application).shoppingListItemInfoDao()
        )
        val shoppingListItemInfoAll: LiveData<List<ShoppingListItemInfo>?> = repository.getAllShoppingListItems.asLiveData()

        fun addShoppingListItemInfo(shoppingListItemInfo: ShoppingListItemInfo){
            viewModelScope.launch{
                repository.insertShoppingListItem(shoppingListItemInfo)
            }
        }

        fun deleteShoppingListItemInfo(shoppingListItemInfo: ShoppingListItemInfo){
            viewModelScope.launch{
                repository.deleteShoppingListItem(shoppingListItemInfo)
            }
        }

        fun getItemsForList(listName: String) = repository.getItemsForList(listName).asLiveData()
    }

    class ShoppingListInfoViewModel(application: Application) : AndroidViewModel(application){
        private val repository = DatabaseRepository.ShoppingListInfoRepository(
            AppDatabase.getInstance(application).shoppingListInfoDao()
        )
        val shoppingListInfoAll : LiveData<List<ShoppingListInfo>?> = repository.getAllShoppingLists.asLiveData()
        val shoppingListInfoNamesAll : LiveData<List<String>?> = repository.getAllShoppingListNames.asLiveData()

        fun addShoppingListInfo(shoppingListInfo: ShoppingListInfo){
            viewModelScope.launch{
                repository.insertShoppingList(shoppingListInfo)
            }
        }
    }

}