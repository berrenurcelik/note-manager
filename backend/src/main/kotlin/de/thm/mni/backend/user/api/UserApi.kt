package de.thm.mni.backend.user.api

import de.thm.mni.backend.user.api.dto.UserCreate
import de.thm.mni.backend.user.api.dto.UserResponse
import de.thm.mni.backend.user.model.User
import de.thm.mni.backend.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserApi(private val userService: UserService) {
    private val logger = LoggerFactory.getLogger(UserApi::class.java)

    @PostMapping
    fun register(@RequestBody req: UserCreate): ResponseEntity<UserResponse> {
        logger.info("Register endpoint called with username: ${req.username}")
        val user = userService.create(req.username, req.password)
        logger.info("User created: $user")
        return ResponseEntity.ok(UserResponse(user.id?.toString() ?: "", user.username, user.roles))
    }
}
