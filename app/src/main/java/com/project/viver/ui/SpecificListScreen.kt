package com.project.viver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.viver.R
import com.project.viver.ViverViewModel
import com.project.viver.data.models.MealPlan

@Composable
fun SpecificListScreen(viewModel: ViverViewModel) {
    val mealPlan = viewModel.mealPlan.value
    if (mealPlan == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Plano alimentar não encontrado.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.First))
                .clip(
                    RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp,
                    )
                )
        ){

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()) // Adiciona scroll se o conteúdo for grande
            ) {
                // Título do plano alimentar
                Text(
                    text = mealPlan.name_meals,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Data de criação
                Text(
                    text = "Criado em: ${mealPlan.created_at}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Café da manhã
                MealSection(
                    title = "Café da manhã",
                    icon = Icons.Default.BreakfastDining,
                    items = mealPlan.breakfast.split(",")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Almoço
                MealSection(
                    title = "Almoço",
                    icon = Icons.Default.LunchDining,
                    items = mealPlan.lunch.split(",")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Lanche da tarde
                MealSection(
                    title = "Lanche da tarde",
                    icon = Icons.Default.Coffee,
                    items = mealPlan.afternoonSnack.split(",")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Jantar
                MealSection(
                    title = "Jantar",
                    icon = Icons.Default.DinnerDining,
                    items = mealPlan.dinner.split(",")
                )
            }
        }
    }
}

@Composable
fun MealSection(
    title: String,
    icon: ImageVector,
    items: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título da seção com ícone
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colorResource(id = R.color.First),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // Lista de alimentos
            Column {
                items.forEach { item ->
                    Text(
                        text = item.trim(), // Remove espaços em branco
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
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

    SpecificListScreen(ViverViewModel())
}
