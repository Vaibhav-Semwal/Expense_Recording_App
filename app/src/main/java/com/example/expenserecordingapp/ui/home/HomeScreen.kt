package com.example.expenserecordingapp.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.textInputServiceFactory
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenserecordingapp.ExpensesTopAppBar
import com.example.expenserecordingapp.R
import com.example.expenserecordingapp.data.local.CategoryItems
import com.example.expenserecordingapp.data.local.Item
import com.example.expenserecordingapp.ui.AppViewModelProvider
import com.example.expenserecordingapp.ui.navigation.NavigationDestination
import com.example.expenserecordingapp.ui.theme.ExpenseRecordingAppTheme
import com.example.expenserecordingapp.ui.theme.secondary_expense_container_alternate
import java.time.MonthDay

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

val testItem =
    listOf(
        Item(id = 1, category = 1, name = "test expense", expense = 20.0),
        Item(id = 4, category = 2, name = "test expense", expense = 50.0),
        Item(id = 2, category = 3, name = "test expense", expense = 10.0),
        Item(id = 6, category = 2, name = "test expense", expense = 20.0),
        Item(id = 7, category = 2, name = "test expense", expense = 50.0),
        Item(id = 9, category = 1, name = "test expense", expense = 20.0),
        Item(id = 3, category = 2, name = "test expense", expense = 50.0),
        Item(id = 0, category = 3, name = "test expense", expense = 20.0),
        Item(id = 8, category = 3, name = "test expense", expense = 50.0),
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeUiState by viewModel.homeUiState.collectAsState()
    val currentBudget by viewModel.currentBudget.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ExpensesTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                drawerState = drawerState
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                containerColor = secondary_expense_container_alternate,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            budget = currentBudget,
            onItemClick = navigateToItemUpdate,
            viewModel = viewModel,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
fun HomeBody(
    modifier: Modifier = Modifier,
    itemList: List<Item>,
    onItemClick: (Int) -> Unit,
    budget: Double,
    viewModel: HomeScreenViewModel = viewModel(),
){
    Column(modifier = modifier){
        ExpenseBudget(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)), budget = budget, viewModel = viewModel)
        when(itemList.isEmpty()){
            true -> Text(text = "there are no Items over here :(", modifier.fillMaxSize())
            false -> ExpensesList(itemList = itemList, onItemClick = { onItemClick(it.id) })
        }
    }
}

@Composable
fun ExpenseBudget(
    modifier: Modifier,
    budget: Double,
    viewModel: HomeScreenViewModel
){
    var modifyBudget by remember{ mutableStateOf(false) }

    Row(modifier = modifier){
        Text(
            text = "Budget: ",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .align(Alignment.CenterVertically)
                .padding(end = dimensionResource(id = R.dimen.padding_small))
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Rs. $budget",
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(end = dimensionResource(id = R.dimen.padding_small))
            )
            IconButton(
                onClick = { modifyBudget = true}) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
            }
            if (modifyBudget){
                Dialog(onDismissRequest = { modifyBudget = false }) {
                    DialogBox(
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun DialogBox(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel
){
    var currentBudget by remember { mutableStateOf("") }
    val context = LocalContext.current

    Card(modifier) {
        Column {
            Text(
                text = "Enter New Budget",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
            OutlinedTextField(
                value = currentBudget,
                onValueChange = { currentBudget = it} ,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .height(50.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )
            Button(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .fillMaxWidth(),
                onClick = { viewModel.setBudget(currentBudget.toDouble()) ;
                    Toast.makeText(context,"saved new budget",Toast.LENGTH_SHORT).show()}
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
private fun ExpensesList(
    itemList: List<Item>, onItemClick: (Item) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items = itemList, key = { it.id }) { item ->
            HomeScreenCards(
                item = item,
                image = try {
                    CategoryItems[item.category].second
                }catch (e: Exception){
                    CategoryItems[0].second
                },
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(item) }
            )
        }
    }
}

@Composable
fun HomeScreenCards(
    item: Item,
    modifier: Modifier = Modifier,
    image: Int,
){
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = if (item.category == 0) getPrimaryColor(isSystemInDarkTheme())
                    else getSecondaryColor(isSystemInDarkTheme())
                )
            ),
        ){
            ImageIcon(
                item = item,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                image = painterResource(id = image)
            )
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    )
                Text(
                    text = if (item.category == 0) "${item.expense} Rs Lent" else "${item.expense} Rs Paid",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Composable
fun ImageIcon(
    item: Item,
    image: Painter,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ){
        Image(
            painter = image,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.size(30.dp),
            contentDescription = null
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Text(text = item.date.toString() + " " + MonthDay.now().month.name.subSequence(0,3))
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
fun Preview(){
    ExpenseRecordingAppTheme {
        HomeBody(itemList = testItem, onItemClick = { }, budget = 10.0)
    }
}