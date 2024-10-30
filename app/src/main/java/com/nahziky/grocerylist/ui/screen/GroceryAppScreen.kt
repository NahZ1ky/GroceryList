package com.nahziky.grocerylist.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nahziky.grocerylist.R

enum class GroceryAppScreens(@StringRes val title: Int) {
    List(title = R.string.app_name),
    Add(title = R.string.add_product),
    Archive(title = R.string.archived_products)
}

@Composable
fun GroceryApp() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val size: Int = innerPadding.calculateTopPadding().value.toInt() // TODO: remove
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
fun BottomBar() {
    // TODO: add bottom bar

    // use shopping cart icon for archived products
}