package com.example.expenserecordingapp.data.local

import kotlinx.coroutines.flow.Flow

interface ItemsRepository {

    // fetch all items with current days date listed on them
    suspend fun getAllTodayItems(date: Int): Flow<List<Item>>

    // fetch all items with current days date listed on them
    suspend fun getAllItemsExceptToday(date: Int): Flow<List<Item>>

    // fetch all items
    fun getAllItemsStream(): Flow<List<Item>>

    // fetch all details of an item with matching [id]
    fun getItemStream(id: Int): Flow<Item?>

    // add item in datasource
    suspend fun insertItem(item: Item)

    // delete item in datasource
    suspend fun deleteItem(item: Item)

    // update item in datasource
    suspend fun updateItem(item: Item)
}