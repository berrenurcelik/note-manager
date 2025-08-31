package de.thm.mni.backend.auth.api.dto

/**
 * Datenklasse zur Darstellung einer Anforderung zur Benutzeranmeldung.
 *
 * Diese Klasse wird verwendet, um die vom Client gesendeten Anmeldeinformationen (Benutzername und Passwort) zu modellieren.
 *
 * @property username Der Benutzername des Benutzers.
 * @property password Das Passwort des Benutzers.
 */
data class LoginRequest(val username: String, val password: String)

/**
 * Datenklasse zur Darstellung der Antwort nach einer erfolgreichen Anmeldung.
 *
 * Diese Klasse wird verwendet, um das generierte JWT-Token an den Client zurückzusenden.
 *
 * @property token Das generierte JWT-Token.
 */
data class TokenResponse(val token: String)
