package de.thm.mni.backend.notebook.api

import de.thm.mni.backend.notebook.model.Notebook
import de.thm.mni.backend.notebook.repository.NotebookRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/notebooks")
@CrossOrigin(origins = ["http://localhost:4200"])
class NotebookApi(private val notebookRepository: NotebookRepository) {

    @GetMapping
    fun getAllNotebooks(authentication: Authentication): ResponseEntity<List<Notebook>> {
        val userId = authentication.name
        val notebooks = notebookRepository.findByUserId(userId)
        return ResponseEntity.ok(notebooks)
    }

    @GetMapping("/{id}")
    fun getNotebookById(@PathVariable id: UUID): ResponseEntity<Notebook> {
        val notebook = notebookRepository.findById(id)
        return if (notebook.isPresent) {
            ResponseEntity.ok(notebook.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    fun createNotebook(@RequestBody notebookRequest: CreateNotebookRequest, authentication: Authentication): ResponseEntity<Notebook> {
        val userId = authentication.name
        val notebook = Notebook(
            title = notebookRequest.title,
            userId = userId
        )
        val savedNotebook = notebookRepository.save(notebook)
        return ResponseEntity.ok(savedNotebook)
    }

    @PutMapping("/{id}")
    fun updateNotebook(@PathVariable id: UUID, @RequestBody notebookRequest: UpdateNotebookRequest): ResponseEntity<Notebook> {
        val existingNotebook = notebookRepository.findById(id)
        return if (existingNotebook.isPresent) {
            val updatedNotebook = Notebook(
                id = id,
                title = notebookRequest.title,
                userId = existingNotebook.get().userId
            )
            val savedNotebook = notebookRepository.save(updatedNotebook)
            ResponseEntity.ok(savedNotebook)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteNotebook(@PathVariable id: UUID): ResponseEntity<Unit> {
        return if (notebookRepository.existsById(id)) {
            notebookRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

data class CreateNotebookRequest(
    val title: String
)

data class UpdateNotebookRequest(
    val title: String
)
