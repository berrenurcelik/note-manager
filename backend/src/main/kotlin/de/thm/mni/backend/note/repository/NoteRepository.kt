package de.thm.mni.backend.note.repository

import de.thm.mni.backend.note.model.Note
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Repository-Schnittstelle für den Zugriff auf Notizdaten.
 *
 * Diese Schnittstelle erweitert [JpaRepository] und bietet grundlegende CRUD-Operationen
 * für die [Note]-Entität. Zusätzlich werden benutzerdefinierte Abfragemethoden definiert,
 * um Notizen basierend auf verschiedenen Kriterien abzurufen.
 */
interface NoteRepository : JpaRepository<Note, UUID> {
    /**
     * Sucht alle Notizen, die zu einem bestimmten Notizbuch gehören.
     *
     * @param notebookId Die ID des Notizbuchs.
     * @return Eine Liste von Notizen.
     */
    fun findByNotebook_Id(notebookId: UUID): List<Note>

    /**
     * Sucht alle Notizen, die zu einem bestimmten Notizbuch gehören.
     *
     * @param notebookId Die ID des Notizbuchs.
     * @return Eine Liste von Notizen.
     */
    fun findByNotebookId(notebookId: UUID): List<Note>

    /**
     * Sucht alle Notizen, die einem bestimmten Benutzer gehören.
     *
     * @param userId Die ID des Benutzers.
     * @return Eine Liste von Notizen.
     */
    fun findByUserId(userId: String): List<Note>

    /**
     * Sucht alle Notizen, die einem bestimmten Benutzer gehören und sich in einem
     * bestimmten Notizbuch befinden.
     *
     * @param userId Die ID des Benutzers.
     * @param notebookId Die ID des Notizbuchs.
     * @return Eine Liste von Notizen.
     */
    fun findByUserIdAndNotebookId(userId: String, notebookId: UUID): List<Note>

    /**
     * Sucht alle Notizen, deren Titel den angegebenen Suchbegriff enthält,
     * wobei die Groß-/Kleinschreibung ignoriert wird.
     *
     * @param title Der Suchbegriff im Titel.
     * @return Eine Liste von Notizen.
     */
    fun findByTitleContainingIgnoreCase(title: String): List<Note>

    /**
     * Sucht alle Notizen eines bestimmten Benutzers, deren Titel den angegebenen
     * Suchbegriff enthält, wobei die Groß-/Kleinschreibung ignoriert wird.
     *
     * @param userId Die ID des Benutzers.
     * @param title Der Suchbegriff im Titel.
     * @return Eine Liste von Notizen.
     */
    fun findByUserIdAndTitleContainingIgnoreCase(userId: String, title: String): List<Note>
}
