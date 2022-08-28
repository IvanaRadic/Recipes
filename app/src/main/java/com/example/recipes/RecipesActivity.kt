package com.example.recipes

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.adapter.RecipeAdapter
import com.example.recipes.databinding.ActivityRecipesBinding
import com.example.recipes.dataclass.Ingredient
import com.example.recipes.dataclass.Recipe
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class RecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var userrecipeArrayList: ArrayList<Recipe>
    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var btnDel: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        btnDel = binding.btnDel
        btnDel.setOnClickListener{deleteRecipe()}

        //recyclerView = findViewById(R.id.rv_userRecipes)
        recyclerView = binding.rvUserRecipes
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userrecipeArrayList = arrayListOf<Recipe>()

        recipeAdapter = RecipeAdapter(userrecipeArrayList)


        recyclerView.adapter = recipeAdapter

        eventChangeListener()

        binding.txtWelcome.text = Firebase.auth.currentUser?.email.toString()
        binding.txtSignedInAs.text
        binding.txtMyRecipes.text

        binding.bottomNav.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                 R.id.btn_home -> {
                    val intent = Intent(this, RecipesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.btn_search -> {
                    val intent = Intent(this, SearchRecipesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.btn_add -> {
                    val intent = Intent(this, AddRecipeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    true
                }

                else ->
                    true
            }
    }
    }

    private fun eventChangeListener(){

        db = FirebaseFirestore.getInstance()
        db.collection("recipes").whereEqualTo("User", Firebase.auth.currentUser?.email.toString()).
        addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                if(error != null){

                    Log.e("Firestore Error", error.message.toString())
                    return

                }
                for(dc : DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        userrecipeArrayList.add(dc.document.toObject(Recipe::class.java))

                    }
                }
                recipeAdapter.notifyDataSetChanged()
            }
        })

        recipeAdapter.setOnItemClickListener(object : RecipeAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                // Toast.makeText(this@SearchRecipesActivity, "you clicked on ${position + 1}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RecipesActivity, ViewRecipeActivity::class.java)

                intent.putExtra("Name", userrecipeArrayList[position].Name)
                intent.putExtra("Instruction", userrecipeArrayList[position].Instruction)
                intent.putExtra("Ingredients", userrecipeArrayList[position].Ingredients)
                intent.putExtra("User", userrecipeArrayList[position].User)
                intent.putExtra("FileName", userrecipeArrayList[position].FileName)
                intent.putExtra("Servings", userrecipeArrayList[position].Servings)

                startActivity(intent)
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.signOff -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun deleteRecipe() {

        //tu se jos koristi findviewbyid
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.delete_popup,null)
        val recipeName = v.findViewById<EditText>(R.id.recipeNameDelete)


        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Delete"){
                dialog,_->
                val db = FirebaseFirestore.getInstance()
                val query = db.collection("recipes").whereEqualTo("Name", recipeName.text.toString()).get()
                            query.addOnCompleteListener{
                                for(document in it.result){
                                    db.collection("recipes").document(document.id).delete()
                                    val intent = Intent(this, RecipesActivity::class.java)
                                    startActivity(intent)
                                    overridePendingTransition(0, 0)
                              }
                            }

            Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()

        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"Canceled", Toast.LENGTH_SHORT).show()
        }
        addDialog.create()
        addDialog.show()

    }

}
