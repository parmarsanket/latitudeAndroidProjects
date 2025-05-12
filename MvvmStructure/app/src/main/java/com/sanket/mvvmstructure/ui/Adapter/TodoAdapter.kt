package com.sanket.mvvmstructure.ui.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import com.sanket.mvvmstructure.R
import com.sanket.mvvmstructure.databased.dao.Station

class TodoAdapter (
   private val todos:List<Station>,
    private val onItemClicked: (Station) -> Unit
):RecyclerView.Adapter<TodoAdapter.TodoViewHolder>(){
    inner class TodoViewHolder(itemview:View):RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items,parent,false)

    return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.itemView.apply {
            val tvItems = findViewById<TextView>(R.id.tvItems)
            tvItems.text = todos[position].n
            setOnClickListener {
                onItemClicked(todos[position])
            }

        }
    }
}

