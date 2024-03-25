package com.example.expenserecordingapp.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.expenserecordingapp.data.local.Item
import com.example.expenserecordingapp.data.local.ItemsRepository
import java.text.NumberFormat
import java.util.Calendar

class ItemDetailsViewModel(
    private val itemsRepository: ItemsRepository
) : ViewModel() {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && (expense.isNotBlank() || expense.toIntOrNull() != 0)&& category.isNotBlank()
        }
    }
}

// UI state for ItemDetailsScreen
data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val category: String = "",
    val expense: String = "",
    val date: Int = Calendar.getInstance().get(Calendar.DATE),
    val month: Int = Calendar.getInstance().get(Calendar.MONTH),
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val additionalInfo: String = ""
)

fun ItemDetails.toItem(): Item = Item(
    id = id,
    category = category.toIntOrNull() ?: 1,
    name = name,
    date = date,
    month = month,
    year = year,
    expense = expense.toDoubleOrNull() ?: 0.0,
    additionalInfo = additionalInfo
)

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    date = date,
    month = month,
    year = year,
    category = category.toString(),
    expense = expense.toString(),
    additionalInfo = additionalInfo
)

fun Item.formatedExpense(): String {
    return NumberFormat.getCurrencyInstance().format(expense)
}

fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)
