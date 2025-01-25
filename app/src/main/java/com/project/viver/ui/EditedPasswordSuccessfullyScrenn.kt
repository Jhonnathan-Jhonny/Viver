package com.project.viver.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.data.models.SingleButton

@Composable
fun EditedPasswordSuccessfullyScreen(
    onOkButtonClick: () -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 150.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.checked),
            contentDescription = "Senha alterada com sucesso",
            modifier = Modifier
                .size(height = 200.dp, width = 130.dp)
        )
        Text(
            text = "Senha nova definida!",
            color = colorResource(id = R.color.First),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Volte para o menu para entrar em sua conta usando a nova senha",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.on_secondary_container_dark),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(height = 50.dp, width = 220.dp)

        )
        SingleButton(
            onClick = {onOkButtonClick()},
            isLoading = false,
            buttonName = "Ok",
            colorButton = colorResource(id = R.color.First),
            colorText = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditedPasswordSuccessfullyScreenPreview(){
    EditedPasswordSuccessfullyScreen(onOkButtonClick = {})
}