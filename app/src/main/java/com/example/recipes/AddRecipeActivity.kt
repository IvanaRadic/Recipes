package com.example.recipes

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.recipes.databinding.ActivityAddRecipeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding
    private var recipeImageUri: Uri? = null

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
            saveImg()
        }

        val selectImage =
            registerForActivityResult(
                ActivityResultContracts.GetContent()){uri: Uri? ->
                uri?.let {
                    recipeImageUri =  uri
                    binding.imgView.setImageURI(uri)
                }}

        binding.btnSelectImg.setOnClickListener {
            selectImage.launch("image/*")
        }

        recipeImageUri = savedInstanceState?.getParcelable("recipeImageUri")
        recipeImageUri?.let {
            binding.imgView.setImageURI(it)
        }

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putParcelable("profileImageUri", recipeImageUri)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val fileName = formatter.format(now)

    private fun saveImg() {
        //val progressDialog = ProgressDialog(this)
      //  progressDialog.setMessage("Uploading file...")
       // progressDialog.setCancelable(false)
      //  progressDialog.show()

        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        recipeImageUri?.let {
            storageReference.putFile(it).addOnSuccessListener {
                binding.imgView.setImageURI(null)
                Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show()
                //if (progressDialog.isShowing) progressDialog.dismiss()

            }
                .addOnFailureListener{
                   // if (progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(this, "Image not saved", Toast.LENGTH_LONG).show()
                }
        }
    }


    private fun saveRecipe(){
        val db = FirebaseFirestore.getInstance()
        val recipes: MutableMap<String, Any> = HashMap()

        recipes["Name"] = binding.etName.text.toString().trim()
        recipes["Description"] = binding.etDescription.text.toString().trim()
        recipes["Servings"] = binding.numServings.text.toString().toInt()
        recipes["FileName"] = fileName
        recipes["User"] = Firebase.auth.currentUser?.email.toString()

        db.collection("recipes").add(recipes).addOnCompleteListener {
            Toast.makeText(this, "You saved your data successfully!", Toast.LENGTH_LONG).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}