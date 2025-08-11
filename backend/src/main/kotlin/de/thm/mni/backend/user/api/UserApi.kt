package de.thm.mni.backend.user.api

import de.thm.mni.backend.user.api.dto.UserCreate
import de.thm.mni.backend.user.api.dto.UserResponse
import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserApi(private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(UserApi::class.java)

    @PostMapping
    fun register(@RequestBody req: UserCreate): ResponseEntity<UserResponse> {
        logger.info("Register endpoint called with username: ${req.username}")
        val user = userService.create(req.username, req.firstName, req.lastName, req.email, req.password)
        logger.info("User created: $user")
        return ResponseEntity.ok(UserResponse(user.id?.toString() ?: "", user.username, user.firstName, user.lastName, user.email))
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        logger.info("Get all users endpoint called")
        val users = userService.findAll()
        val userResponses = users.map { 
            UserResponse(it.id?.toString() ?: "", it.username, it.firstName, it.lastName, it.email) 
        }
        return ResponseEntity.ok(userResponses)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserResponse> {
        logger.info("Get user by id endpoint called")
        return try {
            val uuid = UUID.fromString(id)
            val user = userService.findById(uuid)
            if (user != null) {
                ResponseEntity.ok(UserResponse(user.id?.toString() ?: "", user.username, user.firstName, user.lastName, user.email))
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid UUID format provided")
            ResponseEntity.badRequest().build()
        }
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: String, @RequestBody req: UserCreate): ResponseEntity<UserResponse> {
        logger.info("Update user endpoint called with username: ${req.username}")
        return try {
            val uuid = UUID.fromString(id)
            val updatedUser = userService.update(uuid, req.username, req.firstName, req.lastName, req.email, req.password)
            if (updatedUser != null) {
                ResponseEntity.ok(UserResponse(updatedUser.id?.toString() ?: "", updatedUser.username, updatedUser.firstName, updatedUser.lastName, updatedUser.email))
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid UUID format provided")
            ResponseEntity.badRequest().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        logger.info("Delete user endpoint called")
        return try {
            val uuid = UUID.fromString(id)
            val deleted = userService.delete(uuid)
            if (deleted) {
                ResponseEntity.noContent().build()
            } else {
                ResponseEntity.notFound().build()
            }
        } catch (e: IllegalArgumentException) {
            logger.error("Invalid UUID format provided")
            ResponseEntity.badRequest().build()
        }
    }
}
