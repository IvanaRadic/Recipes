package com.example.recipes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ActivitySearchRecipesBinding
import com.google.firebase.firestore.*
import java.text.FieldPosition

class SearchRecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchRecipesBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>
    private lateinit var myAdapter: MyAdapter
    private lateinit var db: FirebaseFirestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchRecipesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        recyclerView = findViewById(R.id.rv_recipes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        recipeArrayList = arrayListOf<Recipe>()
        myAdapter = MyAdapter(recipeArrayList)
        recyclerView.adapter = myAdapter

        eventChangeListener()

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, RecipesActivity::class.java)
            startActivity(intent)
        }

        binding.btnSearchRecipe.setOnClickListener {
            searchRecipe()
        }
    }



    private fun searchRecipe() {
        TODO("Not yet implemented")
    }

    private fun eventChangeListener(){

        db = FirebaseFirestore.getInstance()
        db.collection("recipes").
        addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                if(error != null){

                    Log.e("Firestore Error", error.message.toString())
                    return

                }
                for(dc : DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        recipeArrayList.add(dc.document.toObject(Recipe::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        })

        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                // Toast.makeText(this@SearchRecipesActivity, "you clicked on ${position + 1}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SearchRecipesActivity, ViewRecipeActivity::class.java)
                intent.putExtra("Name", recipeArrayList[position].Name)
                intent.putExtra("Description", recipeArrayList[position].Description)
                startActivity(intent)
            }
        })

    }
}