package de.thm.mni.backend.note.model

import com.fasterxml.jackson.annotation.JsonIgnore
import de.thm.mni.backend.notebook.model.Notebook
import jakarta.persistence.*
import java.util.Date
import java.util.UUID

/**
 * Repräsentiert eine Notiz als JPA-Entität.
 *
 * Eine Notiz gehört zu einem bestimmten Benutzer ([userId]) und kann optional einem
 * Notizbuch ([notebookId]) zugeordnet sein.
 */
@Entity
@Table(name = "notes")
class Note(
    /**
     * Die eindeutige ID der Notiz.
     */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    /**
     * Der Titel der Notiz.
     */
    val title: String = "",

    /**
     * Der Zeitstempel, wann die Notiz erstellt wurde.
     */
    val createdAt: Date = Date(),

    /**
     * Der Zeitstempel des letzten Updates der Notiz.
     */
    val modifiedAt: Date = Date(),

    /**
     * Der Inhalt der Notiz.
     */
    val content: String = "",

    /**
     * Die ID des Benutzers, dem diese Notiz gehört.
     */
    val userId: String = "",

    /**
     * Die ID des Notizbuchs, dem diese Notiz zugeordnet ist.
     *
     * Dies ist die Spalte in der Datenbanktabelle. `insertable` und `updatable`
     * sind auf `false` gesetzt, da die Zuordnung über das `@ManyToOne`-Feld verwaltet wird.
     */
    @Column(name = "notebook_id", insertable = false, updatable = false)
    val notebookId: UUID? = null,

    /**
     * Die Beziehung zum Notizbuch, zu dem diese Notiz gehört.
     *
     * `@ManyToOne` definiert eine viele-zu-eins-Beziehung. `FetchType.LAZY`
     * bedeutet, dass das Notizbuch erst geladen wird, wenn explizit darauf zugegriffen wird.
     * `@JoinColumn` gibt die Fremdschlüsselspalte an.
     * `@JsonIgnore` stellt sicher, dass dieses Feld bei der Serialisierung zu JSON ignoriert wird,
     * um Endlosschleifen zu vermeiden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notebook_id")
    @JsonIgnore
    val notebook: Notebook? = null
)
