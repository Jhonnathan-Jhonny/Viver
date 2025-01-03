package com.project.viver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.compose.ViverTheme
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://fyioedsjpwkrqpiynaxc.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ5aW9lZHNqcHdrcnFwaXluYXhjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzU4MjU1OTksImV4cCI6MjA1MTQwMTU5OX0.8tZZOGZ4BaR8WnxB518u39rWanq8cT0TMXoARCvrBZs"
) {
    install(Auth)
    install(Postgrest)
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViverTheme {
                ViverApp()
            }
        }
    }
}
