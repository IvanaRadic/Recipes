package com.example.recipes.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.recipes.R

class ListViewAdapter(
    ctx: Activity,
    ingredientList: ArrayList<HashMap<String, String>>
) : BaseAdapter() {
    private var context = ctx
    private var ingredients = ingredientList

    private lateinit var ingredientName: TextView
    private lateinit var amount: TextView
    private lateinit var unit: TextView

    override fun getCount(): Int {
        return ingredients.size
    }

    override fun getItem(position: Int): Any {
        return ingredients[position]
    }

    override fun getItemId(position: Int): Long {
        return 0;
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val ingredient = ingredients[position]
        view = LayoutInflater.from(context).inflate(R.layout.listview_row_item, parent, false)
        ingredientName = view?.findViewById(R.id.ingredientName) as TextView
        amount = view.findViewById(R.id.amount)
        unit = view.findViewById(R.id.unit)
        ingredientName.text = ingredient["ingredientName"]
        amount.text = ingredient["amount"]
        unit.text = ingredient["unit"]
        return view
    }


}