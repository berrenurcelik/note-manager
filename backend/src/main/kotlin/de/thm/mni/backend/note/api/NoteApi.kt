package de.thm.mni.backend.note.api

import de.thm.mni.backend.note.model.Note
import de.thm.mni.backend.note.repository.NoteRepository
import de.thm.mni.backend.notebook.repository.NotebookRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * REST-Controller für die Verwaltung von Notizen.
 *
 * Dieser Controller stellt Endpunkte für CRUD-Operationen (Erstellen, Lesen, Aktualisieren, Löschen)
 * für [Note]-Entitäten bereit. Die Endpunkte sind unter dem Pfad `/api/notes` verfügbar und
 * erfordern eine Authentifizierung.
 *
 * @param noteRepository Das Repository für den Zugriff auf Notizdaten.
 * @param notebookRepository Das Repository für den Zugriff auf Notizbuchdaten.
 */
@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = ["http://localhost:4200"])
class NoteApi(
    private val noteRepository: NoteRepository,
    private val notebookRepository: NotebookRepository
) {

    /**
     * Ruft alle Notizen für den authentifizierten Benutzer ab.
     *
     * Optional kann die Abfrage durch die Angabe einer [notebookId] gefiltert werden,
     * um nur Notizen innerhalb eines bestimmten Notizbuchs zu erhalten.
     *
     * @param authentication Das Authentifizierungsobjekt, das den aktuellen Benutzer identifiziert.
     * @param notebookId (Optional) Die ID des Notizbuchs, dessen Notizen abgerufen werden sollen.
     * @return Ein [ResponseEntity] mit einer Liste von [Note]-Objekten.
     */
    @GetMapping
    fun getAllNotes(
        authentication: Authentication,
        @RequestParam(required = false) notebookId: UUID?
    ): ResponseEntity<List<Note>> {
        val userId = authentication.name
        val notes = if (notebookId != null) {
            noteRepository.findByUserIdAndNotebookId(userId, notebookId)
        } else {
            noteRepository.findByUserId(userId)
        }
        return ResponseEntity.ok(notes)
    }

    /**
     * Ruft eine Notiz anhand ihrer ID ab.
     *
     * @param id Die ID der abzurufenden Notiz.
     * @return Ein [ResponseEntity] mit dem gefundenen [Note]-Objekt oder einem 404-Status,
     * wenn die Notiz nicht gefunden wird.
     */
    @GetMapping("/{id}")
    fun getNoteById(@PathVariable id: UUID): ResponseEntity<Note> {
        val note = noteRepository.findById(id)
        return if (note.isPresent) {
            ResponseEntity.ok(note.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Sucht nach Notizen basierend auf dem Titel für den authentifizierten Benutzer.
     *
     * Die Suche ist nicht case-sensitiv.
     *
     * @param title Der Titel oder ein Teil des Titels, nach dem gesucht werden soll.
     * @param authentication Das Authentifizierungsobjekt des aktuellen Benutzers.
     * @return Ein [ResponseEntity] mit einer Liste von passenden [Note]-Objekten.
     */
    @GetMapping("/search")
    fun searchNotes(
        @RequestParam title: String,
        authentication: Authentication
    ): ResponseEntity<List<Note>> {
        val userId = authentication.name
        val notes = noteRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title)
        return ResponseEntity.ok(notes)
    }

    /**
     * Erstellt eine neue Notiz.
     *
     * Die neue Notiz wird dem authentifizierten Benutzer zugeordnet. Optional kann sie
     * einem Notizbuch zugewiesen werden.
     *
     * @param noteRequest Das [CreateNoteRequest]-Objekt, das die Daten für die neue Notiz enthält.
     * @param authentication Das Authentifizierungsobjekt des aktuellen Benutzers.
     * @return Ein [ResponseEntity] mit der neu erstellten und gespeicherten [Note].
     */
    @PostMapping
    fun createNote(@RequestBody noteRequest: CreateNoteRequest, authentication: Authentication): ResponseEntity<Note> {
        val userId = authentication.name
        val notebook = noteRequest.notebookId?.let { notebookRepository.findById(it).orElse(null) }
        val note = Note(
            title = noteRequest.title,
            content = noteRequest.content,
            userId = userId,
            notebookId = noteRequest.notebookId,
            notebook = notebook
        )
        val savedNote = noteRepository.save(note)
        return ResponseEntity.ok(savedNote)
    }

    /**
     * Aktualisiert eine bestehende Notiz.
     *
     * Aktualisiert den Titel, den Inhalt und optional das Notizbuch einer Notiz anhand ihrer ID.
     * Wenn kein neues Notizbuch angegeben ist, wird die bestehende Zuordnung beibehalten.
     *
     * @param id Die ID der zu aktualisierenden Notiz.
     * @param noteRequest Das [UpdateNoteRequest]-Objekt mit den aktualisierten Daten.
     * @return Ein [ResponseEntity] mit der aktualisierten [Note] oder einem 404-Status,
     * wenn die Notiz nicht gefunden wird.
     */
    @PutMapping("/{id}")
    fun updateNote(@PathVariable id: UUID, @RequestBody noteRequest: UpdateNoteRequest): ResponseEntity<Note> {
        val existingNote = noteRepository.findById(id)
        return if (existingNote.isPresent) {
            val existing = existingNote.get()
            val notebookId = noteRequest.notebookId ?: existing.notebookId
            val notebook = notebookId?.let { notebookRepository.findById(it).orElse(null) }
            
            val updatedNote = Note(
                id = id,
                title = noteRequest.title,
                content = noteRequest.content,
                userId = existing.userId,
                notebookId = notebookId,
                notebook = notebook,
                modifiedAt = java.util.Date()
            )
            val savedNote = noteRepository.save(updatedNote)
            ResponseEntity.ok(savedNote)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Löscht eine Notiz anhand ihrer ID.
     *
     * @param id Die ID der zu löschenden Notiz.
     * @return Ein [ResponseEntity] mit dem Status 204 (No Content) bei Erfolg oder einem
     * 404-Status, wenn die Notiz nicht gefunden wird.
     */
    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable id: UUID): ResponseEntity<Unit> {
        return if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

/**
 * Datenübertragungsobjekt (DTO) für das Erstellen einer Notiz.
 *
 * @param title Der Titel der Notiz.
 * @param content Der Inhalt der Notiz.
 * @param notebookId Die optionale ID des Notizbuchs, dem die Notiz zugeordnet werden soll.
 */
data class CreateNoteRequest(
    val title: String,
    val content: String,
    val notebookId: UUID? = null
)

/**
 * Datenübertragungsobjekt (DTO) für die Aktualisierung einer Notiz.
 *
 * @param title Der neue Titel der Notiz.
 * @param content Der neue Inhalt der Notiz.
 * @param notebookId Die ID des Notizbuchs, dem die Notiz zugeordnet werden soll. Wenn null, wird die bestehende Zuordnung beibehalten.
 */
data class UpdateNoteRequest(
    val title: String,
    val content: String,
    val notebookId: UUID? = null
)
