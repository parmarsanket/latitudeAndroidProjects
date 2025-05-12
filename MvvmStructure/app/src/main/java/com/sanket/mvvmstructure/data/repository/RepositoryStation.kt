package com.sanket.mvvmstructure.data.repository

import com.sanket.mvvmstructure.data.api.Apiservice
import com.sanket.mvvmstructure.databased.dao.Station
import com.sanket.mvvmstructure.unit.result

class RepositoryStation(
private val apiservice: Apiservice
) {
    suspend fun fetchStations(): result<List<Station>> {
        return try {
            val response = apiservice.getLocation()
            result.success(response.m)
        } catch (e: Exception) {
           result.error(e.toString())
        }
    }
}