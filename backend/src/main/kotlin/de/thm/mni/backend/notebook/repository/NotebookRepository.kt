package de.thm.mni.backend.notebook.repository

import de.thm.mni.backend.notebook.model.Notebook
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotebookRepository : JpaRepository<Notebook, UUID> {
    fun findByUserId(userId: String): List<Notebook>
}
