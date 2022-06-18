package com.example.recipes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recipes.databinding.ActivityRecipesBinding
import com.google.firebase.auth.FirebaseAuth

class RecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSignOff.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoSearchRecipe.setOnClickListener {
            val intent = Intent(this, SearchRecipesActivity::class.java)
            startActivity(intent)
        }


    }

}