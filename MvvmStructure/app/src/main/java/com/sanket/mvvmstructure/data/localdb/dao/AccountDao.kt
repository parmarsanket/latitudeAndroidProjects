package com.sanket.mvvmstructure.data.localdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount()





}