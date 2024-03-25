package com.example.expenserecordingapp.ui.history

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenserecordingapp.ExpensesTopAppBar
import com.example.expenserecordingapp.R
import com.example.expenserecordingapp.data.local.CategoryItems
import com.example.expenserecordingapp.data.local.Item
import com.example.expenserecordingapp.ui.AppViewModelProvider
import com.example.expenserecordingapp.ui.home.HomeDestination
import com.example.expenserecordingapp.ui.navigation.NavigationDestination
import com.example.expenserecordingapp.ui.theme.ExpenseRecordingAppTheme
import com.example.expenserecordingapp.ui.theme.secondary_expense_container_alternate
import java.time.LocalDateTime
import java.time.Year
import java.util.Calendar

object HistoryDestination : NavigationDestination {
    override val route = "history"
    override val titleRes = R.string.history_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val historyUiState by viewModel.historyUiState.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExpensesTopAppBar(
                title = stringResource(id = HistoryDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                drawerState = drawerState
            )
        }
    ) { innerPadding ->
        Log.d("TESTING HISTORY SCREEN","${historyUiState.itemList}")
        HistoryBody(
            itemList = historyUiState.itemList,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        )
    }
}

@Composable
fun HistoryBody(
    itemList: List<Item>,
    modifier: Modifier = Modifier,
) {
    Column(modifier){
        Text(text = "All Expenses This Month",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        Column {
            itemList.forEach{ item ->
                if (
                    item.month == Calendar.getInstance().get(Calendar.MONTH) &&
                    item.year == Calendar.getInstance().get(Calendar.YEAR)) {
                    ItemElement(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                        item = item
                    )
                }
            }
        }
    }
}

@Composable
fun ItemElement(
    modifier: Modifier = Modifier,
    item: Item,
){
    Row(modifier){
        Icon(
            painter = painterResource(id = CategoryItems[item.category].second ) ,
            contentDescription = CategoryItems[item.category].first)
        Column(Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))){
            Box(){
                Text(text = item.name, textAlign = TextAlign.Start,modifier = Modifier.fillMaxWidth())
                Text(text = "Rs. ${item.expense}", textAlign = TextAlign.End,modifier = Modifier.fillMaxWidth())
            }
            Text(text = "${item.date}.${item.month}.${item.year}")
        }
    }
}

@Preview
@Composable
fun Preview(){
    var month = Calendar.getInstance().get(Calendar.MONTH)
    var year = Calendar.getInstance().get(Calendar.YEAR)
    ExpenseRecordingAppTheme {
        HistoryBody(itemList = listOf(
            Item(id = 1, name = "XD", expense = 10.00, month = month, year = year),
            Item(id = 2, name = "X2", expense = 10.00, month = month, year = year),
            Item(id = 3, name = "XD4", expense = 10.00, month = month, year = year),
            Item(id = 4, name = "LMAO", expense = 10.00,month = month, year = year))
        )
    }
}
