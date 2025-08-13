package de.thm.mni.backend.note.repository

import de.thm.mni.backend.note.Note
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NoteRepository : JpaRepository<Note, UUID> {
    fun findByNotebook_Id(notebookId: UUID): List<Note>
    fun findByUser_id(userId: String): List<Note>
}
