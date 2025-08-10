package de.thm.mni.backend.auth.api.dto

data class LoginRequest(val username: String, val password: String)
data class TokenResponse(val token: String)
