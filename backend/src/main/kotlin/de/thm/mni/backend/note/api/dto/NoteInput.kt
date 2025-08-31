package de.thm.mni.backend.note.api.dto

/**
 * Data Transfer Object (DTO) für Notiz-Eingabedaten.
 *
 * Diese Klasse wird verwendet, um Eingabedaten für die Erstellung und Aktualisierung
 * von Notizen über GraphQL-Mutationen zu empfangen.
 *
 * @property title Der Titel der Notiz.
 * @property content Der Inhalt der Notiz.
 * @property notebookId Die ID des übergeordneten Notizbuchs als String.
 */
data class NoteInput(
    val title: String,
    val content: String,
    val notebookId: String
)
