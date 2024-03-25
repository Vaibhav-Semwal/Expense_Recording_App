package com.example.expenserecordingapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.example.expenserecordingapp.R
import java.util.Calendar

@Entity(tableName = "items")
class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val category: Int = 1,
    @ColumnInfo val date: Int = Calendar.getInstance().get(Calendar.DATE),
    @ColumnInfo val month: Int = Calendar.getInstance().get(Calendar.MONTH),
    @ColumnInfo val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    @ColumnInfo val name: String,
    @ColumnInfo val expense: Double,
    @ColumnInfo val additionalInfo: String = ""
)

val CategoryItems = listOf(
    Pair("Expenses",R.drawable.baseline_attach_money),
    Pair("Grocery",R.drawable.baseline_store),
    Pair("Payment",R.drawable.baseline_payments)
)