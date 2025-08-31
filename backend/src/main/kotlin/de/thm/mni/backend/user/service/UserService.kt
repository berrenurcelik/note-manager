package de.thm.mni.backend.user.service

import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * Service-Klasse zur Verwaltung von Benutzerlogik.
 *
 * Diese Klasse kapselt die Geschäftslogik für Benutzeroperationen und interagiert
 * mit dem [UserRepository] für den Datenzugriff.
 *
 * @property userRepository Das Repository für den Zugriff auf Benutzerdaten.
 */
@Service
class UserService(private val userRepository: UserRepository) {

    /**
     * Erstellt einen neuen Benutzer und speichert ihn in der Datenbank.
     *
     * @param username Der Benutzername des neuen Benutzers.
     * @param firstName Der Vorname des Benutzers.
     * @param lastName Der Nachname des Benutzers.
     * @param email Die E-Mail-Adresse des Benutzers.
     * @param password Das Passwort des Benutzers (sollte gehasht sein).
     * @return Der neu erstellte und gespeicherte [User].
     */
    fun create(username: String, firstName: String, lastName: String, email: String, password: String): User {
        val user = User(username = username, firstName = firstName, lastName = lastName, email = email, password = password)
        return userRepository.save(user)
    }

    /**
     * Sucht einen Benutzer anhand seiner ID.
     *
     * @param id Die UUID des Benutzers.
     * @return Der gefundene [User] oder `null`, falls kein Benutzer gefunden wurde.
     */
    fun find(id: UUID): User? = userRepository.findById(id).orElse(null)

    /**
     * Sucht einen Benutzer anhand seiner ID.
     *
     * @param id Die UUID des Benutzers.
     * @return Der gefundene [User] oder `null`, falls kein Benutzer gefunden wurde.
     */
    fun findById(id: UUID): User? = userRepository.findById(id).orElse(null)

    /**
     * Sucht einen Benutzer anhand seines Benutzernamens.
     *
     * @param username Der Benutzername des zu suchenden Benutzers.
     * @return Der gefundene [User] oder `null`, falls kein Benutzer gefunden wurde.
     */
    fun findByUsername(username: String): User? = userRepository.findByUsername(username)

    /**
     * Ruft eine Liste aller in der Datenbank gespeicherten Benutzer ab.
     *
     * @return Eine Liste aller [User].
     */
    fun findAll(): List<User> = userRepository.findAll()

    /**
     * Aktualisiert einen bestehenden Benutzer in der Datenbank.
     *
     * @param id Die UUID des zu aktualisierenden Benutzers.
     * @param username Der neue Benutzername.
     * @param firstName Der neue Vorname.
     * @param lastName Der neue Nachname.
     * @param email Die neue E-Mail-Adresse.
     * @param password Das neue Passwort (sollte gehasht sein).
     * @return Der aktualisierte [User] oder `null`, falls der Benutzer nicht gefunden wurde.
     */
    fun update(id: UUID, username: String, firstName: String, lastName: String, email: String, password: String): User? {
        val existingUser = userRepository.findById(id).orElse(null)
        return if (existingUser != null) {
            val updatedUser = User(
                id = existingUser.id,
                username = username,
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password
            )
            userRepository.save(updatedUser)
        } else {
            null
        }
    }

    /**
     * Löscht einen Benutzer anhand seiner ID.
     *
     * @param id Die UUID des zu löschenden Benutzers.
     * @return `true`, wenn der Benutzer erfolgreich gelöscht wurde, sonst `false`.
     */
    fun delete(id: UUID): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}
