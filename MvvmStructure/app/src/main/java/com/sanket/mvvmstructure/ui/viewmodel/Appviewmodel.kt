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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sanket.mvvmstructure.databased.entity.Account
import com.sanket.mvvmstructure.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.utf8Size
import javax.inject.Inject
import android.app.Activity
import android.content.Intent
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider

@HiltViewModel
class Appviewmodel @Inject constructor(
    private val repository: Repository,

):ViewModel() {

    var errorMsgEmail =  MutableLiveData<String>("")
    var errorMsgPassworld =  MutableLiveData<String>("")
    var errorMsgName =  MutableLiveData<String>("")

    // Use MutableStateFlow or LiveData to observe bottom sheet state
    private val _bottomSheetState = MutableLiveData<Int>()
    val bottomSheetState: LiveData<Int> = _bottomSheetState

    fun setBottomSheetState(state: Int) {
        Log.d("BottomSheetTracker", "ViewModel: BottomSheet state set to $state")
        _bottomSheetState.value = state
    }
    fun forceEmitCurrentState() {
        _bottomSheetState.value = _bottomSheetState.value // triggers collectors again
    }

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
    val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1028046261055-kq6vr31fff8o1kqth6nhca5ene0p4d4u.apps.googleusercontent.com") // Get from google-services.json
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    lateinit var context: Context

    fun initializeContext(appContext: Context) {
        context = appContext
    }

    private val _googleUser = MutableLiveData<FirebaseUser?>()
    val googleUser: LiveData<FirebaseUser?> = _googleUser

    fun handleGoogleSignInResult(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credentialManager = CredentialManager.create(activity)

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false) // Set to true to only show already-used accounts
                    .setServerClientId("1028046261055-kq6vr31fff8o1kqth6nhca5ene0p4d4u.apps.googleusercontent.com")
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = activity
                )

                val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val idToken = credential.idToken

                if (idToken != null) {

                    firebaseAuthWithGoogle(idToken)
                } else {
                    errorMessage.postValue("Google Sign-In failed: No ID token.")
                }

            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Exception: ${e.message}")
                errorMessage.postValue("Google Sign-In error: ${e.localizedMessage}")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = FirebaseAuth.getInstance().currentUser
                    _googleUser.postValue(currentUser)
                    user.value = currentUser
                    loginSuccess.postValue(true)
                } else {
                    errorMessage.postValue("Firebase Auth failed: ${task.exception?.message}")
                }
            }
    }

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
        googleSignInClient.signOut()
        loginSuccess.value = false
        _googleUser.value = null
    }
    fun autoLoginIfAvailable() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        user.value = currentUser
        if (currentUser != null) {
            loginSuccess.value = true
        }
    }
}