package com.example.expenserecordingapp.ui.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenserecordingapp.ExpensesTopAppBar
import com.example.expenserecordingapp.R
import com.example.expenserecordingapp.ui.AppViewModelProvider
import com.example.expenserecordingapp.ui.navigation.NavigationDestination
import com.example.expenserecordingapp.ui.theme.ExpenseRecordingAppTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.launch

object ItemEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = R.string.edit_item_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    viewModel: ItemEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ExpensesTopAppBar(
                title = stringResource(ItemEditDestination.titleRes),
                canNavigateBack = true,
                drawerState = drawerState
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column {
            Text(text = "created on ${viewModel.itemUiState.itemDetails.year}.${viewModel.itemUiState.itemDetails.month}.${viewModel.itemUiState.itemDetails.date}")
            ItemDetailBody(
                itemUiState = viewModel.itemUiState,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = { coroutineScope.launch{
                    viewModel.updateItem()
                    navigateBack()
                }},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemEditScreenPreview() {
    ExpenseRecordingAppTheme {
        ItemEditScreen(navigateBack = { /*Do nothing*/ },
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        )
    }
}