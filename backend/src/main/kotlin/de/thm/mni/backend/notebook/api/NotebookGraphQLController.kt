package de.thm.mni.backend.notebook.api

import de.thm.mni.backend.notebook.api.dto.NotebookInput
import de.thm.mni.backend.notebook.model.Notebook
import de.thm.mni.backend.notebook.repository.NotebookRepository
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import java.util.Date
import java.util.UUID

/**
 * GraphQL-Controller für Notizbuch-Operationen.
 *
 * Diese Klasse stellt GraphQL-Endpunkte für die Abfrage und Mutation von Notizbuchdaten bereit.
 * Die Endpunkte sind unter `/graphql` verfügbar.
 *
 * @property notebookRepository Das Repository für den Zugriff auf Notizbuchdaten.
 */
@Controller
class NotebookGraphQLController(private val notebookRepository: NotebookRepository) {
    private val logger = LoggerFactory.getLogger(NotebookGraphQLController::class.java)

    /**
     * Abfrage, um alle Notizbücher abzurufen.
     *
     * @return Eine Liste aller Notizbücher.
     */
    @QueryMapping
    fun notebooks(): List<Notebook> {
        val auth = SecurityContextHolder.getContext().authentication
        logger.info("GraphQL-Abfrage: Alle Notizbücher von authentifiziertem Benutzer: ${auth?.name}")
        return notebookRepository.findAll()
    }

    /**
     * Abfrage, um ein Notizbuch anhand seiner ID abzurufen.
     *
     * @param id Die ID des Notizbuchs als String.
     * @return Das Notizbuch oder `null`, wenn es nicht gefunden wurde.
     */
    @QueryMapping
    fun notebook(@Argument id: String): Notebook? {
        logger.info("GraphQL-Abfrage: Notizbuch mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            notebookRepository.findById(uuid).orElse(null)
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id")
            null
        }
    }

    /**
     * Abfrage, um Notizbücher anhand der Benutzer-ID abzurufen.
     *
     * @param userId Die ID des Benutzers als String.
     * @return Eine Liste von Notizbüchern des Benutzers.
     */
    @QueryMapping
    fun notebooksByUser(@Argument userId: String): List<Notebook> {
        logger.info("GraphQL-Abfrage: Notizbücher für Benutzer-ID: $userId")
        return notebookRepository.findByUserId(userId)
    }

    /**
     * Mutation, um ein neues Notizbuch zu erstellen.
     *
     * @param input Die Eingabedaten des Notizbuchs ([NotebookInput]).
     * @return Das neu erstellte Notizbuch.
     */
    @MutationMapping
    fun createNotebook(@Argument input: NotebookInput): Notebook? {
        logger.info("GraphQL-Mutation: createNotebook mit Titel: ${input.title}")
        
        val auth = SecurityContextHolder.getContext().authentication
        val username = auth?.name
        
        if (username.isNullOrEmpty()) {
            logger.error("Benutzer nicht authentifiziert")
            return null
        }

        val notebook = Notebook(
            title = input.title,
            image = input.image ?: "",
            userId = username,
            createdAt = Date()
        )
        
        val savedNotebook = notebookRepository.save(notebook)
        logger.info("Notizbuch über GraphQL erstellt: $savedNotebook")
        return savedNotebook
    }

    /**
     * Mutation, um ein bestehendes Notizbuch zu aktualisieren.
     *
     * @param id Die ID des zu aktualisierenden Notizbuchs als String.
     * @param input Die aktualisierten Notizbuchdaten ([NotebookInput]).
     * @return Das aktualisierte Notizbuch oder `null`, wenn es nicht gefunden wurde.
     */
    @MutationMapping
    fun updateNotebook(@Argument id: String, @Argument input: NotebookInput): Notebook? {
        logger.info("GraphQL-Mutation: updateNotebook mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            val existingNotebook = notebookRepository.findById(uuid)
            
            if (!existingNotebook.isPresent) {
                logger.error("Notizbuch nicht gefunden: $id")
                return null
            }

            val updatedNotebook = Notebook(
                id = uuid,
                title = input.title,
                image = input.image ?: existingNotebook.get().image,
                userId = existingNotebook.get().userId,
                createdAt = existingNotebook.get().createdAt
            )
            
            val savedNotebook = notebookRepository.save(updatedNotebook)
            logger.info("Notizbuch über GraphQL aktualisiert: $savedNotebook")
            savedNotebook
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id")
            null
        }
    }

    /**
     * Mutation, um ein Notizbuch zu löschen.
     *
     * @param id Die ID des zu löschenden Notizbuchs als String.
     * @return `true`, wenn das Notizbuch gelöscht wurde, sonst `false`.
     */
    @MutationMapping
    fun deleteNotebook(@Argument id: String): Boolean {
        logger.info("GraphQL-Mutation: deleteNotebook mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            val exists = notebookRepository.existsById(uuid)
            if (exists) {
                notebookRepository.deleteById(uuid)
                logger.info("Notizbuch über GraphQL gelöscht: $id")
                true
            } else {
                logger.error("Notizbuch nicht gefunden: $id")
                false
            }
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id")
            false
        }
    }
}
