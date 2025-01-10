package com.project.viver.ui
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.project.viver.models.OrderUiStateUser
import com.project.viver.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ViverViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiStateUser())
    val uiState: StateFlow<OrderUiStateUser> = _uiState.asStateFlow()

    fun isEmailAvailable(email: String, onResult: (Boolean) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = supabase
                    .from("users")
                    .select {
                        filter {
                            eq("email", email)
                        }
                    }
                    .decodeSingleOrNull<OrderUiStateUser>()

                onResult(response == null)
            } catch (e: Exception) {
                if (e.message?.contains("no rows") == true) {
                    onResult(true)
                } else {
                    Log.e("SupabaseError", "Erro ao verificar email: ${e.message}")
                    onResult(false)
                }
            }
        }
    }

    private fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun saveUserToSupabase(user: OrderUiStateUser) {
        viewModelScope.launch(Dispatchers.IO) {
            val hashedPassword = hashPassword(user.password)
            try {
                val response = supabase
                    .from("users")
                    .insert(
                        mapOf(
                            "name" to user.name,
                            "surname" to user.surname,
                            "email" to user.email,
                            "password" to hashedPassword,
                            "sex" to user.sex
                        )
                    )

                val decodedResponse = response.decodeAs<OrderUiStateUser>()
                Log.d("SupabaseResponse", "Decoded Response: $decodedResponse")
            } catch (e: Exception) {
                Log.e("SupabaseError", "Error during insert or deserialization: ${e.message}")
            }
        }
    }

    private fun checkPassword(inputPassword: String, storedHash: String): Boolean {
        return BCrypt.verifyer().verify(inputPassword.toCharArray(), storedHash.toCharArray()).verified
    }


}

//class ViverViewModel : ViewModel() {
//
//    private val client = OkHttpClient()
//
//    private fun generateVerificationToken(): String {
//        return UUID.randomUUID().toString()
//    }
//
//    private fun sendVerificationEmail(email: String, token: String) {
//        val emailBody = """
//        {
//            "From": "jhonnathan.rodrigues@aluno.uepb.edu.br",
//            "To": "$email",
//            "Subject": "Verificação de E-mail",
//            "HtmlBody": "<p>Clique no link para verificar seu e-mail: <a href='https://your-app.com/verify?token=$token'>Verificar</a></p>"
//        }
//        """.trimIndent()
//
//        val request = Request.Builder()
//            .url("https://api.postmarkapp.com/email")
//            .addHeader("X-Postmark-Server-Token", "084df19e-30fb-4c1a-8d0e-aa995e0b5719")
//            .post(emailBody.toRequestBody("application/json".toMediaType()))
//            .build()
//
//        try {
//            client.newCall(request).execute().use { response ->
//                if (!response.isSuccessful) {
//                    throw Exception("Erro ao enviar e-mail: ${response.body?.string()}")
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("EmailError", "Erro ao enviar e-mail: ${e.message}")
//        }
//    }
//
//    suspend fun verifyUser(token: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            try {
//                val response = supabase
//                    .from("user_verification")
//                    .select {
//                        filter {
//                            eq("token", token)
//                        }
//                    }
//                val user = response.decodeAs<Map<String, Any>>()
//
//                // Mover o usuário para a tabela `users`
//                supabase
//                    .from("users")
//                    .insert(user)
//
//                // Remover da tabela `user_verification`
//                supabase
//                    .from("user_verification")
//                    .delete {
//                        filter { eq("token", token) }
//                    }
//
//                true
//            } catch (e: Exception) {
//                Log.e("VerificationError", "Erro ao verificar usuário: ${e.message}")
//                false
//            }
//        }
//    }
//
//    fun saveUserForVerification(user: OrderUiStateUser) {
//        val token = generateVerificationToken()
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                supabase
//                    .from("user_verification")
//                    .insert(
//                        mapOf(
//                            "name" to user.name,
//                            "surname" to user.surname,
//                            "email" to user.email,
//                            "password" to user.password,
//                            "sex" to user.sex,
//                            "token" to token
//                        )
//                    )
//                sendVerificationEmail(user.email, token)
//            } catch (e: Exception) {
//                Log.e("SupabaseError", "Erro ao salvar usuário para verificação: ${e.message}")
//            }
//        }
//    }
//}