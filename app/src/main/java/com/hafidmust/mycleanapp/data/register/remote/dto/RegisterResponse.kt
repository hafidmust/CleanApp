package com.hafidmust.mycleanapp.data.register.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int?,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("token")
	val token: String
)
