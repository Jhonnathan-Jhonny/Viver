package com.project.viver.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project.viver.R
import com.project.viver.ViverScreen
import com.project.viver.ViverViewModel
import kotlinx.coroutines.delay

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun InitialLogoScreen(
    navController: NavHostController,
    viewModel: ViverViewModel,
    context: Context
) {
    // Tela inicial com a logo que aparece por 4 segundos
    LaunchedEffect(key1 = true) {
        delay(2000)
        if (viewModel.checkIfUserLoggedIn(context)) {
            navController.navigate(ViverScreen.Home.name)
            return@LaunchedEffect
        }
        navController.navigate(ViverScreen.StartOrder.name)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.colorful_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(300.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InitialLogoScreenPreview() {
    val navController = rememberNavController()
    val viewModel = ViverViewModel()
    InitialLogoScreen(
        navController = NavHostController(LocalContext.current),
        context = navController.context,
        viewModel = viewModel
    )
}