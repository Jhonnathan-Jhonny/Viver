package com.project.viver.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.viver.data.models.MealPlan

@Composable
fun SpecificListScreen(
    mealPlan: MealPlan?,
) {
    if (mealPlan == null) {
        Text("Plano alimentar não encontrado.")
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Exibir a data de criação
            Text(
                text = "Criado em: ${mealPlan.created_at}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Exibir as refeições
            Text(
                text = "Café da manhã:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = mealPlan.breakfast,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Almoço:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = mealPlan.lunch,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Lanche da tarde:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = mealPlan.afternoonSnack,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Jantar:",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = mealPlan.dinner,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpecificListScreenPreview() {
    val sampleMealPlan = MealPlan(
        id = 1,
        name_meals = "Plano Alimentar Exemplo",
        created_at = "2024-01-29",
        user_id = "12345",
        breakfast = "Pão integral, café e frutas",
        lunch = "Arroz, feijão, frango grelhado e salada",
        afternoonSnack = "Iogurte natural com granola",
        dinner = "Sopa de legumes com torradas"
    )

    SpecificListScreen(mealPlan = sampleMealPlan)
}
