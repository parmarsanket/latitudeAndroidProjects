package com.sanket.mvvmstructure.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Insert
import com.sanket.mvvmstructure.databased.entity.Account
import com.sanket.mvvmstructure.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class appviewmodel @Inject constructor(
    private val repository: Repository
):ViewModel() {
     var email = mutableStateOf("")
         private set
     var password = mutableStateOf("")
        private set
     var name = mutableStateOf("")
        private set

    var errorMsg = mutableStateOf("")
        private set

    private val _loginResult = MutableLiveData<Account?>()
    val loginResult: LiveData<Account?> = _loginResult

    fun onEmailChanged(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChanged(newPassword: String) {
        password.value = newPassword
    }

    fun onNameChanged(newName: String) {
        name.value = newName
    }
    fun signup(){
        viewModelScope.launch (Dispatchers.IO){

            val exsit = repository.checkemail(email = email.value)

            withContext(Dispatchers.IO) {
                if (exsit) {
                    errorMsg.value = "EmailAddress Already Exists"
                } else {
                    repository.addAccount(
                        Account(
                            email = email.value,
                            password = password.value,
                            name = name.value
                        )
                    )
                    //signup to mainScreen
                }
            }

        }
    }
    fun login(){
        viewModelScope.launch {
            val account = repository.checkemailandpass(email = email.value, password.value)
            if (account == null) {
                errorMsg.value = "email or password maybe incorrect"

            }
            else{
            _loginResult.value=account
            //login to mainScreen
            }
        }
    }
}