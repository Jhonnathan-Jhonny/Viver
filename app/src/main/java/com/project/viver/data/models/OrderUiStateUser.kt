package com.project.viver.data.models

import kotlinx.serialization.Serializable

@Serializable
data class OrderUiStateUser (
    val idAuth: String = "",
    val name: String? = "",
    val surname: String? = "",
    val email: String = "",
    val password: String = "",
    val sex: String? = "",
    val restrictions: String? = "",
    val weight: Int? = 0,
    val physicalActivityLevel: Int? = 0,
)