package com.example.chatsystem

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*


class Messege {
    var messege: String? = null
    var senderId: String? = null
    var date: String? = null
    var name: String? = null

    constructor(){}

    constructor(messege: String?, senderId: String?, name:String?, date: String?){
        this.messege = messege
        this.senderId = senderId
        this.name = name
        this.date = date
    }

}