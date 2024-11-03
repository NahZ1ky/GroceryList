package com.nahziky.grocerylist.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nahziky.grocerylist.ui.CategoryListViewModel

@Composable
fun ArchiveScreen(
    viewModel: CategoryListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

}