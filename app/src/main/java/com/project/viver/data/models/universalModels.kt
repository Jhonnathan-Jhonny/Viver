package com.project.viver.data.models

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.viver.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onNext: (() -> Unit)? = null, // Callback para avançar ao próximo campo
    errorMessage: Boolean? = null
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = colorResource(id = R.color.Third),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingIcon = { leadingIcon?.invoke() },
        trailingIcon = { trailingIcon?.invoke() },
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = colorResource(id = R.color.Third),
            focusedBorderColor = colorResource(id = R.color.Third),
            unfocusedBorderColor = if (errorMessage == true) Color.Red else colorResource(id = R.color.Third),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = if (onNext != null) ImeAction.Next else ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onNext?.invoke() ?: focusManager.moveFocus(FocusDirection.Down)
            },
            onDone = {
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
fun SingleButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    buttonName: String,
    colorButton: Color,
    colorText: Color
) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(colorButton)
            .border(
                1.dp,
                color = colorResource(id = R.color.First),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = colorText)
        } else {
            Text(
                text = buttonName,
                color = colorText,
                fontSize = 22.sp,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    TextBox(value = "", onValueChange = {}, label = "Email")
//    SingleButton(onClick = { /*TODO*/ }, isLoading = true, buttonName = "Login", colorResource(id = R.color.First), colorResource(id = R.color.First))
}



























