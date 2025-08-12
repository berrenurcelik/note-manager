package de.thm.mni.backend.notebook

import de.thm.mni.backend.note.Note
import jakarta.persistence.*
import java.util.Date
import java.util.UUID

@Entity
@Table(name = "notebooks")
class Notebook(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    val title: String = "",
    val createdAt: Date = Date(),

    @OneToMany(mappedBy = "notebook", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val notes: List<Note> = emptyList(),

    val user_id: String = "",
    val coverImage: String = ""
)
