package com.project.viver.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.project.viver.R
import com.project.viver.ViverViewModel
import com.project.viver.data.models.OrderUiStateUser
import com.project.viver.data.models.UserState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: ViverViewModel,
    context: Context
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
                ProfileContent(userProfile = userProfile, context = context)
            }
        }
    }
}

@Composable
fun ProfileContent(userProfile: OrderUiStateUser?, context: Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.First))
    ) {
        // Topo da tela com avatar
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
            // Nome do usuário
            Text(
                text = userProfile?.name ?: "Carregando...",
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )

            // Botão de editar perfil
            Button(
                onClick = { /* Ação de editar */ },
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
                    text = "Editar perfil",
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
                ProfileInfoRow(label = "Nome", value = userProfile?.name ?: "Não informado")
                ProfileInfoRow(label = "Sobrenome", value = userProfile?.surname ?: "Não informado")
                ProfileInfoRow(label = "E-mail", value = userProfile?.email ?: "Não informado")
                ProfileInfoRow(label = "Sexo", value = userProfile?.sex ?: "Não informado")
                ProfileInfoRow(
                    label = "Restrições alimentares",
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
fun ProfileInfoRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = label,
            color = Color.Black,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = value,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(ViverViewModel(), LocalContext.current)
}
