package com.project.viver.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseClient {
    val supabase = createSupabaseClient(
//        Request failed with Unable to resolve host "oyzhnayzidgozcfmvrvb.supabase.co": No address associated with hostname
        supabaseUrl = "https://kaenpcrufhqbozxkrsrq.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImthZW5wY3J1ZmhxYm96eGtyc3JxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzYyNTkyNDQsImV4cCI6MjA5MTgzNTI0NH0.jGIEscgFnuCKQS_tNEJPpmwyDP0vOmd-2Ql8XgiIp0Q"
    ) {
        install(Auth)
        install(Postgrest) {
            serializer = KotlinXSerializer(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    coerceInputValues = true 
                }
            )
        }
    }
}
