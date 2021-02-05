package com.kandyba.gotogether.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R

class CategoriesAdapter(): RecyclerView.Adapter<CategoriesAdapter.CategoryHolder>() {

    private lateinit var categories: List<String>

    fun setCategories(categories: List<String>) {
        this.categories = categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val category = categories[position]
        holder.bindViews(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val category: TextView = itemView.findViewById(R.id.category)

        fun bindViews(item: String) {
            category.text = item
        }
    }

}