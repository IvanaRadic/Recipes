package com.example.recipes.dataclass

import android.widget.Spinner

data class Ingredient(
    var amount: String?= null,
    var unit: String?= null,
    var ingredientName:String?= null
)
