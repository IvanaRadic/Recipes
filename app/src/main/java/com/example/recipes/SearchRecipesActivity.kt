package com.example.recipes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ActivitySearchRecipesBinding
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.text.FieldPosition
import java.util.*
import kotlin.collections.ArrayList

class SearchRecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchRecipesBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeArrayList: ArrayList<Recipe>
    private lateinit var tempArrayList: ArrayList<Recipe>
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
        tempArrayList = arrayListOf<Recipe>()
        //tuh
        myAdapter = MyAdapter(tempArrayList)
        recyclerView.adapter = myAdapter

        eventChangeListener()

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, RecipesActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_item,menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(newText: String?): Boolean {

                    recyclerView.adapter!!.notifyDataSetChanged()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                tempArrayList.clear()

                val searchText = newText!!.lowercase(Locale.getDefault())
                if(searchText.isNotEmpty()){
                    recipeArrayList.forEach{
                        if(it.Name!!.lowercase(Locale.getDefault()).contains(searchText)){

                            tempArrayList.add(it)

                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                }
                else{
                    tempArrayList.clear()
                    tempArrayList.addAll(recipeArrayList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
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
                tempArrayList.addAll(recipeArrayList)
                myAdapter.notifyDataSetChanged()
            }
        })

        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                // Toast.makeText(this@SearchRecipesActivity, "you clicked on ${position + 1}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SearchRecipesActivity, ViewRecipeActivity::class.java)
                //tu 2 puta
                intent.putExtra("Name", tempArrayList[position].Name)
                intent.putExtra("Description", tempArrayList[position].Description)
                startActivity(intent)
            }
        })



    }
}