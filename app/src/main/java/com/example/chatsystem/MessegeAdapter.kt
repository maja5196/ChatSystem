package com.example.chatsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessegeAdapter(val context: Context, val messegeList: ArrayList<Messege>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECIEVE = 1
    val ITEM_SEND = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        if(viewType == 1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false)
            return RecieveViewHolder(view)
        }else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            return SendViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessege = messegeList[position]

        if(holder.javaClass == SendViewHolder::class.java){
            val viewHolder = holder as SendViewHolder
            holder.send.text = currentMessege.messege
            holder.userSend.text = currentMessege.name
            holder.dateSend.text = currentMessege.date
        }else{
            val viewHolder = holder as RecieveViewHolder
            holder.recieve.text = currentMessege.messege
            holder.userRecieve.text = currentMessege.name
            holder.dateRecieve.text = currentMessege.date
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessege = messegeList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessege.senderId)){
            return ITEM_SEND
        }else{
            return ITEM_RECIEVE
        }
    }

    override fun getItemCount(): Int {
        return messegeList.size
    }

    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val send = itemView.findViewById<TextView>(R.id.sendMessege)
        val userSend = itemView.findViewById<TextView>(R.id.sendMessegeUser)
        val dateSend = itemView.findViewById<TextView>(R.id.sendMessegeDate)
    }

    class RecieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recieve = itemView.findViewById<TextView>(R.id.recieveMessege)
        val userRecieve = itemView.findViewById<TextView>(R.id.recieveMessegeUser)
        val dateRecieve = itemView.findViewById<TextView>(R.id.recieveMessegeDate)
    }
}