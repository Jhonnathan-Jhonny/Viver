package com.project.viver.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.project.viver.R
import com.project.viver.data.models.DoubleButton
import com.project.viver.data.models.TextBox

@Composable
fun NewPasswordScreen(
    onConfirmButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit
){

    val confirmPassword = remember { mutableStateOf("") }
    val isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.nova_senha),
            fontSize = 24.sp,
            color = Color.Black
        )
        TextBox(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = stringResource(id = R.string.senha)
        )
        DoubleButton(
            onClickCancel = { onCancelButtonClicked() },
            onClickConfirm = { /*TODO*/},
            isLoading = isLoading)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewPasswordScreen() {
    val onConfirmButtonClicked = {}
    val onCancelButtonClicked = {}
    NewPasswordScreen(onConfirmButtonClicked = onConfirmButtonClicked, onCancelButtonClicked = onCancelButtonClicked)
}

