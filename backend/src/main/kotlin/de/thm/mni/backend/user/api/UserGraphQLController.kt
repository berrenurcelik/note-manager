package de.thm.mni.backend.user.api

import de.thm.mni.backend.user.api.dto.UserInput
import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import java.util.UUID

/**
 * GraphQL Controller for User operations
 * 
 * Provides GraphQL endpoints for querying and mutating User data.
 * Available at /graphql endpoint.
 */
@Controller
class UserGraphQLController(private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(UserGraphQLController::class.java)

    /**
     * Query to get all users
     * 
     * @return List of all users
     */
    @QueryMapping
    fun users(): List<User> {
        val auth = SecurityContextHolder.getContext().authentication
        logger.info("GraphQL query: users by authenticated user: ${auth?.name}")
        return userService.findAll()
    }

    /**
     * Query to get a user by ID
     * 
     * @param id User ID
     * @return User or null if not found
     */
    @QueryMapping
    fun user(@Argument id: String): User? {
        logger.info("GraphQL query: user with id: $id")
        return try {
            val uuid = UUID.fromString(id)
            userService.findById(uuid)
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid UUID format: $id")
            null
        }
    }

    /**
     * Query to get a user by username
     * 
     * @param username Username
     * @return User or null if not found
     */
    @QueryMapping
    fun userByUsername(@Argument username: String): User? {
        logger.info("GraphQL query: userByUsername with username: $username")
        return userService.findByUsername(username)
    }

    /**
     * Mutation to create a new user
     * 
     * @param input User input data
     * @return Created user
     */
    @MutationMapping
    fun createUser(@Argument input: UserInput): User {
        logger.info("GraphQL mutation: createUser with username: ${input.username}")
        val user = userService.create(input.username, input.firstName, input.lastName, input.email, input.password)
        logger.info("User created via GraphQL: $user")
        return user
    }

    /**
     * Mutation to update an existing user
     * 
     * @param id User ID
     * @param input Updated user data
     * @return Updated user or null if not found
     */
    @MutationMapping
    fun updateUser(@Argument id: String, @Argument input: UserInput): User? {
        logger.info("GraphQL mutation: updateUser with id: $id")
        return try {
            val uuid = UUID.fromString(id)
            userService.update(uuid, input.username, input.firstName, input.lastName, input.email, input.password)
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid UUID format: $id")
            null
        }
    }

    /**
     * Mutation to delete a user
     * 
     * @param id User ID
     * @return true if deleted, false if not found
     */
    @MutationMapping
    fun deleteUser(@Argument id: String): Boolean {
        logger.info("GraphQL mutation: deleteUser with id: $id")
        return try {
            val uuid = UUID.fromString(id)
            userService.delete(uuid)
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid UUID format: $id")
            false
        }
    }
}
