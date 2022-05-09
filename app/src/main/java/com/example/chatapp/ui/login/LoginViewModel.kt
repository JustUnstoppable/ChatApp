package com.example.chatapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.call.await
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//we want to able to provide rather inject the view model using dagger hilt
@HiltViewModel
class LoginViewModel @Inject constructor(
    //dependency to inject here is ChatClient
    private val client: ChatClient
    //as we have access to ChatClient in view model with that we can make network calls
):ViewModel() {
    //We are able to send events in this mutableSharedFlow instance but we don't want to send events from fragments to viewModel
    //so we don't want to access this instance from the fragment.
    private val _loginEvent =MutableSharedFlow<LogInEvent>()
    //public version is used to expose to the fragment.
    val loginEvent=_loginEvent.asSharedFlow()
    private fun isValidUsername(username: String)=
        username.length >= Constants.MIN_USERNAME_LENGTH

    //The main job of view model is now to connect the user using this client instance.
    fun connectUser(username: String){
        //It will remove leading and trailing spaces
        val trimmedUsername=username.trim()
        viewModelScope.launch {
            if(isValidUsername(trimmedUsername)){
                val result=client.connectGuestUser(
                    userId=trimmedUsername,
                    username=trimmedUsername
                ).await() //executes the network call inside of this Coroutine and gives us result and saves it in result var..
                if(result.isError){
                    //To send events from viewModel to fragment, we will use share flow
                    //sharedFlow== //Used to emit events //happen only once
                                 // when we rotate device these events won't fire off again.
                    _loginEvent.emit(LogInEvent.ErrorLogIn(result.error().message ?:"Unknown Error"))
                    return@launch
                }
                _loginEvent.emit(LogInEvent.Success)
            }else{
                _loginEvent.emit(LogInEvent.ErrorInputTooShort)
            }
        }
    }
    // This class will give us different types of events we can distinguish between that we can center a fragment
    sealed class LogInEvent{
        object ErrorInputTooShort : LogInEvent()
        data class ErrorLogIn(val error:String) :LogInEvent()
        object Success : LogInEvent()


    }
}