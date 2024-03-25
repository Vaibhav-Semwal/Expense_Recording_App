package com.example.expenserecordingapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expenserecordingapp.ui.history.HistoryDestination
import com.example.expenserecordingapp.ui.home.HomeDestination
import com.example.expenserecordingapp.ui.navigation.InventoryNavHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ExpenseApp(
    navController: NavHostController = rememberNavController()
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        modifier = Modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(){
                Spacer(modifier = Modifier.padding(1.dp))
                NavDrawerItem(navigate = { navController.navigate(HomeDestination.route) }, text = "Home")
                NavDrawerItem(navigate = { navController.navigate(HistoryDestination.route)}, text = "History")
            }
        }
    ) {
        InventoryNavHost(navController = navController, drawerState = drawerState)
    }

}

@Composable
fun NavDrawerItem(
    navigate: () -> Unit,
    text: String
){
    TextButton(
        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        onClick = navigate
    ) {
        Text(text = text)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()
    CenterAlignedTopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.headlineLarge) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = stringResource(R.string.menu_button),
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                    )
                }
            }
        }
    )
}