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
    fun findByUsername(username: String): User? = userRepository.findByUsername(username)
}
