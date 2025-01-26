package com.project.viver.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.viver.R
import com.project.viver.ViverViewModel
import com.project.viver.data.models.DoubleButton
import com.project.viver.data.models.OrderUiStateUser
import com.project.viver.data.models.UserState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationForNewListScreen(
    onCancelButtonClicked: () -> Unit = {},
    onNextButtonClicked: () -> Unit = {},
    viewModel: ViverViewModel,
    context: Context
) {
    // Carrega as informações do perfil do usuário
    val userProfile by viewModel.userProfile.observeAsState()

    // Chama a função para buscar as informações do perfil assim que a tela for exibida
    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile(context)
    }

    // Verifica se o userProfile foi carregado
    var weight by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("") }

    // Atualiza o valor do peso e do nível de atividade quando o perfil for carregado
    LaunchedEffect(userProfile) {
        userProfile?.let {
            weight = it.weight
            selectedOption = it.physical_activity_level ?: ""
        }
    }

    val uiState = viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.First))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    )
                )
                .background(Color.White)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Título
            Text(
                text = "Informações necessárias:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo para peso
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Text(
                    text = "Peso:",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    placeholder = {
                        Text(
                            text = "Digite seu peso",
                        )
                    },
                    modifier = Modifier
                        .height(50.dp)
                        .background(
                            Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(2.dp, colorResource(id = R.color.First), shape = RoundedCornerShape(8.dp)),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,  // Cor da borda sem foco
                        focusedBorderColor = colorResource(id = R.color.First),  // Cor da borda em foco (verde)
                        unfocusedTrailingIconColor = Color.Transparent, // Para remover qualquer ícone de borda
                        focusedTrailingIconColor = Color.Transparent,  // Para remover qualquer ícone de borda em foco
                        cursorColor = colorResource(id = R.color.First), // Cor do cursor (linha piscando verde)
                    ),
                    textStyle = TextStyle(
                        color = if (weight.isNotEmpty()) Color.Black else Color.Gray, // Cor do texto
                        fontSize = 16.sp // Opcional: ajuste o tamanho da fonte conforme necessário
                    )
                )

                Text(
                    text = "Kg",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nível de atividades físicas
            Text(
                text = "Nível de atividades física:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val options = listOf("Sedentário", "Ativo", "Muito ativo")
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = option }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = { selectedOption = option },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(id = R.color.First),
                            unselectedColor = Color.Gray
                        )
                    )
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                DoubleButton(
                    onClickCancel = { onCancelButtonClicked() },
                    onClickConfirm = {
                        // Verificação do peso
                        if (weight.isNotEmpty() && weight.toDoubleOrNull() != null) {
                            viewModel.updateUserInfoForNewList(
                                user = OrderUiStateUser(weight = weight, physical_activity_level = selectedOption),
                                context = context
                            )
                        } else {
                            // Mostrar mensagem de erro se o peso não for válido
                            Toast.makeText(context, "Peso inválido. Por favor, insira um número válido.", Toast.LENGTH_LONG).show()
                        }
                    },
                    isLoading = false
                )
            }
        }

        LaunchedEffect(uiState.value) {
            when (val currentState = uiState.value) {
                is UserState.Success -> {
                    if (currentState.message == "Peso e nível de atividade atualizados com sucesso.") {
                        Toast.makeText(context, currentState.message, Toast.LENGTH_LONG).show()
                        onNextButtonClicked() // Navegar para a próxima tela
                        viewModel.resetUserState() // Reset para estado neutro
                    }
                }
                is UserState.Error -> {
                    Toast.makeText(context, currentState.message, Toast.LENGTH_LONG).show()
                    viewModel.resetUserState() // Reset para estado neutro
                }
                else -> Unit
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InformationForNewListScreenPreview() {
    val viewModel: ViverViewModel = viewModel()
    val context: Context = LocalContext.current
    InformationForNewListScreen(viewModel = viewModel, context = context)
}
