package de.thm.mni.backend.user.service

import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {
    
    fun create(username: String, password: String): User {
        val user = User(username = username, password = password, roles = listOf("USER"))
        return userRepository.save(user)
    }

    fun find(id: UUID): User? = userRepository.findById(id).orElse(null)
    fun findById(id: UUID): User? = userRepository.findById(id).orElse(null)
    fun findByUsername(username: String): User? = userRepository.findByUsername(username)
    fun findAll(): List<User> = userRepository.findAll()

    fun update(id: UUID, username: String, password: String): User? {
        val existingUser = userRepository.findById(id).orElse(null)
        return if (existingUser != null) {
            val updatedUser = User(
                id = existingUser.id,
                username = username,
                password = password,
                roles = existingUser.roles
            )
            userRepository.save(updatedUser)
        } else {
            null
        }
    }

    fun delete(id: UUID): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}
