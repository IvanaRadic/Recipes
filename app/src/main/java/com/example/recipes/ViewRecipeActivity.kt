package com.example.recipes

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.recipes.databinding.ActivityViewRecipeBinding


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
        val serving : TextView = binding.tvServingSize
        binding.txtInstruction.text
        var textview: TextView
        textview = binding.txtInstruction
        textview.setMovementMethod(ScrollingMovementMethod());

        binding.txtUser.text



        val bundle: Bundle? = intent.extras
        val Name = bundle!!.getString("Name")
        val Instruction = bundle!!.getString("Instruction")
        val Ingredients = bundle!!.getStringArrayList("Ingredients") as ArrayList<String>
        val User = bundle!!.getString("User")
        val FileName = bundle!!.getString("FileName")
        val Servings = bundle!!.get("Servings").toString()


        val adapter: ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1,Ingredients)


        ingredientList.adapter = adapter


        recipeName.text = Name
        recipeInstruction.text = Instruction
        //recipeIngredients.text = Ingredients.toString()
        recipeUser.text = User
        serving.text = Servings

    }



}