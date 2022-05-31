package com.example.recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ViewRecipeActivity : AppCompatActivity() {

    private lateinit var tvRecipeName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)

        val recipeName : TextView = findViewById(R.id.tvRecipeName)
        val recipeDescription : TextView = findViewById(R.id.tvRecipeDescription)

        val bundle: Bundle? = intent.extras
        val Name = bundle!!.getString("Name")
        val Description = bundle!!.getString("Description")

        recipeName.text = Name
        recipeDescription.text = Description
    }


}