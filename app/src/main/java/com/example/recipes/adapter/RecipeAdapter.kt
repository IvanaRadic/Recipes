package com.example.recipes.adapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.dataclass.Recipe
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RecipeAdapter(private val recipeList : ArrayList<Recipe>) : RecyclerView.Adapter<RecipeAdapter.MyViewHolder>() {


    private lateinit var mListener: onItemClickListener



    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_list, parent,false)
        return MyViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = recipeList[position]
        holder.recipeName.text = currentItem.Name
        //holder.recipeImage.setImageResource(images[position])

    }



    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val recipeName : TextView = itemView.findViewById(R.id.tv_recipe_name)
        val recipeImage : ImageView = itemView.findViewById(R.id.recipeImage)


        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)

            }
        }
    }




}