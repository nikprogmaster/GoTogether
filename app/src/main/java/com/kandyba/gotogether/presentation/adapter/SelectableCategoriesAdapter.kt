package com.kandyba.gotogether.presentation.adapter

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.kandyba.gotogether.R
import com.kandyba.gotogether.models.presentation.SelectableInterest

/**
 * Адаптер выбираемых категорий
 *
 * @constructor
 * @property categories список категорий
 */
class SelectableCategoriesAdapter(
    val categories: List<SelectableInterest>
) : RecyclerView.Adapter<SelectableCategoriesAdapter.CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.selectable_category_item, parent, false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        val category = categories[position]
        holder.bindViews(category)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun getSelectedInterests(): List<String> {
        return categories.filter { it.selected }
            .map { it.code }
    }

    fun unselectAllCategories() {
        categories.map { it.selected = false }
        notifyDataSetChanged()
    }

    class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val category: ToggleButton = itemView.findViewById(R.id.selectable_category)

        fun bindViews(item: SelectableInterest) {
            category.isChecked = item.selected
            category.text = item.name
            category.setOnClickListener {
                category.text = item.name
                item.selected = !item.selected
                if (category.isChecked) {
                    category.setTextColor(
                        itemView.resources.getColor(
                            R.color.black,
                            itemView.context.theme
                        )
                    )
                    category.typeface = Typeface.DEFAULT_BOLD
                } else {
                    category.setTextColor(
                        itemView.resources.getColor(
                            R.color.not_selected_tint,
                            itemView.context.theme
                        )
                    )
                    category.typeface = Typeface.DEFAULT
                }
            }
        }
    }

}