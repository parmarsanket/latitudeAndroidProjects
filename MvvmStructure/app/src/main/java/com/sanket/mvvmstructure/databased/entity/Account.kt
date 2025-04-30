package com.sanket.mvvmstructure.databased.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Account")
data class Account(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val email:String,
    val password:String,
    val name:String
)
