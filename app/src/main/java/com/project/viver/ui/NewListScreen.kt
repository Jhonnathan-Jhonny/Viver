package com.project.viver.ui

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.ViverViewModel
import com.project.viver.data.models.DoubleButton
import com.project.viver.data.models.UserState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MutableCollectionMutableState")
@Composable
fun NewListScreen(
    viewModel: ViverViewModel,
    context: Context,
    onConfirmButtonClicked: () -> Unit = {},
    onCancelButtonClicked: () -> Unit = {}
) {
    val userState by viewModel.uiState.collectAsState()

    LaunchedEffect(userState) {
        if (userState is UserState.Success) {
            val successMessage = (userState as UserState.Success).message
            if (successMessage == "Plano alimentar salvo com sucesso!") {
                onConfirmButtonClicked()
            }
        }
    }
    val meal = viewModel.meal
//    val totals by remember { mutableStateOf(viewModel.totals) }
    val userProfile by remember { mutableStateOf(viewModel.userProfile) }
    var mealPlanName by remember { mutableStateOf("Plano Alimentar") }
    var isEditing by remember { mutableStateOf(false) }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isEditing) {
                    // Campo de texto para edição
                    TextField(
                        value = mealPlanName,
                        onValueChange = { mealPlanName = it },
                        placeholder = { Text("Digite o nome do plano") },
                        modifier = Modifier
                            .height(50.dp)
                            .background(
                                Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                2.dp,
                                colorResource(id = R.color.First),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        singleLine = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.Transparent,  // Cor da borda sem foco
                            focusedBorderColor = colorResource(id = R.color.First),  // Cor da borda em foco (verde)
                            unfocusedTrailingIconColor = Color.Transparent, // Para remover qualquer ícone de borda
                            focusedTrailingIconColor = Color.Transparent,  // Para remover qualquer ícone de borda em foco
                            cursorColor = colorResource(id = R.color.First), // Cor do cursor (linha piscando verde)
                        ),
                        textStyle = TextStyle(
                            color = Color.Black, // Cor do texto
                            fontSize = 16.sp
                        )
                    )

                    // Botão de salvar
                    IconButton(onClick = {
                        if (mealPlanName.isBlank()) {
                            mealPlanName = "Plano Alimentar" // Mantém o nome padrão se estiver vazio
                        }
                        isEditing = false // Desativa o modo de edição
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Salvar",
                            tint = colorResource(id = R.color.First)
                        )
                    }
                } else {
                    // Texto exibido quando não está em modo de edição
                    Text(
                        text = mealPlanName,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )

                    // Botão de edição
                    IconButton(onClick = {
                        isEditing = true // Ativa o modo de edição
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = colorResource(id = R.color.First)
                        )
                    }
                }
            }

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
                        mealPlanName = mealPlanName,
                        context = context,
                    )
                    Toast.makeText(context, "Plano alimentar salvo com sucesso!", Toast.LENGTH_LONG).show()
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
