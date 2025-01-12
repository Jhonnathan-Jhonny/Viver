package com.project.viver.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://fyioedsjpwkrqpiynaxc.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ5aW9lZHNqcHdrcnFwaXluYXhjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzU4MjU1OTksImV4cCI6MjA1MTQwMTU5OX0.8tZZOGZ4BaR8WnxB518u39rWanq8cT0TMXoARCvrBZs"
    ) {
        install(Auth)
        install(Postgrest)
    }
}