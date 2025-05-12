package com.sanket.mvvmstructure.data.api

import com.sanket.mvvmstructure.databased.dao.StationResponse
import retrofit2.http.GET


interface Apiservice {
@GET("nearestv2.php?k=busapp123&lt=51.50739774204607&lg=-0.12760374695062637")
suspend fun getLocation(): StationResponse
}