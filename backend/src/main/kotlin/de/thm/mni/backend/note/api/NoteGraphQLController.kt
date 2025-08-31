package de.thm.mni.backend.note.api

import de.thm.mni.backend.note.api.dto.NoteInput
import de.thm.mni.backend.note.model.Note
import de.thm.mni.backend.note.repository.NoteRepository
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
 * GraphQL-Controller für Notiz-Operationen.
 *
 * Diese Klasse stellt GraphQL-Endpunkte für die Abfrage und Mutation von Notizdaten bereit.
 * Die Endpunkte sind unter `/graphql` verfügbar.
 *
 * @property noteRepository Das Repository für den Zugriff auf Notizdaten.
 * @property notebookRepository Das Repository für den Zugriff auf Notizbuchdaten.
 */
@Controller
class NoteGraphQLController(
    private val noteRepository: NoteRepository,
    private val notebookRepository: NotebookRepository
) {
    private val logger = LoggerFactory.getLogger(NoteGraphQLController::class.java)

    /**
     * Abfrage, um alle Notizen abzurufen.
     *
     * @return Eine Liste aller Notizen.
     */
    @QueryMapping
    fun notes(): List<Note> {
        val auth = SecurityContextHolder.getContext().authentication
        logger.info("GraphQL-Abfrage: Alle Notizen von authentifiziertem Benutzer: ${auth?.name}")
        return noteRepository.findAll()
    }

    /**
     * Abfrage, um eine Notiz anhand ihrer ID abzurufen.
     *
     * @param id Die ID der Notiz als String.
     * @return Die Notiz oder `null`, wenn sie nicht gefunden wurde.
     */
    @QueryMapping
    fun note(@Argument id: String): Note? {
        logger.info("GraphQL-Abfrage: Notiz mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            noteRepository.findById(uuid).orElse(null)
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id")
            null
        }
    }

    /**
     * Abfrage, um Notizen anhand der Notizbuch-ID abzurufen.
     *
     * @param notebookId Die ID des Notizbuchs als String.
     * @return Eine Liste von Notizen in dem Notizbuch.
     */
    @QueryMapping
    fun notesByNotebook(@Argument notebookId: String): List<Note> {
        logger.info("GraphQL-Abfrage: Notizen für Notizbuch-ID: $notebookId")
        return try {
            val uuid = UUID.fromString(notebookId)
            noteRepository.findByNotebookId(uuid)
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $notebookId")
            emptyList()
        }
    }

    /**
     * Abfrage, um Notizen anhand des Titels zu filtern.
     * Diese Implementierung erfüllt die 90%-Anforderung für Titel-Filterung über GraphQL.
     *
     * @param title Der Titel oder Teil des Titels zum Filtern.
     * @return Eine Liste von Notizen, die den Titel enthalten.
     */
    @QueryMapping
    fun notesByTitle(@Argument title: String): List<Note> {
        logger.info("GraphQL-Abfrage: Notizen mit Titel-Filter: $title")
        return noteRepository.findByTitleContainingIgnoreCase(title)
    }

    /**
     * Mutation, um eine neue Notiz zu erstellen.
     *
     * @param input Die Eingabedaten der Notiz ([NoteInput]).
     * @return Die neu erstellte Notiz.
     */
    @MutationMapping
    fun createNote(@Argument input: NoteInput): Note? {
        logger.info("GraphQL-Mutation: createNote mit Titel: ${input.title}")
        
        val auth = SecurityContextHolder.getContext().authentication
        val username = auth?.name
        
        if (username.isNullOrEmpty()) {
            logger.error("Benutzer nicht authentifiziert")
            return null
        }

        return try {
            val notebookUuid = UUID.fromString(input.notebookId)
            val notebook = notebookRepository.findById(notebookUuid)
            
            if (!notebook.isPresent) {
                logger.error("Notizbuch nicht gefunden: ${input.notebookId}")
                return null
            }

            val note = Note(
                title = input.title,
                content = input.content,
                userId = username,
                notebookId = notebookUuid,
                createdAt = Date(),
                modifiedAt = Date()
            )
            
            val savedNote = noteRepository.save(note)
            logger.info("Notiz über GraphQL erstellt: $savedNote")
            savedNote
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: ${input.notebookId}")
            null
        }
    }

    /**
     * Mutation, um eine bestehende Notiz zu aktualisieren.
     *
     * @param id Die ID der zu aktualisierenden Notiz als String.
     * @param input Die aktualisierten Notizdaten ([NoteInput]).
     * @return Die aktualisierte Notiz oder `null`, wenn sie nicht gefunden wurde.
     */
    @MutationMapping
    fun updateNote(@Argument id: String, @Argument input: NoteInput): Note? {
        logger.info("GraphQL-Mutation: updateNote mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            val existingNote = noteRepository.findById(uuid)
            
            if (!existingNote.isPresent) {
                logger.error("Notiz nicht gefunden: $id")
                return null
            }

            val notebookUuid = UUID.fromString(input.notebookId)
            val notebook = notebookRepository.findById(notebookUuid)
            
            if (!notebook.isPresent) {
                logger.error("Notizbuch nicht gefunden: ${input.notebookId}")
                return null
            }

            val updatedNote = Note(
                id = uuid,
                title = input.title,
                content = input.content,
                userId = existingNote.get().userId,
                notebookId = notebookUuid,
                createdAt = existingNote.get().createdAt,
                modifiedAt = Date()
            )
            
            val savedNote = noteRepository.save(updatedNote)
            logger.info("Notiz über GraphQL aktualisiert: $savedNote")
            savedNote
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id oder ${input.notebookId}")
            null
        }
    }

    /**
     * Mutation, um eine Notiz zu löschen.
     *
     * @param id Die ID der zu löschenden Notiz als String.
     * @return `true`, wenn die Notiz gelöscht wurde, sonst `false`.
     */
    @MutationMapping
    fun deleteNote(@Argument id: String): Boolean {
        logger.info("GraphQL-Mutation: deleteNote mit ID: $id")
        return try {
            val uuid = UUID.fromString(id)
            val exists = noteRepository.existsById(uuid)
            if (exists) {
                noteRepository.deleteById(uuid)
                logger.info("Notiz über GraphQL gelöscht: $id")
                true
            } else {
                logger.error("Notiz nicht gefunden: $id")
                false
            }
        } catch (e: IllegalArgumentException) {
            logger.error("Ungültiges UUID-Format: $id")
            false
        }
    }
}
