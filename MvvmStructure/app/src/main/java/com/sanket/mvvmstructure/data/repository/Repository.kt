package com.sanket.mvvmstructure.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.sanket.mvvmstructure.databased.dao.AccountDao
import com.sanket.mvvmstructure.databased.entity.Account
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Repository @Inject constructor(
    private val accountDao: AccountDao,
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth

) {

//    suspend fun addAccount(account: Account)
//    {
//        accountDao.addAccount(account)
//    }
//    suspend fun checkemailandpass(email: String,password:String): Account?
//    {
//      return  accountDao.getAccount(email = email, password = password)
//    }
//    suspend fun checkemail(email: String):Boolean
//    {
//       return accountDao.checkEmailExists(email)>0
//    }
//
//    suspend fun getAccountbyEmail(email: String):Account
//    {
//        return  accountDao.getAccountByEmail(email=email)
//    }
//    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//
//    fun saveLoginEmail(email: String) {
//        prefs.edit().putString("logged_in_email", email).apply()
//    }
//
//    fun getLoggedInEmail(): String? {
//        return prefs.getString("logged_in_email", null)
//    }
//
//    fun clearLogin() {
//        prefs.edit().remove("logged_in_email").apply()
//    }
fun signup(email: String, password: String, name: String, onResult: (Boolean, String?) -> Unit) {
    firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                FirebaseAuth.getInstance().signOut()
                user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                    onResult(updateTask.isSuccessful, updateTask.exception?.message)
                }
            } else {
                onResult(false, it.exception?.message)
            }
        }
}

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                onResult(it.isSuccessful, it.exception?.message)
            }
    }

    fun getCurrentUser() = firebaseAuth.currentUser
    fun logout() = firebaseAuth.signOut()
}
