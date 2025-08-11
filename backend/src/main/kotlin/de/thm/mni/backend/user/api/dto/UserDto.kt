package de.thm.mni.backend.user.api.dto

data class UserCreate(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class UserInput(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

data class UserResponse(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String
)
