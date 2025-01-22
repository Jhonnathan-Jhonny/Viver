package com.project.viver.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.project.viver.R
import com.project.viver.ViverScreen
import com.project.viver.ViverViewModel
import com.project.viver.data.models.OrderUiStateUser
import com.project.viver.data.models.TextBox
import com.project.viver.data.models.UserState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ViverViewModel,
    context: Context,
    onEditPasswordButtonClicked: () -> Unit
) {
    val userProfile by viewModel.userProfile.observeAsState()
    val userState by viewModel.uiState.collectAsState()

    // Busca os dados do usuário
    LaunchedEffect(key1 = Unit) {
        viewModel.fetchUserProfile(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.First))
    ) {
        when (userState) {
            is UserState.Loading -> {
                // Indicador de carregamento
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
            is UserState.Error -> {
                // Exibe mensagem de erro
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (userState as UserState.Error).message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
            is UserState.Success -> {
                ProfileContent(userProfile = userProfile, context = context, onEditPasswordButtonClicked = onEditPasswordButtonClicked)
            }
        }
    }
}

@Composable
fun ProfileContent(
    userProfile: OrderUiStateUser?,
    onEditPasswordButtonClicked: () -> Unit,
    viewModel: ViverViewModel = ViverViewModel(),
    context: Context
) {
    var isEditing by remember { mutableStateOf(false) }
    val name = remember { mutableStateOf(userProfile?.name ?: "") }
    val surname = remember { mutableStateOf(userProfile?.surname ?: "") }
    val sex = remember { mutableStateOf(userProfile?.sex ?: "") }
    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(colorResource(id = R.color.First))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 32.dp
                    )
                )
                .background(Color.White)
                .zIndex(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.person_icon),
                contentDescription = "Avatar do usuário",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.on_secondary_container_dark))
            )
        }

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
                .padding(top = 95.dp)
        ) {
            // Nome e sobrenome do usuário lado a lado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = userProfile?.name ?: "Carregando...",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(end = 8.dp), // Espaço entre nome e sobrenome
                    textAlign = TextAlign.Center
                )
                Text(
                    text = userProfile?.surname ?: "Carregando...",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = {
                    if (isEditing) {
                        // Salvar alterações no banco de dados
                        val updatedUser = OrderUiStateUser(
                            name = name.value,
                            surname = surname.value,
                            sex = sex.value
                        )
                        viewModel.updateUserProfile(updatedUser, context)
                    }
                    // Alternar entre editar e salvar
                    isEditing = !isEditing
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
                    .height(40.dp)
                    .width(150.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Green
                ),
                border = BorderStroke(1.dp, colorResource(id = R.color.First))
            ) {
                Text(
                    text = if (isEditing) "Salvar" else "Editar perfil",
                    color = colorResource(id = R.color.First),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Informações do perfil
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                ProfileInfoRow(
                    label = stringResource(id = R.string.nome),
                    value = name.value,
                    isEditable = isEditing,
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onValueChange = { name.value = it }
                )
                ProfileInfoRow(
                    label = stringResource(id = R.string.sobrenome),
                    value = surname.value,
                    isEditable = isEditing,
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onValueChange = { surname.value = it }
                )
                ProfileInfoRow(
                    label = stringResource(id = R.string.e_mail),
                    value = userProfile?.email ?: "Não informado"
                )
                PasswordField(
                    label = stringResource(id = R.string.senha),
                    value = "********",
                    onEditClick = {onEditPasswordButtonClicked()}
                )

                ProfileInfoRow(
                    label = stringResource(id = R.string.sexo),
                    value = if (isEditing) sex.value else when (userProfile?.sex) {
                        "M" -> "Masculino"
                        "F" -> "Feminino"
                        else -> "Outro"
                    },
                    isEditable = isEditing,
                    onNext = { focusManager.clearFocus() },
                    onValueChange = { sex.value = it }
                )
                ProfileInfoRow(
                    label = stringResource(R.string.restri_es_alimentares),
                    value = userProfile?.restrictions ?: "Não informado"
                )
            }

            // Botão de desativar conta
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* Ação de desativar conta */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(text = "Desativar conta", color = Color.White)
            }
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Título "Senha" acima do campo
        Text(
            text = label,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp) // Espaçamento abaixo do título
        )

        // Caixa de entrada com o campo de senha
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = Color.White, // Fundo branco para o campo de senha
                    shape = RoundedCornerShape(12.dp) // Bordas arredondadas
                )
                .border(
                    width = 1.dp,
                    color = Color.Gray.copy(alpha = 0.3f), // Bordas sutis
                    shape = RoundedCornerShape(12.dp) // Bordas arredondadas também na borda
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // O campo de senha com os pontos representando a senha
                Text(
                    text = "•".repeat(value.length), // Usando os pontos diretamente aqui
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                    modifier = Modifier.weight(1f) // Faz o texto ocupar todo o espaço disponível
                )

                // Ícone de edição alinhado à direita
                IconButton(
                    onClick = { onEditClick() },
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit),
                        contentDescription = "Editar senha",
                        tint = colorResource(id = R.color.First)
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileInfoRow(
    label: String,
    value: String,
    isEditable: Boolean = false,
    onNext: (() -> Unit)? = null,
    onValueChange: (String) -> Unit = {}
) {
    var errorMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )

        if (isEditable) {
            if (label == stringResource(R.string.sexo)) {
                GenderSelectionRow(selectedValue = value, onValueChange = onValueChange)
            } else {
                TextBox(
                    value = value,
                    onValueChange = {
                        if (it.isNotBlank()) {
                            errorMessage = false
                            onValueChange(it)
                        } else {
                            errorMessage = true
                        }
                    },
                    label = label,
                    onNext = onNext,
                    errorMessage = errorMessage
                )
            }

            if (errorMessage) {
                Text(
                    text = "Campo não pode estar vazio.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        } else {
            Column (
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f), // Bordas sutis
                        shape = RoundedCornerShape(12.dp) // Bordas arredondadas também na borda
                    )
                    .fillMaxWidth()
                    .size(50.dp),
                verticalArrangement = Arrangement.Center,
            ){
                Text(
                    text = value,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

        }
    }
}

@Composable
fun GenderSelectionRow(
    selectedValue: String,
    onValueChange: (String) -> Unit
) {
    val color = colorResource(id = R.color.Third)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        GenderRadioButton(
            text = "M",
            isSelected = selectedValue == "M",
            onClick = { onValueChange("M") },
            color = color
        )

        Spacer(modifier = Modifier.width(16.dp))

        GenderRadioButton(
            text = "F",
            isSelected = selectedValue == "F",
            onClick = { onValueChange("F") },
            color = color
        )

        Spacer(modifier = Modifier.width(16.dp))

        GenderRadioButton(
            text = stringResource(R.string.outro),
            isSelected = selectedValue == "Outro",
            onClick = { onValueChange("Outro") },
            color = color
        )
    }
}

@Composable
fun GenderRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = color,
                unselectedColor = color
            )
        )
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileContent(
        userProfile = OrderUiStateUser(name = "Jhonnathan",
        surname = "Rodrigues", sex = "M"),
        context = LocalContext.current,
        onEditPasswordButtonClicked = {ViverScreen.ConfirmPassword.name})
//    ProfileScreen(ViverViewModel(), LocalContext.current)
}
