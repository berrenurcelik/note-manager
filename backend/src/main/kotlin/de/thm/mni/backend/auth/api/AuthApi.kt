package de.thm.mni.backend.auth.api

import de.thm.mni.backend.auth.api.dto.LoginRequest
import de.thm.mni.backend.auth.api.dto.TokenResponse
import de.thm.mni.backend.auth.token.TokenService
import de.thm.mni.backend.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthApi(
    private val userService: UserService,
    private val tokenService: TokenService
) {
    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<TokenResponse> {
        val user = userService.findByUsername(req.username)
        return if (user != null && user.password == req.password) {
            val token = tokenService.create(user)
            ResponseEntity.ok(TokenResponse(token))
        } else {
            ResponseEntity.status(401).build()
        }
    }
}
