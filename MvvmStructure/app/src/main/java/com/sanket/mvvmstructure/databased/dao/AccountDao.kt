package com.sanket.mvvmstructure.databased.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanket.mvvmstructure.databased.entity.Account

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(account: Account)

    @Query("SELECT * FROM Account WHERE email = :email AND password = :password")
    fun getAccount(email: String, password: String): Account?

    @Query("SELECT EXISTS(SELECT 1 FROM Account WHERE email = :email)")
    fun checkEmailExists(email: String): Int

    @Query("SELECT * FROM Account WHERE email = :email ")
    fun getAccountByEmail(email: String):Account

}