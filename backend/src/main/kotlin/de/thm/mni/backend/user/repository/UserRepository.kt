package de.thm.mni.backend.user.repository

import de.thm.mni.backend.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

/**
 * Repository-Schnittstelle zur Verwaltung von [User]-Entitäten in der Datenbank.
 *
 * Erbt von [JpaRepository] und stellt Standard-CRUD-Operationen bereit.
 */
@Repository
interface UserRepository : JpaRepository<User, UUID> {
    /**
     * Findet einen Benutzer anhand seines eindeutigen Benutzernamens.
     *
     * @param username Der Benutzername des zu suchenden Benutzers.
     * @return Das [User]-Objekt, wenn es gefunden wird, andernfalls `null`.
     */
    fun findByUsername(username: String): User?
}
