package de.thm.mni.backend.notebook.repository

import de.thm.mni.backend.notebook.Notebook
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotebookRepository : JpaRepository<Notebook, UUID> {
    fun findByUser_id(userId: String): List<Notebook>
}
