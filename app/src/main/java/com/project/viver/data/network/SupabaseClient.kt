package com.project.viver.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseClient {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://oyzhnayzidgozcfmvrvb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im95emhuYXl6aWRnb3pjZm12cnZiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjE3NDgxMDgsImV4cCI6MjA3NzMyNDEwOH0.gshBgv60jT2QNwOtxHYrwB_XnO52QiSjl_imi1YUQ9Y"
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
