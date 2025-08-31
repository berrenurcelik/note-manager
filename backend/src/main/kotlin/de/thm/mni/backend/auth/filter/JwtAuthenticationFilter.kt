package de.thm.mni.backend.auth.filter

import de.thm.mni.backend.auth.token.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filter zur JWT-Authentifizierung.
 *
 * Dieser Filter fängt eingehende HTTP-Anfragen ab, um JWT-Tokens aus dem
 * "Authorization"-Header zu validieren. Bei einem gültigen Token wird
 * der Benutzer im [SecurityContextHolder] authentifiziert, um den Zugriff
 * auf geschützte Endpunkte zu ermöglichen.
 *
 * @param tokenService Der Dienst zur Erstellung, Validierung und Verwaltung von JWT-Tokens.
 */
@Component
class JwtAuthenticationFilter(
    private val tokenService: TokenService
) : OncePerRequestFilter() {

    /**
     * Führt die Logik des Filters für jede Anfrage aus.
     *
     * Überprüft, ob ein "Authorization"-Header mit einem gültigen "Bearer"-Token vorhanden ist.
     * Wenn das Token gültig ist, wird der Benutzer aus dem Token extrahiert und ein
     * [UsernamePasswordAuthenticationToken] im [SecurityContextHolder] gesetzt. Dies
     * ermöglicht Spring Security, den Benutzer für die aktuelle Anfrage als authentifiziert
     * zu betrachten.
     *
     * @param request Die eingehende HTTP-Anfrage.
     * @param response Die HTTP-Antwort.
     * @param filterChain Die Kette der Filter, die nach diesem Filter ausgeführt werden.
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)

            try {
                if (tokenService.isValid(token)) {
                    val user = tokenService.getUserFromToken(token)
                    if (user != null) {
                        val authentication = UsernamePasswordAuthenticationToken(
                            user.username,
                            null,
                            emptyList() // Hier könnten Rollen hinzugefügt werden
                        )
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            } catch (e: Exception) {
                // Bei einem ungültigen oder abgelaufenen Token wird der Authentifizierungsversuch
                // einfach ignoriert. Der Benutzer bleibt unauthentifiziert.
                logger.debug("Invalid JWT token: ${e.message}")
            }
        }
        filterChain.doFilter(request, response)
    }
}
