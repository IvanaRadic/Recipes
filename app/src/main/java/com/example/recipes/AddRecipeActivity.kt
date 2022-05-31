package com.example.recipes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipes.databinding.ActivityAddRecipeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSignOff.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoRecipes.setOnClickListener {
            val intent = Intent(this, SearchRecipesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSaveRecipe.setOnClickListener{
            saveRecipe()
        }
    }
    private fun saveRecipe(){

        val db = FirebaseFirestore.getInstance()
        val recipes: MutableMap<String, Any> = HashMap()

        recipes["Name"] = binding.etName.text.toString().trim()
        recipes["Description"] = binding.etDescription.text.toString().trim()
        recipes["Servings"] = binding.numServings.text.toString().toInt()

        db.collection("recipes").add(recipes).addOnCompleteListener {
            Toast.makeText(this, "You saved your data successfully!", Toast.LENGTH_LONG).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }


    }
}