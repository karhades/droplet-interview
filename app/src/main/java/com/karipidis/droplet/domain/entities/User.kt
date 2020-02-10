package com.karipidis.droplet.domain.entities

data class User(
    val id: String,
    val avatar: String,
    val firstName: String,
    val lastName: String,
    val email: String
)