package com.example.expenserecordingapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class],version = 3, exportSchema = true)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object{
        @Volatile
        private var Instance: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context , ExpenseDatabase::class.java, "expense_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}