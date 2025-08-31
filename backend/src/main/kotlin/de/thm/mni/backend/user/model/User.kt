package de.thm.mni.backend.user.model

import jakarta.persistence.*
import java.util.UUID

/**
 * Repräsentiert einen Benutzer als JPA-Entität.
 *
 * Diese Klasse wird für die Speicherung und den Abruf von Benutzerinformationen aus der Datenbank verwendet.
 */
@Entity
@Table(name = "users")
class User(
    /**
     * Die eindeutige ID des Benutzers.
     */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null,

    /**
     * Der einzigartige Benutzername.
     */
    val username: String = "",

    /**
     * Der Vorname des Benutzers.
     */
    val firstName: String = "",

    /**
     * Der Nachname des Benutzers.
     */
    val lastName: String = "",

    /**
     * Die E-Mail-Adresse des Benutzers.
     */
    val email: String = "",

    /**
     * Das Passwort des Benutzers. Hinweis: In einer Produktionsumgebung sollte dieses Feld
     * gehasht und niemals im Klartext gespeichert werden.
     */
    val password: String = ""
)
