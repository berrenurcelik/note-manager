package de.thm.mni.backend.auth.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date
import java.util.UUID

@Service
class TokenService(
    private val userService: UserService
) {
    val keyPair: KeyPair = KeyPairGenerator.getInstance("RSA").genKeyPair()
    val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey
    val privateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey

    @Value("\${spring.application.name}")
    private val issuer: String = ""

    private val TIME_TO_LIVE = 60 * 60 * 1000 // 1 hour in milliseconds

    fun create(user: User): String = JWT.create()
        .withIssuer(issuer)
        .withSubject(user.id.toString())
        .withIssuedAt(Date())
        .withExpiresAt(Date(System.currentTimeMillis() + TIME_TO_LIVE))
        .withClaim("username", user.username)
        .withClaim("email", user.email)
        .sign(Algorithm.RSA256(privateKey))

    fun isValid(token: String): Boolean {
        return try {
            decode(token) != null
        } catch (e: Exception) {
            false
        }
    }

    fun decode(token: String): DecodedJWT? {
        val verifier = JWT.require(Algorithm.RSA256(publicKey))
            .withIssuer(issuer)
            .build()
        return verifier.verify(token)
    }

    fun getUserFromToken(token: String): User? {
        val decodedJWT = decode(token) ?: return null
        val userId = decodedJWT.subject ?: return null
        return userService.find(UUID.fromString(userId))
    }
}
