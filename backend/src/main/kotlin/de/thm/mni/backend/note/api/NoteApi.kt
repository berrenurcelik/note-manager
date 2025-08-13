package de.thm.mni.backend.note.api

import de.thm.mni.backend.note.model.Note
import de.thm.mni.backend.note.repository.NoteRepository
import de.thm.mni.backend.notebook.repository.NotebookRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = ["http://localhost:4200"])
class NoteApi(
    private val noteRepository: NoteRepository,
    private val notebookRepository: NotebookRepository
) {

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

    @GetMapping("/{id}")
    fun getNoteById(@PathVariable id: UUID): ResponseEntity<Note> {
        val note = noteRepository.findById(id)
        return if (note.isPresent) {
            ResponseEntity.ok(note.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/search")
    fun searchNotes(
        @RequestParam title: String,
        authentication: Authentication
    ): ResponseEntity<List<Note>> {
        val userId = authentication.name
        val notes = noteRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title)
        return ResponseEntity.ok(notes)
    }

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

    @PutMapping("/{id}")
    fun updateNote(@PathVariable id: UUID, @RequestBody noteRequest: UpdateNoteRequest): ResponseEntity<Note> {
        val existingNote = noteRepository.findById(id)
        return if (existingNote.isPresent) {
            val updatedNote = Note(
                id = id,
                title = noteRequest.title,
                content = noteRequest.content,
                userId = existingNote.get().userId,
                modifiedAt = java.util.Date()
            )
            val savedNote = noteRepository.save(updatedNote)
            ResponseEntity.ok(savedNote)
        } else {
            ResponseEntity.notFound().build()
        }
    }

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

data class CreateNoteRequest(
    val title: String,
    val content: String,
    val notebookId: UUID? = null
)

data class UpdateNoteRequest(
    val title: String,
    val content: String,
    val notebookId: UUID? = null
)
