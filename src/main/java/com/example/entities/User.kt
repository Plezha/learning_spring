package com.example.entities

import org.openapitools.model.UserResponse
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

// Такое на жаве я писать не буду.
data class User @JvmOverloads constructor(
    val uuid: UUID,
    val email: String,
    @get:JvmName("getPassword_")
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf()
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
            .firstName(firstName)
            .lastName(lastName)
}
