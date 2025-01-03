package com.project.viver.models

import kotlinx.serialization.Serializable

@Serializable
class User (
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val sex: String,
    val restrictions: String,
)