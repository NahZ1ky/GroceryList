package com.nahziky.grocerylist.ui.screen

import android.content.ClipData.Item
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nahziky.grocerylist.ui.theme.Typography
import com.nahziky.grocerylist.ui.viewModel.GroceryViewModel

@Composable
fun Category(
    itemList: List<Item>,
    modifier: Modifier = Modifier
) {
    listOf(itemList) { item ->

    }
}

@Composable
fun Item(
    itemName: String,
    modifier: Modifier = Modifier
) {
    val uiState by GroceryViewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = true,
                onCheckedChange = {
                    /* -----*----- */
                },
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = itemName,
                fontSize = 18.sp,
                style = Typography.bodyMedium // or is it?
            )
        }

    }
}


@Preview
@Composable
fun ItemPreview() {
    Item("Apples")
}