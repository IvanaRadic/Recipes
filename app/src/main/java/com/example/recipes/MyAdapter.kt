package com.example.recipes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val recipeList : ArrayList<Recipe>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recipe_list, parent,false)
        return MyViewHolder(itemView, mListener)
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {

        val currentItem = recipeList[position]
        holder.recipeName.text = currentItem.Name
        holder.recipeDescription.text = currentItem.Description
        //holder.recipeImage.setImageResource(images[position])

    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val recipeName : TextView = itemView.findViewById(R.id.tv_recipe_name)
        val recipeDescription : TextView = itemView.findViewById(R.id.tv_recipe_description)
        val recipeImage : ImageView = itemView.findViewById(R.id.recipeImage)


        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)

            }
        }
    }



}