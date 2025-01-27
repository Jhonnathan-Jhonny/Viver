package com.project.viver.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.viver.ViverViewModel


@SuppressLint("MutableCollectionMutableState")
@Composable
fun NewListScreen(viewModel: ViverViewModel) {
    val meal = viewModel.meal
    val totals by remember { mutableStateOf(viewModel.totals) }
    val userProfile by remember { mutableStateOf(viewModel.userProfile) }

    val weight = userProfile.value!!.weight
    val activityLevel = userProfile.value!!.physical_activity_level

    LaunchedEffect(Unit) {
        viewModel.generateMealPlan(
            weight.toDouble(),
            when(activityLevel) {
                "Sedentário" -> 1.4
                "Ativo" -> 1.6
                else -> 1.7
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Plano Alimentar", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(16.dp))

        // Exibir refeições
        meal.forEach { item ->
            Text(text = item, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Exibir totais
        Text(
            text = "Totais: ${totals["calories"]?.toInt()}kcal, " +
                    "${totals["protein"]?.toInt()}g P, " +
                    "${totals["fat"]?.toInt()}g G, " +
                    "${totals["carbs"]?.toInt()}g C",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NewListScreenPreview() {
    NewListScreen(ViverViewModel())
}
