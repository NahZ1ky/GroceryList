package com.nahziky.grocerylist.ui.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "product_table")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val productId: Int = 0,
    @SerialName("name") val productName: String = "",
    val productIsChecked: Boolean = false,
    val calories: String = "",
)

