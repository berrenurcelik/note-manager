package de.thm.mni.backend.notebook.model

import com.fasterxml.jackson.annotation.JsonIgnore
import de.thm.mni.backend.note.model.Note
import jakarta.persistence.*
import java.util.Date
import java.util.UUID

/**
 * Repräsentiert ein Notizbuch als JPA-Entität.
 *
 * Ein Notizbuch gehört zu einem bestimmten Benutzer ([userId]) und kann eine Sammlung
 * von [Note]-Objekten enthalten. Es wird als Container für Notizen verwendet.
 */
@Entity
@Table(name = "notebooks")
class Notebook(
    /**
     * Die eindeutige ID des Notizbuchs.
     */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    /**
     * Der Titel des Notizbuchs.
     */
    val title: String = "",

    /**
     * Der Zeitstempel, wann das Notizbuch erstellt wurde.
     */
    val createdAt: Date = Date(),

    /**
     * Eine Liste der Notizen, die zu diesem Notizbuch gehören.
     *
     * Die `@OneToMany`-Annotation definiert die Eins-zu-viele-Beziehung zur [Note]-Entität.
     * `mappedBy` verweist auf das Feld "notebook" in der [Note]-Klasse, das die Beziehung steuert.
     * `cascade = [CascadeType.ALL]` stellt sicher, dass Notizen mit dem Notizbuch gespeichert,
     * aktualisiert und gelöscht werden.
     * `fetch = FetchType.LAZY` sorgt dafür, dass die Notizen erst bei Bedarf geladen werden,
     * um die Performance zu verbessern.
     */
    @OneToMany(mappedBy = "notebook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val notes: List<Note> = emptyList(),

    /**
     * Die ID des Benutzers, dem dieses Notizbuch gehört.
     */
    val userId: String = "",

    /**
     * Der Dateiname oder Pfad des Bildes, das als Cover für das Notizbuch dient.
     */
    val image: String = ""
)
