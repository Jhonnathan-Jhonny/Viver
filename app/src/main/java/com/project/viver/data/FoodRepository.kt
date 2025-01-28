package com.project.viver.data

import com.project.viver.data.models.Food

object FoodRepository {
    // Alimentos para lanches (manhã e tarde) divididos por categorias
    private val snackProteins = listOf(
        Food(name = "Iogurte natural", calories = 59, protein = 3.5, fat = 3.3, carbs = 4.7),
        Food(name = "Queijo cottage", calories = 98, protein = 11.1, fat = 4.3, carbs = 3.4),
        Food(name = "Ovo cozido", calories = 68, protein = 5.5, fat = 4.8, carbs = 0.6)
    )

    private val snackCarbs = listOf(
        Food(name = "Banana", calories = 89, protein = 1.1, fat = 0.3, carbs = 23.0),
        Food(name = "Maçã", calories = 52, protein = 0.3, fat = 0.2, carbs = 14.0),
        Food(name = "Pão integral", calories = 69, protein = 2.7, fat = 1.1, carbs = 11.6),
        Food(name = "Aveia", calories = 389, protein = 16.9, fat = 6.9, carbs = 66.3)
    )

    private val snackFats = listOf(
        Food(name = "Castanhas", calories = 607, protein = 15.0, fat = 55.0, carbs = 20.0),
        Food(name = "Amêndoas", calories = 576, protein = 21.1, fat = 49.9, carbs = 21.6),
        Food(name = "Pasta de amendoim", calories = 588, protein = 25.1, fat = 50.4, carbs = 20.1)
    )

    // Alimentos para refeições principais (almoço e jantar) divididos por categorias
    private val mainProteins = listOf(
        Food(name = "Frango grelhado", calories = 165, protein = 31.0, fat = 3.6, carbs = 0.0),
        Food(name = "Peixe assado", calories = 206, protein = 22.0, fat = 12.0, carbs = 0.0),
        Food(name = "Carne bovina magra", calories = 250, protein = 26.0, fat = 15.0, carbs = 0.0),
        Food(name = "Tofu", calories = 76, protein = 8.0, fat = 4.8, carbs = 1.9)
    )

    private val mainCarbs = listOf(
        Food(name = "Arroz integral", calories = 112, protein = 2.6, fat = 0.9, carbs = 23.0),
        Food(name = "Batata-doce", calories = 86, protein = 1.6, fat = 0.1, carbs = 20.0),
        Food(name = "Quinoa", calories = 120, protein = 4.1, fat = 1.9, carbs = 21.3),
        Food(name = "Feijão preto", calories = 91, protein = 5.5, fat = 0.5, carbs = 16.0)
    )

    private val mainFats = listOf(
        Food(name = "Azeite de oliva", calories = 119, protein = 0.0, fat = 13.5, carbs = 0.0),
        Food(name = "Abacate", calories = 160, protein = 2.0, fat = 15.0, carbs = 9.0),
        Food(name = "Sementes de chia", calories = 486, protein = 16.5, fat = 30.7, carbs = 42.1)
    )

    fun getSnackProteins(): List<Food> = snackProteins
    fun getSnackCarbs(): List<Food> = snackCarbs
    fun getSnackFats(): List<Food> = snackFats
    fun getMainProteins(): List<Food> = mainProteins
    fun getMainCarbs(): List<Food> = mainCarbs
    fun getMainFats(): List<Food> = mainFats
}