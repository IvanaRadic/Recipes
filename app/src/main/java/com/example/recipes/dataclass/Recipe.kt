package com.example.recipes.dataclass

data class Recipe(var Name: String ?= null,
                  var Instruction: String ?= null,
                  var Ingredients: ArrayList<*> ?= null,
                  var FileName: String ?= null,
                  var User: String ?= null)
