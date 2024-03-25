package com.example.expenserecordingapp.data.local

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao : ItemDao) : ItemsRepository {
    override suspend fun getAllTodayItems(date: Int): Flow<List<Item>> = itemDao.getAllTodayItems(date)

    override suspend fun getAllItemsExceptToday(date: Int): Flow<List<Item>>  = itemDao.getAllItemsExceptToday(date)

    override fun getAllItemsStream(): Flow<List<Item>> = itemDao.getAllItems()

    override fun getItemStream(id: Int): Flow<Item?> = itemDao.getItem(id)

    override suspend fun insertItem(item: Item) = itemDao.insert(item)

    override suspend fun deleteItem(item: Item) = itemDao.delete(item)

    override suspend fun updateItem(item: Item) = itemDao.update(item)


}