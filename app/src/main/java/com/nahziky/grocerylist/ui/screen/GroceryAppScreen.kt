package com.nahziky.grocerylist.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nahziky.grocerylist.R
import com.nahziky.grocerylist.ui.CategoryListViewModel

enum class GroceryAppScreens(@StringRes val title: Int) {
    ListScreen(title = R.string.grocery_list),
    AddScreen(title = R.string.add_product),
    ArchiveScreen(title = R.string.archived_list);
}

@Composable
fun GroceryApp(
    viewModel: CategoryListViewModel = CategoryListViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GroceryAppScreens.valueOf(
        backStackEntry?.destination?.route ?: GroceryAppScreens.ListScreen.name
    )
    val uiState by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(GroceryAppScreens.AddScreen.name) },
                containerColor = MaterialTheme.colorScheme.tertiary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.padding(16.dp),
                interactionSource = remember { MutableInteractionSource() },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = R.string.add_product.toString()
                    )
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GroceryAppScreens.ListScreen.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = GroceryAppScreens.ListScreen.name) { ListScreen(viewModel) }
            composable(route = GroceryAppScreens.ArchiveScreen.name) { ArchiveScreen(viewModel) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentScreen: GroceryAppScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "go back"
                    )
                }
            }
        }
    )
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    BottomNavigation(
        windowInsets = BottomNavigationDefaults.windowInsets,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.List, contentDescription = null) },
            label = { Text(stringResource(R.string.grocery_list)) },
            selected = false,
            onClick = { navController.navigate(GroceryAppScreens.ListScreen.name) },
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
            label = { Text(stringResource(R.string.archived_list)) },
            selected = false,
            onClick = { navController.navigate(GroceryAppScreens.ArchiveScreen.name) }
        )
    }
}

@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.shape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = content
    )
}