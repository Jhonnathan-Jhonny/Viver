package com.project.viver.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.viver.R
import com.project.viver.ViverViewModel
import com.project.viver.data.models.DoubleButton
import com.project.viver.data.models.UserState

@SuppressLint("MutableCollectionMutableState")
@Composable
fun NewListScreen(
    viewModel: ViverViewModel,
    context: Context,
    onConfirmButtonClicked: () -> Unit = {},
    onCancelButtonClicked: () -> Unit = {}
) {
    val meal = viewModel.meal
    val totals by remember { mutableStateOf(viewModel.totals) }
    val userProfile by remember { mutableStateOf(viewModel.userProfile) }
    val userState by viewModel.uiState.collectAsState()

//    val weight = userProfile.value!!.weight
//    val activityLevel = userProfile.value!!.physical_activity_level

    val weight = 70.0
    val activityLevel = "Ativo"

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.First))
            .clip(
                RoundedCornerShape(
                    topStart = 32.dp,
                    topEnd = 32.dp
                )
            )
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Plano Alimentar",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Exibir refeições com seus horários
            meal.forEachIndexed { index, item ->
                val mealTime = when (index) {
                    0 -> "Lanche da manhã"
                    1 -> "Almoço"
                    2 -> "Lanche da tarde"
                    else -> "Janta"
                }

                Text(
                    text = "$mealTime:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                // Exibir os itens da refeição
                item.forEach { foodItem ->
                    Text(
                        text = foodItem,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Exibir totais
//            Text(
//                text = "Totais: ${totals["calories"]?.toInt()}kcal, " +
//                        "${totals["protein"]?.toInt()}g P, " +
//                        "${totals["fat"]?.toInt()}g G, " +
//                        "${totals["carbs"]?.toInt()}g C",
//                style = MaterialTheme.typography.bodyLarge,
//                fontWeight = FontWeight.Bold
//            )

            Spacer(modifier = Modifier.height(100.dp))

            DoubleButton(
                onClickCancel = {
                    onCancelButtonClicked()
                },
                onClickConfirm = {
                     viewModel.saveMealPlanToDatabase(
                        mealPlan = viewModel.meal,
                        totals = viewModel.totals,
                        context = context
                    )
                    if (userState is UserState.Success) {
                        onConfirmButtonClicked()
                    }
                },
                nameConfirmButton = "Salvar",
                isLoading = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewListScreenPreview() {
    NewListScreen(ViverViewModel(), LocalContext.current)
}
