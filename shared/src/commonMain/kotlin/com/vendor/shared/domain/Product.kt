package com.vendor.shared.domain

import androidx.compose.ui.graphics.Color
import com.vendor.shared.CategoryBlue
import com.vendor.shared.CategoryGreen
import com.vendor.shared.CategoryPurple
import com.vendor.shared.CategoryRed
import com.vendor.shared.CategoryYellow
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail : String,
    val category: String,
    val flavours : List<String>? = null,
    val weight : Int? = null,
    val price: Double,
    val isPopular : Boolean = false,
    val isDiscounted : Boolean = false,
    val isNew : Boolean = false
)

enum class ProductCategory(
    val title : String,
    val color : Color
){
    Protein(
        title = "Protein",
        color = CategoryYellow
    ),
    Creatine(
        title = "Creatine",
        color = CategoryBlue
    ),
    PreWorkout(
        title = "Pre-Workout",
        color = CategoryGreen
    ),
    Gainers(
        title = "Gainers",
        color = CategoryPurple
    ),
    Accessories(
        title = "Accessories",
        color = CategoryRed
    )
}
