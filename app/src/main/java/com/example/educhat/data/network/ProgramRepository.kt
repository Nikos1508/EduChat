package com.example.educhat.data.network

import com.example.educhat.data.model.Program
import io.github.jan.supabase.postgrest.postgrest

object ProgramRepository {
    private val client = SupabaseClient.client

    suspend fun getPrograms(): List<Program> {
        return try {
            val result = client
                .postgrest
                .from("programs")
                .select()
                .decodeList<Program>()

            println("Fetched programs: $result")
            result
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Failed to fetch programs: ${e.message}")
        }
    }
}