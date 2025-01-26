package com.project.viver.data.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderUiStateUser(
    val idAuth: String = "",
    val name: String? = "",
    val surname: String? = "",
    val email: String = "",
    var password: String = "",
    val sex: String? = "",
    val weight: String = "",
    val physical_activity_level: String? = "",
)