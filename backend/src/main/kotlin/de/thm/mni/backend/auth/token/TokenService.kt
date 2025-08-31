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

/**
 * Ein Dienst für die Erstellung, Validierung und Verwaltung von JWT-Tokens.
 *
 * Dieser Dienst verwendet ein RSA-Schlüsselpaar zur sicheren Signierung und Verifizierung der Tokens.
 *
 * @param userService Der Dienst zum Abrufen von Benutzerinformationen.
 */
@Service
class TokenService(
    private val userService: UserService
) {
    /** Ein zufällig generiertes RSA-Schlüsselpaar für die Signierung und Verifizierung von Tokens. */
    val keyPair: KeyPair = KeyPairGenerator.getInstance("RSA").genKeyPair()

    /** Der öffentliche Schlüssel aus dem generierten Schlüsselpaar. Wird zur Verifizierung von Tokens verwendet. */
    val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey

    /** Der private Schlüssel aus dem generierten Schlüsselpaar. Wird zur Signierung von Tokens verwendet. */
    val privateKey: RSAPrivateKey = keyPair.private as RSAPrivateKey

    /** Der Aussteller-Wert für das JWT, der aus der Anwendungskonfiguration geladen wird. */
    @Value("\${spring.application.name}")
    private val issuer: String = ""

    /** Die Gültigkeitsdauer eines Tokens in Millisekunden (hier: 1 Stunde). */
    private val TIME_TO_LIVE = 60 * 60 * 1000 // 1 hour in milliseconds

    /**
     * Erstellt ein neues JWT für den angegebenen Benutzer.
     *
     * Das Token enthält den Benutzernamen, die E-Mail-Adresse, eine Ablaufzeit und die Benutzer-ID als Subjekt.
     * Es wird mit dem privaten RSA-Schlüssel signiert.
     *
     * @param user Das [User]-Objekt, für das das Token erstellt wird.
     * @return Der signierte JWT-Token-String.
     */
    fun create(user: User): String = JWT.create()
        .withIssuer(issuer)
        .withSubject(user.id.toString())
        .withIssuedAt(Date())
        .withExpiresAt(Date(System.currentTimeMillis() + TIME_TO_LIVE))
        .withClaim("username", user.username)
        .withClaim("email", user.email)
        .sign(Algorithm.RSA256(privateKey))

    /**
     * Überprüft, ob ein JWT gültig ist.
     *
     * Diese Methode gibt `true` zurück, wenn das Token erfolgreich dekodiert und verifiziert werden kann,
     * andernfalls `false`.
     *
     * @param token Der zu validierende Token-String.
     * @return `true`, wenn das Token gültig ist, `false` andernfalls.
     */
    fun isValid(token: String): Boolean {
        return try {
            decode(token) != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Dekodiert und verifiziert einen JWT-Token.
     *
     * Das Token wird mit dem öffentlichen Schlüssel und dem erwarteten Aussteller verifiziert.
     *
     * @param token Der zu dekodierende Token-String.
     * @return Ein [DecodedJWT]-Objekt bei einem gültigen Token, andernfalls `null`.
     */
    fun decode(token: String): DecodedJWT? {
        val verifier = JWT.require(Algorithm.RSA256(publicKey))
            .withIssuer(issuer)
            .build()
        return verifier.verify(token)
    }

    /**
     * Extrahiert die Benutzerinformationen aus einem JWT.
     *
     * Zuerst wird der Token dekodiert, um die Benutzer-ID zu extrahieren. Anschließend wird der
     * entsprechende Benutzer aus dem [UserService] abgerufen.
     *
     * @param token Der JWT-Token-String.
     * @return Das [User]-Objekt, wenn es gefunden wurde, andernfalls `null`.
     */
    fun getUserFromToken(token: String): User? {
        val decodedJWT = decode(token) ?: return null
        val userId = decodedJWT.subject ?: return null
        return userService.find(UUID.fromString(userId))
    }
}
