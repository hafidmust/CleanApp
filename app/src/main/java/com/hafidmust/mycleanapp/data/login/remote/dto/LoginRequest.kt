package com.hafidmust.mycleanapp.data.login.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String
)
