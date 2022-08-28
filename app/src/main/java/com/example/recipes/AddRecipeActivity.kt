package com.example.recipes

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.adapter.IngredientAdapter
import com.example.recipes.databinding.ActivityAddRecipeBinding
import com.example.recipes.dataclass.Ingredient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*


class AddRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRecipeBinding
    private var recipeImageUri: Uri? = null

    private lateinit var addBtn: FloatingActionButton
    private lateinit var rvIngredients : RecyclerView
    private lateinit var ingredientList: ArrayList<Ingredient>
    private lateinit var ingredientAdapter: IngredientAdapter
    lateinit var unit : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ingredientList = ArrayList()
        addBtn = binding.addingBtn
        rvIngredients = binding.rvIngredients
        ingredientAdapter = IngredientAdapter(this, ingredientList)
        rvIngredients.layoutManager = LinearLayoutManager(this)
        rvIngredients.adapter = ingredientAdapter
        addBtn.setOnClickListener{addIngredient()}

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

        binding.bottomNav.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.btn_home -> {
                    val intent = Intent(this, RecipesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0);
                    true
                }
                R.id.btn_search -> {
                    val intent = Intent(this, SearchRecipesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0);
                    true
                }
                R.id.btn_add -> {
                    val intent = Intent(this, AddRecipeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0);
                    true
                }

                else ->
                    true
            }
        }
    }

    private fun addIngredient() {

        //tu se jos koristi findviewbyid
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item,null)
        val ingredientName = v.findViewById<EditText>(R.id.etIngredientName)
        val amount = v.findViewById<EditText>(R.id.etAmount)

        unit = v.findViewById(R.id.spUnit) as Spinner
        val pickUnit = resources.getStringArray(R.array.units)
        unit.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, pickUnit)
        unit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(this@AddRecipeActivity, "Please select unit", Toast.LENGTH_LONG).show()
            }
        }

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("Ok"){
            dialog,_->
            val ingredient = ingredientName.text.toString()
            val amount = amount.text.toString()
            var selectedUnit = unit.selectedItem.toString()

            //tu mi naopako ulazi u listu
            ingredientList.add(Ingredient(amount,selectedUnit,ingredient))
            ingredientAdapter.notifyDataSetChanged()
            Toast.makeText(this,"Added", Toast.LENGTH_SHORT).show()
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putParcelable("recipeImageUri", recipeImageUri)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    val now = Date()
    val fileName = formatter.format(now)

    private fun saveImg() {
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        recipeImageUri?.let {
            storageReference.putFile(it).addOnSuccessListener {
                binding.imgView.setImageURI(null)
                Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show()
            }
                .addOnFailureListener{
                    Toast.makeText(this, "Image not saved", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun saveRecipe(){
        val db = FirebaseFirestore.getInstance()
        val recipes: MutableMap<String, Any> = HashMap()

        recipes["Name"] = binding.etName.text.toString().trim()
        recipes["Instruction"] = binding.etInstruction.text.toString().trim()
        recipes["Servings"] = binding.numServings.text.toString().toInt()
        recipes["FileName"] = fileName
        recipes["User"] = Firebase.auth.currentUser?.email.toString()
        recipes["Ingredients"] = ingredientList



        db.collection("recipes").add(recipes).addOnCompleteListener {
            Toast.makeText(this, "You saved your data successfully!", Toast.LENGTH_LONG).show()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}