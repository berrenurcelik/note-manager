package de.thm.mni.backend.note.repository

import de.thm.mni.backend.note.model.Note
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NoteRepository : JpaRepository<Note, UUID> {
    fun findByNotebook_Id(notebookId: UUID): List<Note>
    fun findByNotebookId(notebookId: UUID): List<Note>
    fun findByUserId(userId: String): List<Note>
    fun findByUserIdAndNotebookId(userId: String, notebookId: UUID): List<Note>
    fun findByTitleContainingIgnoreCase(title: String): List<Note>
    fun findByUserIdAndTitleContainingIgnoreCase(userId: String, title: String): List<Note>
}
