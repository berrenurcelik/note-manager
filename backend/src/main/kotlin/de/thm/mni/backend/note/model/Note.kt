package de.thm.mni.backend.note.model

import com.fasterxml.jackson.annotation.JsonIgnore
import de.thm.mni.backend.notebook.model.Notebook
import jakarta.persistence.*
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "notes")
class Note(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    val title: String = "",
    val createdAt: Date = Date(),
    val modifiedAt: Date = Date(),
    val content: String = "",
    val userId: String = "",

    @Column(name = "notebook_id", insertable = false, updatable = false)
    val notebookId: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notebook_id")
    @JsonIgnore
    val notebook: Notebook? = null
)
