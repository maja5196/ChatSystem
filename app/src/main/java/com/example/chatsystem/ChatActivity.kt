package com.example.chatsystem

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ChatActivity : AppCompatActivity() {

    private  lateinit var chatRecyclerView: RecyclerView
    private lateinit var messegeBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messegeAdapter: MessegeAdapter
    private lateinit var messegeList: ArrayList<Messege>
    private lateinit var mDBRef: DatabaseReference
    private lateinit var name: String
    private lateinit var user: String

    var recieverRoom: String? = null
    var senderRoom: String? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)



        name = intent.getStringExtra("name").toString()
        val recieverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser!!.uid
        mDBRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = recieverUid + senderUid
        recieverRoom = senderUid + recieverUid
        supportActionBar?.title = name


        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messegeBox = findViewById(R.id.messege)
        sendButton = findViewById(R.id.sendButton)
        messegeList = ArrayList()
        messegeAdapter = MessegeAdapter(this, messegeList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messegeAdapter
        val mDB = FirebaseDatabase.getInstance().getReference("user")
        mDB.child(senderUid).get().addOnSuccessListener {
            user = it.child("name").value.toString()
        }

        //adding data to recyclerView
        mDBRef.child("chats").child(senderRoom!!).child("messeges")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messegeList.clear()
                    for(postSnapchot in snapshot.children){
                       val messege = postSnapchot.getValue(Messege::class.java)
                        messegeList.add(messege!!)
                    }
                    chatRecyclerView.scrollToPosition(messegeAdapter.getItemCount()-1)
                    messegeAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //adding the messege to the databse
        sendButton.setOnClickListener {
            val messege = messegeBox.text.toString()
            val calendar = Calendar.getInstance()
            val timeStamp = SimpleDateFormat("dd-MM-yyyy HH:mm").format(Date())

            val messegeobject = Messege(messege, senderUid, user, timeStamp)

            mDBRef.child("chats").child(senderRoom!!).child("messeges").push()
                .setValue(messegeobject).addOnSuccessListener {
                    mDBRef.child("chats").child(recieverRoom!!).child("messeges").push()
                        .setValue(messegeobject)
                }
            messegeBox.setText("")
        }
    }

    // this event will enable the back
    // function to the button on press
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id==android.R.id.home){
            this.finish()
        }
        return super.onOptionsItemSelected(item)
    }


}