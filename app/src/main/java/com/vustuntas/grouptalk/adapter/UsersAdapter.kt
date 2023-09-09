package com.vustuntas.grouptalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import com.vustuntas.grouptalk.R

class UsersAdapter(val usersArrayList : ArrayList<String>) : RecyclerView.Adapter<UsersAdapter.UsersVH>() {
    class UsersVH(itemView : View) : RecyclerView.ViewHolder(itemView){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_user_list,parent,false)
        return UsersVH(itemView)
    }

    override fun onBindViewHolder(holder: UsersVH, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.recycler_row_user_list_userMailAddress).text = usersArrayList.get(position)
    }

    override fun getItemCount(): Int {
        return usersArrayList.size
    }
}