package com.sanket.mvvmstructure.databased

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sanket.mvvmstructure.databased.dao.AccountDao
import com.sanket.mvvmstructure.databased.entity.Account

@Database(entities = [Account::class], version = 1, exportSchema = false)
abstract class Accountdatabased:RoomDatabase() {
    abstract fun user():AccountDao
}