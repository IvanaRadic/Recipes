package com.example.recipes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.dataclass.Ingredient


class IngredientAdapter(val c: Context, val ingredientList:ArrayList<Ingredient>):RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>()
{
    inner class IngredientViewHolder(val v: View): RecyclerView.ViewHolder(v){
        val amount = v.findViewById<TextView>(R.id.tv_amount)
        val unit = v.findViewById<TextView>(R.id.tv_unit)
        val ingredientName = v.findViewById<TextView>(R.id.tv_ingredient)
        val delete = v.findViewById<Button>(R.id.btnDelete)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.ingredients_list,parent,false)
        return IngredientViewHolder(v)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val newList = ingredientList[position]
        holder.amount.text = newList.amount.toString()
        holder.unit.text = newList.unit.toString()
        holder.ingredientName.text = newList.ingredientName
        holder.delete.setOnClickListener{
            ingredientList.removeAt(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}