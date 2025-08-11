package de.thm.mni.backend.config

import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * DataLoader - Sample data generator for testing purposes
 * 
 * This class runs automatically when the Spring Boot application starts
 * and adds test users to the database. 
 * 
 * Test users:
 * - admin / admin123 (ADMIN, USER roles)
 * - user2 / 123 (USER role)
 * - user3 / 123 (USER role)
 */
@Component
class DataLoader(
    private val userRepository: UserRepository
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (userRepository.count() > 0) {
            return
        }

        val user1 = User(
            username = "admin", 
            password = "admin123", 
            roles = listOf("ADMIN", "USER")
        )
        
        val user2 = User(
            username = "user2", 
            password = "123", 
            roles = listOf("USER")
        )
        
        val user3 = User(
            username = "user3", 
            password = "123", 
            roles = listOf("USER")
        )
        userRepository.saveAll(listOf(user1, user2, user3))
    }
}
