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
 * - admin / 123
 * - john.d / 123
 * - jane.s / 123
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
            firstName = "Admin",
            lastName = "User",
            email = "admin@example.com",
            password = "123"
        )
        
        val user2 = User(
            username = "john.doe", 
            firstName = "John",
            lastName = "D",
            email = "john.d@example.com",
            password = "123"
        )
        
        val user3 = User(
            username = "jane.smith", 
            firstName = "Jane",
            lastName = "S",
            email = "jane.s@example.com",
            password = "123"
        )
        userRepository.saveAll(listOf(user1, user2, user3))
    }
}
