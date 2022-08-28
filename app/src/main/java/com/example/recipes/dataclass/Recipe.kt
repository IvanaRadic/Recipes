package com.example.recipes.dataclass

import android.widget.ImageView

data class Recipe(var Name: String ?= null,
                  var Instruction: String ?= null,
                  var Ingredients: ArrayList<*> ?= null,
                  var FileName: String ?= null,
                  var User: String ?= null,
                  var Image: ImageView?= null,
                  var Servings: Long ?= null)
