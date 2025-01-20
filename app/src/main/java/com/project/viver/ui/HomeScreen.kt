package com.project.viver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.viver.R
import com.project.viver.ViverScreen
import com.project.viver.data.models.SingleButton

@Composable
fun HomeScreen(
    onNewListButtonClicked: () -> Unit,
    onListsButtonClicked: () -> Unit,
    onProfileButtonClicked: () -> Unit
) {

    val isLoading by remember { mutableStateOf(false) }

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
                ) // Arredondamento nos cantos superiores
                .background(Color.White) // Cor da tela principal
                .padding(16.dp), // Padding interno, opcional
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            SingleButton(
                onClick = { onNewListButtonClicked() },
                isLoading = isLoading,
                buttonName = stringResource(R.string.nova_lista),
                colorButton = colorResource(id = R.color.First),
                colorText = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            SingleButton(
                onClick = { onListsButtonClicked() },
                isLoading = isLoading,
                buttonName = "Minhas listas",
                colorButton = colorResource(id = R.color.First),
                colorText = Color.White
            )

            Spacer(modifier = Modifier.height(200.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = NavController(LocalContext.current)
    HomeScreen({
        navController.navigate(ViverScreen.NewList.name) }, {
        navController.navigate(ViverScreen.Lists.name)
    }) { navController.navigate(ViverScreen.Profile.name) }
}