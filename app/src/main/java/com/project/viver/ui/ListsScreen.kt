package com.project.viver.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RestaurantMenu
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import java.text.SimpleDateFormat
import java.util.Locale


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
    Column(
        modifier = modifier
            .background(Color.White)
    ) {
        Text(
            text = "Meus Planos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
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
            .height(150.dp)
            .clickable {
                viewModel.getMealPlanById(id = mealPlan.id, context = context)
            }
            .padding(8.dp) // Espaçamento externo
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                clip = true
            ),
        shape = RoundedCornerShape(16.dp), // Bordas arredondadas
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E9) // Cor de fundo suave
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFD1E0D7),
                            Color(0xFFE8F5E9)
                        )
                    )
                )
                .padding(16.dp), // Espaçamento interno
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Ícone
            Icon(
                imageVector = Icons.Outlined.RestaurantMenu,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp), // Ícone maior
                tint = colorResource(id = R.color.First)
            )

            // Textos
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = mealPlan.name_meals,
                    style = MaterialTheme.typography.titleMedium, // Texto maior
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = formatDateTime(mealPlan.created_at),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

fun formatDateTime(dateTime: String): String {
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = parser.parse(dateTime)
        formatter.format(date ?: dateTime)
    } catch (e: Exception) {
        dateTime
    }
}


@Preview(showBackground = true)
@Composable
fun MealPlanCardPreview() {
    val sampleMealPlan = MealPlan(
        id = 1,
        name_meals = "Plano Alimentar Exemplo",
        created_at = "2024-01-29",
        user_id = "12345",
        breakfast = "Pão integral, café e frutas",
        lunch = "Arroz, feijão, frango grelhado e salada",
        afternoonSnack = "Iogurte natural com granola",
        dinner = "Sopa de legumes com torradas"
    )
//    ListsScreen(viewModel = viewModel(), context = LocalContext.current, navController = NavController(LocalContext.current))
    MealPlanCard(mealPlan = sampleMealPlan, navController = NavController(LocalContext.current), viewModel = viewModel(), context = LocalContext.current)
}