package de.thm.mni.backend.notebook.api

import de.thm.mni.backend.notebook.model.Notebook
import de.thm.mni.backend.notebook.repository.NotebookRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * REST-Controller für die Verwaltung von Notizbüchern.
 *
 * Dieser Controller stellt Endpunkte für CRUD-Operationen (Erstellen, Lesen, Aktualisieren, Löschen)
 * für [Notebook]-Entitäten bereit. Die Endpunkte sind unter dem Pfad `/api/notebooks` verfügbar und
 * erfordern eine Authentifizierung.
 *
 * @param notebookRepository Das Repository für den Zugriff auf Notizbuchdaten.
 */
@RestController
@RequestMapping("/api/notebooks")
@CrossOrigin(origins = ["http://localhost:4200"])
class NotebookApi(private val notebookRepository: NotebookRepository) {

    /**
     * Ruft alle Notizbücher für den authentifizierten Benutzer ab.
     *
     * @param authentication Das Authentifizierungsobjekt, das den aktuellen Benutzer identifiziert.
     * @return Ein [ResponseEntity] mit einer Liste von [Notebook]-Objekten.
     */
    @GetMapping
    fun getAllNotebooks(authentication: Authentication): ResponseEntity<List<Notebook>> {
        val userId = authentication.name
        val notebooks = notebookRepository.findByUserId(userId)
        return ResponseEntity.ok(notebooks)
    }

    /**
     * Ruft ein Notizbuch anhand seiner ID ab.
     *
     * @param id Die ID des abzurufenden Notizbuchs.
     * @return Ein [ResponseEntity] mit dem gefundenen [Notebook]-Objekt oder einem 404-Status,
     * wenn das Notizbuch nicht gefunden wird.
     */
    @GetMapping("/{id}")
    fun getNotebookById(@PathVariable id: UUID): ResponseEntity<Notebook> {
        val notebook = notebookRepository.findById(id)
        return if (notebook.isPresent) {
            ResponseEntity.ok(notebook.get())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Erstellt ein neues Notizbuch für den authentifizierten Benutzer.
     *
     * @param notebookRequest Das [CreateNotebookRequest]-Objekt, das die Daten für das neue Notizbuch enthält.
     * @param authentication Das Authentifizierungsobjekt des aktuellen Benutzers.
     * @return Ein [ResponseEntity] mit dem neu erstellten und gespeicherten [Notebook].
     */
    @PostMapping
    fun createNotebook(@RequestBody notebookRequest: CreateNotebookRequest, authentication: Authentication): ResponseEntity<Notebook> {
        val userId = authentication.name
        val notebook = Notebook(
            title = notebookRequest.title,
            image = notebookRequest.image,
            userId = userId
        )
        val savedNotebook = notebookRepository.save(notebook)
        return ResponseEntity.ok(savedNotebook)
    }

    /**
     * Aktualisiert ein bestehendes Notizbuch.
     *
     * Aktualisiert den Titel und das Bild eines Notizbuchs anhand seiner ID.
     *
     * @param id Die ID des zu aktualisierenden Notizbuchs.
     * @param notebookRequest Das [UpdateNotebookRequest]-Objekt mit den aktualisierten Daten.
     * @return Ein [ResponseEntity] mit dem aktualisierten [Notebook] oder einem 404-Status,
     * wenn das Notizbuch nicht gefunden wird.
     */
    @PutMapping("/{id}")
    fun updateNotebook(@PathVariable id: UUID, @RequestBody notebookRequest: UpdateNotebookRequest): ResponseEntity<Notebook> {
        val existingNotebook = notebookRepository.findById(id)
        return if (existingNotebook.isPresent) {
            val updatedNotebook = Notebook(
                id = id,
                title = notebookRequest.title,
                image = notebookRequest.image,
                userId = existingNotebook.get().userId
            )
            val savedNotebook = notebookRepository.save(updatedNotebook)
            ResponseEntity.ok(savedNotebook)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * Löscht ein Notizbuch anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Notizbuchs.
     * @return Ein [ResponseEntity] mit dem Status 204 (No Content) bei Erfolg oder einem
     * 404-Status, wenn das Notizbuch nicht gefunden wird.
     */
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

/**
 * Datenübertragungsobjekt (DTO) für das Erstellen eines Notizbuchs.
 *
 * @param title Der Titel des Notizbuchs.
 * @param image Der Dateiname oder Pfad des Bildes, das als Cover verwendet wird.
 */
data class CreateNotebookRequest(
    val title: String,
    val image: String
)

/**
 * Datenübertragungsobjekt (DTO) für die Aktualisierung eines Notizbuchs.
 *
 * @param title Der neue Titel des Notizbuchs.
 * @param image Der neue Dateiname oder Pfad für das Coverbild.
 */
data class UpdateNotebookRequest(
    val title: String,
    val image: String
)
