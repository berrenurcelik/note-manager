package de.thm.mni.backend.user.api

import de.thm.mni.backend.user.api.dto.UserInput
import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import java.util.UUID

/**
 * GraphQL-Controller für Benutzer-Operationen.
 *
 * Diese Klasse stellt GraphQL-Endpunkte für die Abfrage und Mutation von Benutzerdaten bereit.
 * Die Endpunkte sind unter `/graphql` verfügbar.
 *
 * @property userService Der Dienst zur Verarbeitung der Benutzerlogik.
 */
@Controller
class UserGraphQLController(private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(UserGraphQLController::class.java)

    /**
     * Abfrage, um alle Benutzer abzurufen.
     *
     * @return Eine Liste aller Benutzer.
     */
    @QueryMapping
    fun users(): List<User> {
        val auth = SecurityContextHolder.getContext().authentication
        logger.info("GraphQL-Abfrage: Benutzer von authentifiziertem Benutzer: ${auth?.name}")
        return userService.findAll()
    }

    /**
     * Abfrage, um einen Benutzer anhand seiner ID abzurufen.
     *
     * @param id Die ID des Benutzers als String.
     * @return Der Benutzer oder `null`, wenn er nicht gefunden wurde.
     */
    @QueryMapping
    fun user(@Argument id: String): User? {
        logger.info("GraphQL-Abfrage: Benutzer mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            userService.findById(uuid)
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id")
            null
        }
    }

    /**
     * Abfrage, um einen Benutzer anhand seines Benutzernamens abzurufen.
     *
     * @param username Der Benutzername.
     * @return Der Benutzer oder `null`, wenn er nicht gefunden wurde.
     */
    @QueryMapping
    fun userByUsername(@Argument username: String): User? {
        logger.info("GraphQL-Abfrage: userByUsername mit Benutzername: $username")
        return userService.findByUsername(username)
    }

    /**
     * Mutation, um einen neuen Benutzer zu erstellen.
     *
     * @param input Die Eingabedaten des Benutzers ([UserInput]).
     * @return Der neu erstellte Benutzer.
     */
    @MutationMapping
    fun createUser(@Argument input: UserInput): User {
        logger.info("GraphQL-Mutation: createUser mit Benutzername: ${input.username}")
        val user = userService.create(input.username, input.firstName, input.lastName, input.email, input.password)
        logger.info("Benutzer über GraphQL erstellt: $user")
        return user
    }

    /**
     * Mutation, um einen bestehenden Benutzer zu aktualisieren.
     *
     * @param id Die ID des zu aktualisierenden Benutzers als String.
     * @param input Die aktualisierten Benutzerdaten ([UserInput]).
     * @return Der aktualisierte Benutzer oder `null`, wenn er nicht gefunden wurde.
     */
    @MutationMapping
    fun updateUser(@Argument id: String, @Argument input: UserInput): User? {
        logger.info("GraphQL-Mutation: updateUser mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            userService.update(uuid, input.username, input.firstName, input.lastName, input.email, input.password)
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id")
            null
        }
    }

    /**
     * Mutation, um einen Benutzer zu löschen.
     *
     * @param id Die ID des zu löschenden Benutzers als String.
     * @return `true`, wenn der Benutzer gelöscht wurde, sonst `false`.
     */
    @MutationMapping
    fun deleteUser(@Argument id: String): Boolean {
        logger.info("GraphQL-Mutation: deleteUser mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            userService.delete(uuid)
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id")
            false
        }
    }
}
