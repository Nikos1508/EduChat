package com.example.educhat.ui.data.network

import com.example.educhat.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue

object SupabaseClient {
    var client = createSupabaseClient(
        supabaseKey = BuildConfig.supabaseKey,
        supabaseUrl = BuildConfig.supabaseUrl
    ){
        install(GoTrue)
    }
}