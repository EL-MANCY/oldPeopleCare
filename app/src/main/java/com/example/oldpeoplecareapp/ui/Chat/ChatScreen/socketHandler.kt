package com.example.oldpeoplecareapp.ui.Chat.ChatScreen

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object socketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("https://old-care.onrender.com")
        //    mSocket = IO.socket("http://10.0.2.2:8000")
        } catch (e: URISyntaxException) {

            Log.i("SocketConnected",e.toString())

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
        Log.i("SocketConnected","got Socket")

    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
        Log.i("SocketConnected","Connected")
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}