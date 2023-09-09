package com.vustuntas.grouptalk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vustuntas.grouptalk.helperClass.MessageInfo
import java.util.ArrayList
import com.vustuntas.grouptalk.R

class MessageAdapter(val messageArrayList : ArrayList<MessageInfo>) : RecyclerView.Adapter<MessageAdapter.MessagesVH>() {
    class MessagesVH(itemView : View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
        return MessagesVH(itemView)
    }

    override fun onBindViewHolder(holder: MessagesVH, position: Int) {
        val messageInfoObject = messageArrayList.get(position) as MessageInfo
        holder.itemView.findViewById<TextView>(R.id.recycler_row_userMailAddressTextView).text = messageInfoObject.userMailAddress
        holder.itemView.findViewById<TextView>(R.id.recycler_row_userMessageTextView).text = messageInfoObject.userMessage
    }

    override fun getItemCount(): Int {
        return messageArrayList.size
    }
}