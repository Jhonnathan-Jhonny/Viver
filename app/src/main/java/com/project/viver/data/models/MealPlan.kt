package com.project.viver.data.models

data class MealPlan(
    val name_meals: String,
    val breakfast: List<String>,
    val lunch: List<String>,
    val afternoonSnack: List<String>,
    val dinner: List<String>
)
