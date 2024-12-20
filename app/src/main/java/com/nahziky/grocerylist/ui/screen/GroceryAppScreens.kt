package com.nahziky.grocerylist.ui.screen

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nahziky.grocerylist.R
import com.nahziky.grocerylist.ui.GeneralViewModel
import com.nahziky.grocerylist.ui.SettingsPreferencesViewModel

enum class GroceryAppScreens(@StringRes val title: Int) {
    ListScreen(title = R.string.grocery_list),
    AddScreen(title = R.string.add_product),
    ArchiveScreen(title = R.string.archived_list);
}

@Composable
fun GroceryApp(
    generalViewModel: GeneralViewModel = viewModel(factory = GeneralViewModel.factory),
    settingsViewModel: SettingsPreferencesViewModel
    = viewModel(
        factory = SettingsPreferencesViewModel
            .Factory
    ),
    navController: NavHostController = rememberNavController(),
) {
    val settingsUiState = settingsViewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = GroceryAppScreens.valueOf(
        backStackEntry?.destination?.route ?: GroceryAppScreens.ListScreen.name
    )
    val isTitleCentered = settingsUiState.value.isTitleCentered

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isTitleCentered) {
                Log.d("mainActivity - setting", "title is centered")
                SidedTopBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    settingViewModel = viewModel(factory = SettingsPreferencesViewModel.Factory),
                )
            } else {
                Log.d("mainActivity - setting", "title is not centered")
                CenteredTopBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    settingViewModel = viewModel<SettingsPreferencesViewModel>(factory = SettingsPreferencesViewModel.Factory),
                )
            }
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
                shape = shapes.large,
                modifier = Modifier
                    .padding(16.dp)
                    .size(32.dp),
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
            composable(route = GroceryAppScreens.ListScreen.name) {
                ListScreen(generalViewModel)
            }
            composable(route = GroceryAppScreens.ArchiveScreen.name) {
                ArchiveScreen()
            }
            composable(route = GroceryAppScreens.AddScreen.name) {
                AddScreen(
                    generalViewModel = viewModel(factory = GeneralViewModel.factory),
                    //categoryListViewModel = generalViewModel
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SidedTopBar(
    currentScreen: GroceryAppScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    settingViewModel: SettingsPreferencesViewModel
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
                        contentDescription = "Go Back"
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    Log.d("settings", "$settingViewModel.uiState.value.isTitleCentered")
                    settingViewModel.toggleCenteredTitle(!settingViewModel.uiState.value.isTitleCentered)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowUp,
                    contentDescription = "change title to center align"
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredTopBar(
    currentScreen: GroceryAppScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    settingViewModel: SettingsPreferencesViewModel
) {
    CenterAlignedTopAppBar(
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
                        contentDescription = "Go Back"
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    Log.d("settings", "$settingViewModel.uiState.value.isTitleCentered")
                    settingViewModel.toggleCenteredTitle(!settingViewModel.uiState.value.isTitleCentered)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "toggle centered title"
                )
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

@Preview
@Composable
fun GroceryAppPreview() {
    GroceryApp()
}