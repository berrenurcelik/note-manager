package de.thm.mni.backend.notebook.repository

import de.thm.mni.backend.notebook.model.Notebook
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

/**
 * Repository-Schnittstelle für den Zugriff auf Notizbuchdaten.
 *
 * Diese Schnittstelle erweitert [JpaRepository] und bietet grundlegende CRUD-Operationen
 * für die [Notebook]-Entität.
 */
interface NotebookRepository : JpaRepository<Notebook, UUID> {
    /**
     * Sucht alle Notizbücher, die einem bestimmten Benutzer gehören.
     *
     * @param userId Die ID des Benutzers.
     * @return Eine Liste von Notizbüchern.
     */
    fun findByUserId(userId: String): List<Notebook>
}
