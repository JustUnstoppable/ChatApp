package com.example.chatapp.di

import android.content.Context
import com.example.chatapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import javax.inject.Singleton

//we create these modules to define our dependencies or our objects we want to reuse and we want to inject in our single classes.
// The application module defines those objects that should live as long as our application does so singleton
@Module
//below means these dependencies here to live long as our application does.
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    //to provide client instance
    @Provides
    fun provideChatClient(@ApplicationContext context: Context)=
        //build our chat client and provide it for all of our classes we want to able to inject it into
        ChatClient.Builder(context.getString(R.string.api_key),context).build()

}