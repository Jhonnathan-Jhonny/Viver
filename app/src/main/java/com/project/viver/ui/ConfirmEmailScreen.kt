@file:Suppress("UNUSED_EXPRESSION")

package com.project.viver.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.ViverAppTopBar1
import com.project.viver.ViverScreen
import com.project.viver.data.models.SingleButton
import com.project.viver.utils.ViverNavigationType

@Composable
fun ConfirmEmailScreen(
    onOkButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(370.dp))

        Image(
            painter = painterResource(id = R.drawable.email_confirm),
            contentDescription = "Email confirmado",
            modifier = Modifier
                .width(136.dp)
                .height(77.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "E-mail enviado!",
            color = colorResource(id = R.color.First),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Confirme seu e-mail verificando sua caixa de entrada ou spam.",
            color = colorResource(id = R.color.on_secondary_container_dark),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        SingleButton(
            onClick = onOkButtonClicked,
            isLoading = false,
            buttonName = "OK",
            colorButton = colorResource(id = R.color.First),
            colorText = Color.White
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(name = "Celular", device = Devices.PIXEL_4, showBackground = true)
@Composable
fun ConfirmEmailScreenPreview() {
    Scaffold(
        topBar = {
            ViverAppTopBar1(
                navigationType = ViverNavigationType.BOTTOM_NAVIGATION,
                currentScreen = ViverScreen.Home,
                canNavigateBack = false,
                navigateUp = {},
            )
        }
    ) { _ ->
        ConfirmEmailScreen(onOkButtonClicked = {}, )
    }
}