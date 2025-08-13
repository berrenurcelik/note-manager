package de.thm.mni.backend.note

import de.thm.mni.backend.notebook.Notebook
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
    val user_id: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notebook_id")
    val notebook: Notebook? = null
)
