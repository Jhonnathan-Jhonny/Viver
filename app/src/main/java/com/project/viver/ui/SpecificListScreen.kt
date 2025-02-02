package com.project.viver.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.ViverViewModel
import com.project.viver.data.models.MealPlan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecificListScreen(
    onDeleteActionConfirmedButtonClicked: () -> Unit,
    viewModel: ViverViewModel,
    context: Context
) {

    val mealPlan = viewModel.mealPlan.value
    var isEditing by remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    var mealPlanName by remember { mutableStateOf(mealPlan!!.name_meals) }

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
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    // Título do plano alimentar
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
                                mealPlanName = mealPlan.name_meals // Mantém o nome padrão se estiver vazio
                                Toast.makeText(context, "Nome não pode ser vazio!", Toast.LENGTH_LONG).show()
                                isEditing = false
                            }
                            else{
                                viewModel.updateNameMealPlan(id = mealPlan.id, NewName = mealPlanName, context = context)
                                isEditing = false
                            }
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
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
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


                //Deleter plano alimentar
                Button(
                    onClick = { showDeleteDialog.value = true }, // Exibe o diálogo ao clicar
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Red,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Deletar plano alimentar!",
                        color = Color.Red,
                        fontSize = 15.sp
                    )
                }

                // Diálogo de confirmação
                if (showDeleteDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog.value = false }, // Fecha o diálogo ao clicar fora
                        title = {
                            Text(
                                text = "Confirmação",
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.Black
                            )
                        },
                        text = {
                            Text(
                                text = "Realmente deseja apagar permanentemente este plano alimentar?",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDeleteDialog.value = false
                                    viewModel.deleteMealPlan(id = mealPlan.id, context = context)
                                    onDeleteActionConfirmedButtonClicked()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text(text = "Sim", color = Color.White)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = { showDeleteDialog.value = false }, // Fecha o diálogo
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) {
                                Text(text = "Não", color = Color.White)
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        containerColor = Color.White
                    )
                }
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
            modifier = Modifier
                .padding(16.dp)
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

    SpecificListScreen({},ViverViewModel(),LocalContext.current)
}
