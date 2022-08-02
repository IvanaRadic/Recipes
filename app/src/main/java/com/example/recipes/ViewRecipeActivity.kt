package com.example.recipes

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.get
import androidx.core.view.iterator
import com.example.recipes.databinding.ActivityLoginBinding
import com.example.recipes.databinding.ActivityViewRecipeBinding
import com.example.recipes.dataclass.Ingredient
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.reflect.Array.get

class ViewRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val recipeName : TextView = binding.tvRecipeName
        val recipeInstruction : TextView = binding.tvRecipeInstruction
        val recipeIngredients : TextView = binding.tvRecipeIngredients
        val recipeUser : TextView = binding.tvRecipeUser
        val ingredientList : ListView = binding.listOfIngredients


        val bundle: Bundle? = intent.extras
        val Name = bundle!!.getString("Name")
        val Instruction = bundle!!.getString("Instruction")
        val Ingredients = bundle!!.get("Ingredients")as ArrayList<String>
        val User = bundle!!.getString("User")

        //val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1,Ingredients)

        //ingredientList.adapter = adapter

        recipeName.text = Name
        recipeInstruction.text = Instruction
        recipeIngredients.text = Ingredients.toString()
        recipeUser.text = User



    }


}