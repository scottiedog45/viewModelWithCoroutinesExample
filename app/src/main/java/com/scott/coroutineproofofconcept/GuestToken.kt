package com.scott.coroutineproofofconcept


import com.google.gson.annotations.SerializedName

data class GuestToken(
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("token")
    val token: String
)