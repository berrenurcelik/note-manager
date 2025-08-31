package de.thm.mni.backend.user.api.dto

/**
 * Datenübertragungsobjekt (DTO) zum Erstellen eines neuen Benutzers.
 *
 * @param username Der einzigartige Benutzername.
 * @param firstName Der Vorname des Benutzers.
 * @param lastName Der Nachname des Benutzers.
 * @param email Die E-Mail-Adresse des Benutzers.
 * @param password Das Passwort des Benutzers (im Klartext, sollte vor der Speicherung gehasht werden).
 */
data class UserCreate(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

/**
 * Datenübertragungsobjekt (DTO) für Benutzereingaben.
 *
 * Dies ist eine generische Klasse für Benutzereingaben, die alle Felder zur
 * Erstellung oder Aktualisierung eines Benutzers enthalten kann.
 *
 * @param username Der Benutzername.
 * @param firstName Der Vorname.
 * @param lastName Der Nachname.
 * @param email Die E-Mail-Adresse.
 * @param password Das Passwort.
 */
data class UserInput(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

/**
 * Datenübertragungsobjekt (DTO) für die Antwort an den Benutzer.
 *
 * Dies wird verwendet, um Benutzerdetails an den Client zurückzugeben und dabei
 * sensible Informationen wie das Passwort auszulassen.
 *
 * @param id Die eindeutige ID des Benutzers.
 * @param username Der Benutzername.
 * @param firstName Der Vorname.
 * @param lastName Der Nachname.
 * @param email Die E-Mail-Adresse.
 */
data class UserResponse(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String
)
