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
 * JWT Authentication Filter
 * 
 * This filter intercepts incoming requests and validates JWT tokens
 * from the Authorization header.
 */
@Component
class JwtAuthenticationFilter(
    private val tokenService: TokenService
) : OncePerRequestFilter() {

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
                            emptyList() // No roles for now
                        )
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            } catch (e: Exception) {
                logger.debug("Invalid JWT token: ${e.message}")
            }
        }
        filterChain.doFilter(request, response)
    }
}
