package de.thm.mni.backend.user.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,
    val username: String = "",
    val password: String = "",
    @ElementCollection
    val roles: List<String> = listOf()
)
