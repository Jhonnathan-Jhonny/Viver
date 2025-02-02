package com.project.viver.data.models

import kotlinx.serialization.Serializable

@Serializable
data class MealPlan(
    val id: Int,
    var name_meals: String,
    val created_at: String,
    val user_id: String,
    val breakfast: String,
    val lunch: String,
    val afternoonSnack: String,
    val dinner: String
)