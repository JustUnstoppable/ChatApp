package com.example.chatapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.livedata.ChatDomain
import javax.inject.Inject

@HiltAndroidApp
class ChatApplication : Application(){
    @Inject
    lateinit var client:ChatClient
    override fun onCreate() {
        super.onCreate()
        //client is a thing for stream that makes all the network request to the stream servers
        //Initialise the stream SDK
        ChatDomain.Builder(client,applicationContext).build()     //livedata
    }
}