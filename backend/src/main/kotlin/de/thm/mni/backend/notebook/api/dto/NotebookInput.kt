package de.thm.mni.backend.notebook.api.dto

/**
 * Data Transfer Object (DTO) für Notizbuch-Eingabedaten.
 *
 * Diese Klasse wird verwendet, um Eingabedaten für die Erstellung und Aktualisierung
 * von Notizbüchern über GraphQL-Mutationen zu empfangen.
 *
 * @property title Der Titel des Notizbuchs.
 * @property image Der URL oder Pfad des Bildes für das Notizbuch (optional).
 */
data class NotebookInput(
    val title: String,
    val image: String? = null
)
