package com.correct.goldx.data

data class CitiesResponse(
    val data: List<String>,
    val error: Boolean,
    val msg: String
)