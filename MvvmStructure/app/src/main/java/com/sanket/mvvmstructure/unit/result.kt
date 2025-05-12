package com.sanket.mvvmstructure.unit

sealed class result<T>(
    val data:T?=null,
    val message :String?=null
) {
    class success<T>(data: T?):result<T>(data)
    class error<T>(message: String?,data:T?=null):result<T>(data )

}