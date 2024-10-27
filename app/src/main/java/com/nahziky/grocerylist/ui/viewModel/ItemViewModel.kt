package com.nahziky.grocerylist.ui.viewModel

import com.nahziky.grocerylist.ui.state.ItemUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ItemViewModel {

    private val _state: MutableStateFlow(ItemUiState())

    val  uiState: StateFlow<ItemUiState> = _state.asStateFlow()

    fun updateItemName() {

    }
}