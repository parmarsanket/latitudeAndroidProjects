package com.sanket.mvvmstructure.data.repository

import com.sanket.mvvmstructure.databased.dao.AccountDao
import com.sanket.mvvmstructure.databased.entity.Account
import javax.inject.Inject

class Repository @Inject constructor(private val accountDao: AccountDao) {

    suspend fun addAccount(account: Account)
    {
        accountDao.addAccount(account)
    }
    suspend fun checkemailandpass(email: String,password:String): Account?
    {
      return  accountDao.getAccount(email = email, password = password)
    }
    suspend fun checkemail(email: String):Boolean
    {
       return accountDao.checkEmailExists(email)>0
    }
}