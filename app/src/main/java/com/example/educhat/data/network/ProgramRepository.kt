package com.example.educhat.data.network

import com.example.educhat.data.model.Program
import io.github.jan.supabase.postgrest.postgrest

object ProgramRepository {
    private val client = SupabaseClient.client

    suspend fun getPrograms(): List<Program> {
        return try {
            client
                .postgrest
                .from("programs")
                .select()
                .decodeList<Program>()
        } catch (e: Exception) {
            throw RuntimeException("Failed to fetch programs: ${e.message}")
        }
    }
}