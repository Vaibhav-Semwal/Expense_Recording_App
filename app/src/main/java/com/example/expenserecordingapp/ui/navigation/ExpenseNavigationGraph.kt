    package com.example.expenserecordingapp.ui.navigation

import android.util.Log
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.expenserecordingapp.ui.history.HistoryDestination
import com.example.expenserecordingapp.ui.history.HistoryScreen
import com.example.expenserecordingapp.ui.home.HomeDestination
import com.example.expenserecordingapp.ui.home.HomeScreen
import com.example.expenserecordingapp.ui.item.ItemDetailDestination
import com.example.expenserecordingapp.ui.item.ItemDetailScreen
import com.example.expenserecordingapp.ui.item.ItemEditDestination
import com.example.expenserecordingapp.ui.item.ItemEditScreen

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

@Composable
fun InventoryNavHost(
    navController: NavHostController,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                drawerState = drawerState,
                navigateToItemEntry = { navController.navigate(ItemDetailDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${ItemEditDestination.route}/${it}")
                    Log.e("xyz","$it")
                }
            )
        }
        composable(route = ItemDetailDestination.route) {
            ItemDetailScreen(
                navigateBack = { navController.popBackStack() },
                drawerState = drawerState
            )
        }
        composable(route = HistoryDestination.route){
            HistoryScreen(drawerState = drawerState)
        }
        composable(
            route = ItemEditDestination.routeWithArgs,
            arguments = listOf( navArgument(ItemEditDestination.itemIdArg){
                type = NavType.IntType
            })
        ){
            Log.e("testing navigation","${NavType.IntType}")
            ItemEditScreen(
                navigateBack = { navController.popBackStack() },
                drawerState = drawerState
            )
        }
    }
}