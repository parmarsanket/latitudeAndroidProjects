package com.sanket.mvvmstructure.databased.dao

import kotlinx.serialization.Serializable
@Serializable
data class StationResponse(
    val m: List<Station>
)
@Serializable
data class Station(
    val lt: String,      // Latitude
    val lg: String,      // Longitude
    val n: String,       // Name
    val i: String,       // Code or identifier
    val id: String,      // Full ID
    val b: String,       // Bearing or bus route (optional)
    val t: String,       // Towards direction (optional)
    val s: String,       // Stop letter (optional)
    val r: List<Route>,  // Routes
    val m: String,       // Mode (tube/bus/empty)
    val src: String,     // Source (e.g. "tfl")
    val a: String        // Agency (e.g. "tfl")
)
@Serializable
data class Route(
    val n: String,    // Route name or number
    val id: String,   // Route ID
    val c0: String,   // Color code (background)
    val c1: String,   // Color code (foreground/text)
    val m: String     // Mode (e.g. "bus", "tube")
)