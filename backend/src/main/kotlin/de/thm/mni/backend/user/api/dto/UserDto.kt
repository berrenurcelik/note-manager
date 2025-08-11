package de.thm.mni.backend.user.api.dto

data class UserCreate(
    val username: String,
    val password: String
)

data class UserResponse(
    val id: String,
    val username: String,
    val roles: List<String>
)
