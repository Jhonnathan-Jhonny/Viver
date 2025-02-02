package com.project.viver.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project.viver.R
import com.project.viver.ViverScreen
import com.project.viver.ViverViewModel
import com.project.viver.data.models.LoadingIndicator
import com.project.viver.data.models.MealPlan
import com.project.viver.data.models.UserState


@Composable
fun ListsScreen(
    viewModel: ViverViewModel,
    context: Context,
    navController: NavController
) {
    val mealPlans = viewModel.mealPlans
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchMealPlans(context)
    }

    when (uiState) {
        is UserState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                LoadingIndicator()
            }
        }
        is UserState.Error -> {
            Text(
                text = (uiState as UserState.Error).message,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }
        else -> {

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
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .statusBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MealPlansGrid(
                        mealPlans = mealPlans,
                        navController = navController,
                        viewModel = viewModel,
                        context = context
                    )
                }
            }
        }
    }
}

@Composable
fun MealPlansGrid(
    mealPlans: List<MealPlan>,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ViverViewModel,
    context: Context
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .background(Color.White)
            .padding(8.dp)
    ) {

        items(mealPlans) { mealPlan ->
            MealPlanCard(
                mealPlan = mealPlan,
                navController = navController,
                viewModel = viewModel,
                context = context
            )
        }
    }
}

@Composable
fun MealPlanCard(
    mealPlan: MealPlan,
    navController: NavController,
    viewModel: ViverViewModel,
    context: Context
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState) {
        if (uiState is UserState.Success) {
            val successMessage = (uiState as UserState.Success).message
            if (successMessage == "Dados encontrados") {
                navController.navigate(ViverScreen.SpecificList.name) {
                    popUpTo(ViverScreen.Lists.name) { inclusive = false }
                }
            }
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                viewModel.getMealPlanById(id = mealPlan.id, context = context)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color(0xFFD1E0D7))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Fastfood,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 8.dp),
                tint = colorResource(id = R.color.First)
            )
            Column {
                Text(
                    text = mealPlan.name_meals,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp),
                    color = Color.Black
                )
                Text(
                    text = "Criado em: ${mealPlan.created_at}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MealPlanCardPreview() {
    MealPlanCard(
        mealPlan = MealPlan(
            id = 1,
            name_meals = "Dieta Balanceada",
            created_at = "2024-01-29",
            user_id = "123",
            breakfast = "Omelete e suco de laranja",
            lunch = "Frango grelhado com arroz e salada",
            afternoonSnack = "Iogurte com frutas",
            dinner = "Sopa de legumes"
        ),
        navController = NavController(LocalContext.current),
        viewModel = viewModel(),
        context = LocalContext.current
    )
}
