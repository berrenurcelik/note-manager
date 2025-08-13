package de.thm.mni.backend.notebook.model

import com.fasterxml.jackson.annotation.JsonIgnore
import de.thm.mni.backend.note.model.Note
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

    val userId: String = "",
    val coverImage: String = ""
)
