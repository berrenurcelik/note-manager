package de.thm.mni.backend.auth.api

import de.thm.mni.backend.auth.api.dto.LoginRequest
import de.thm.mni.backend.auth.api.dto.TokenResponse
import de.thm.mni.backend.auth.token.TokenService
import de.thm.mni.backend.user.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controller für Authentifizierungs-Endpunkte.
 *
 * Stellt REST-Endpunkte für die Benutzerauthentifizierung bereit, wie z. B. die Anmeldung.
 *
 * @param userService Der Dienst zur Verwaltung von Benutzern.
 * @param tokenService Der Dienst zur Erstellung und Validierung von JWT-Tokens.
 */
@RestController
@RequestMapping("/api/auth")
class AuthApi(
    private val userService: UserService,
    private val tokenService: TokenService
) {
    /**
     * Authentifiziert einen Benutzer mit Benutzername und Passwort.
     *
     * Bei erfolgreicher Authentifizierung wird ein JWT-Token zurückgegeben.
     *
     * @param req Die Anforderung, die Benutzername und Passwort enthält.
     * @return Ein [ResponseEntity] mit einem [TokenResponse] im Falle eines Erfolgs (HTTP 200).
     * Andernfalls wird ein HTTP 401 (Unauthorized) Status zurückgegeben.
     */
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
