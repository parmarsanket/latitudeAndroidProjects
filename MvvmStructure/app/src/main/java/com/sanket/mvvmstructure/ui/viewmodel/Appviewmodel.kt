package com.sanket.mvvmstructure.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.widget.Toast
import androidx.collection.emptyLongSet
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sanket.mvvmstructure.databased.entity.Account
import com.sanket.mvvmstructure.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.utf8Size
import javax.inject.Inject

@HiltViewModel
class Appviewmodel @Inject constructor(
    private val repository: Repository,

):ViewModel() {



    var errorMsgEmail =  MutableLiveData<String>("")
    var errorMsgPassworld =  MutableLiveData<String>("")
    var errorMsgName =  MutableLiveData<String>("")




    var _successSignup = MutableLiveData(false)
         private set
    var _successLogin = MutableLiveData(false)
        private set
    val successSignup:MutableLiveData<Boolean> = _successSignup

    private val _loginResult = MutableLiveData<Account?>(null)
    var loginResult: MutableLiveData<Account?> = _loginResult



//    fun autoLoginIfAvailable() {
//        val email = repository.getLoggedInEmail()
//        if (!email.isNullOrEmpty()) {
//            viewModelScope.launch(Dispatchers.IO) {
//                val account = repository.getAccountbyEmail(email = email)
//                account.let {
//                    _loginResult.postValue(it)
//                    successLoin.postValue(true)
//                }
//            }
//        }
//    }
//    fun signup(name:String,email: String,password: String){
//        viewModelScope.launch (Dispatchers.IO){
//
//            val exist = repository.checkemail(email = email)
//
//
//            withContext(Dispatchers.IO) {
//
//                 if (exist) {
//                    errorMsgEmail.postValue("Email Address already exist")
//                } else {
//                        repository.addAccount(
//                            Account(
//                                email = email,
//                                password = password,
//                                name = name
//                            )
//                        )
//                        //signup to main Sceen
//                     successSignup.postValue(true)
//                    }
//                }
//            }
//
//        }
//    fun logout() {
//        loginResult.postValue(null)
//        repository.clearLogin()
//    }
//
//    fun login(email:String,password:String){
//        viewModelScope.launch(Dispatchers.IO) {
//            val account = repository.checkemailandpass(email, password)
//            Log.d("LoginDebug", "Account returned: $account")
//            //val account = repository.checkemailandpass(email = email, password = password)
//            withContext(Dispatchers.IO){
//
//
//                 if(!repository.checkemail(email))
//                {
//                    errorMsgEmail.postValue("Email is incorrect")
//
//                }
//            else if (account == null) {
//                errorMsgPassworld.postValue("password is incorrect")
//            }
//            else{
//            _loginResult.postValue(account)
//            //login to mainScreen
//                successLoin.postValue(true)
//
//                     repository.saveLoginEmail(email)
//
//                     errorMsgEmail.postValue("")
//                     errorMsgEmail.postValue("")
//            }
//                }
//        }
//    }
    var user = MutableLiveData<FirebaseUser?>(null)
    var errorMessage = MutableLiveData<String?>(null)
    var loginSuccess = MutableLiveData<Boolean>(false)

    fun signup(email: String, password: String, name: String) {
        repository.signup(email, password, name) { success, error ->
            if (success) {
                user.value = repository.getCurrentUser()
                successSignup.postValue(true)
            } else {
                errorMessage.value = error
            }
        }
    }

    fun login(email: String, password: String) {
        repository.login(email, password) { success, error ->
            if (success) {
                user.value = repository.getCurrentUser()
                loginSuccess.value = true
            } else {
                errorMessage.value = error
            }
        }
    }

    fun logout() {
        repository.logout()
        user.value = null
        loginSuccess.value = false
    }
    fun autoLoginIfAvailable() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        user.value = currentUser
        if (currentUser != null) {
            loginSuccess.value = true
        }
    }
}