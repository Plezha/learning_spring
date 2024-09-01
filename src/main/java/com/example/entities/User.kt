package com.example.entities

import org.openapitools.model.UserResponse
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

// Такое на жаве я писать не буду.
data class User @JvmOverloads constructor(
    val uuid: UUID = UUID.randomUUID(),
    val email: String,
    @get:JvmName("getPassword_")
    val password: String,
    val role: UserRole = UserRole.USER,
    val firstName: String? = null,
    val lastName: String? = null,
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(role.toString()))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    fun toUserResponse(): UserResponse =
        UserResponse()
            .uuid(uuid)
            .email(email)
            .role(UserResponse.RoleEnum.valueOf(role.toString()))
            .firstName(firstName)
            .lastName(lastName)
}
