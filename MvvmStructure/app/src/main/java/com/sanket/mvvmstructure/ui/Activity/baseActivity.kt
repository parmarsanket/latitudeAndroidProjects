package com.sanket.mvvmstructure.ui.Activity

import android.os.Bundle
import android.util.Patterns
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sanket.mvvmstructure.R

open class baseActivity : AppCompatActivity() {

   fun emailvalidation(email:String,errorEmailMsg:(String)->Unit):Boolean {
       return when{
           email.isEmpty() -> {
               errorEmailMsg("Email is required")
               false
           }
           !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
               errorEmailMsg("Invalid email format")
               false
           }
           else -> {
               errorEmailMsg("")
               true
           }
       }

   }
    fun passwordvalidation(password:String,errorPasswordMsg:(String)->Unit):Boolean
    {
        return when{
            password.isEmpty() -> {
                errorPasswordMsg("Password is required")
                false
            }
            password.length<8 -> {
                errorPasswordMsg("password minimum required 8 latter")
                false
            }
            else -> {
                errorPasswordMsg("")
                true
            }
        }
    }
    fun namevalidation(name: String,errorNameMsg: (String) -> Unit):Boolean
    {
        return when{
            name.isEmpty() -> {
                errorNameMsg("Name is required")
                false
            }
            else -> {
                errorNameMsg("")
                true
            }
        }
    }
}