package com.example.expenserecordingapp.ui.item

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expenserecordingapp.ExpensesTopAppBar
import com.example.expenserecordingapp.R
import com.example.expenserecordingapp.data.local.CategoryItems
import com.example.expenserecordingapp.ui.AppViewModelProvider
import com.example.expenserecordingapp.ui.home.getPrimaryColor
import com.example.expenserecordingapp.ui.home.getSecondaryColor
import com.example.expenserecordingapp.ui.navigation.NavigationDestination
import com.example.expenserecordingapp.ui.theme.ExpenseRecordingAppTheme
import com.example.expenserecordingapp.ui.theme.primary_expense_container_alternate
import kotlinx.coroutines.launch

object ItemDetailDestination : NavigationDestination {
    override val route = "item_entry"
    override val titleRes = R.string.item_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    navigateBack: () -> Unit,
    drawerState: DrawerState,
    viewModel: ItemDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            ExpensesTopAppBar(
                title = stringResource(ItemDetailDestination.titleRes),
                canNavigateBack = false,
                drawerState = drawerState
            )
        }
    ) { innerPadding ->
        ItemDetailBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun ItemDetailBody(
    itemUiState: ItemUiState,
    onItemValueChange: (ItemDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(Modifier.padding(
        horizontal = dimensionResource(id = R.dimen.padding_small),
        vertical = dimensionResource(id = R.dimen.padding_extra_large)
    )) {
        Box (modifier = modifier
            .background(brush = Brush.linearGradient(colors =
                if (itemUiState.itemDetails.category.toIntOrNull() == 0) getPrimaryColor(isSystemInDarkTheme())
                else getSecondaryColor(isSystemInDarkTheme())
            )
        )){
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                ItemInputForm(
                    itemDetails = itemUiState.itemDetails,
                    onValueChange = onItemValueChange,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = onSaveClick,
                    colors = if(itemUiState.itemDetails.category.toIntOrNull() == 0)
                        ButtonDefaults.buttonColors(containerColor = primary_expense_container_alternate, contentColor = Color.White)
                    else
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = Color.White),
                    enabled = itemUiState.isEntryValid,
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    modifier: Modifier = Modifier,
    onValueChange: (ItemDetails) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var fieldColor = if (itemDetails.category.toIntOrNull() == 0)
            OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            )
            else
            OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            )

        var isExpanded by remember{ mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {isExpanded = it},
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = CategoryItems[itemDetails.category.toIntOrNull() ?: 1].first,
                onValueChange = { onValueChange(itemDetails.copy(category = it)) },
                readOnly = true,
                label = { Text("Category") },
                colors = fieldColor,
                leadingIcon = { Icon(painter = painterResource(id = CategoryItems[itemDetails.category.toIntOrNull() ?: 1].second ), contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) } ,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                enabled = enabled,
                singleLine = true
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                CategoryItems.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        leadingIcon = { Icon(
                            painter = painterResource(id = item.second),
                            contentDescription = item.first ) },
                        text = { Text(text = item.first) },
                        onClick = { onValueChange(itemDetails.copy(category = index.toString())) }
                    )
                }
            }
        }
        OutlinedTextField(
            value = itemDetails.name,
            onValueChange = { onValueChange(itemDetails.copy(name = it)) },
            label = { Text("Name") },
            colors = fieldColor,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = itemDetails.expense,
            onValueChange = { onValueChange(itemDetails.copy(expense = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Expense") },
            colors = fieldColor,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = itemDetails.additionalInfo,
            onValueChange = { onValueChange(itemDetails.copy(additionalInfo = it)) },
            label = { Text("Additional Info") },
            colors = fieldColor,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            enabled = enabled
        )
        if (enabled) {
            Text(
                text = "Required Field",
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun Preview(){
    ExpenseRecordingAppTheme {
        ItemDetailBody(
            itemUiState = ItemUiState(
                isEntryValid = false,
                itemDetails = ItemDetails(id = 1, category = "0", name = "test item", expense = "300")
            ),
            onItemValueChange = { } ,
            onSaveClick = { /*TODO*/ })
    }
}