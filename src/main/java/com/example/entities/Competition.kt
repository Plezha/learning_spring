package com.example.entities

import java.util.*

data class Competition(
    val uuid: UUID = UUID.randomUUID(),
    val name: String,
    val owners: List<UUID>,
    )
