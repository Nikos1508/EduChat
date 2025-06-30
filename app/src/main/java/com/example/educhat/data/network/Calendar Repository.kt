package com.example.educhat.data.network

import com.example.educhat.data.model.CalendarEvent
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate

object CalendarRepository {
    suspend fun getEventsForMonth(year: Int, month: Int): List<CalendarEvent> {
        val start = LocalDate.of(year, month, 1)
        val end = start.plusMonths(1)

        return withContext(Dispatchers.IO) {
            val allEvents = SupabaseClient.client.postgrest
                .from("calendar")
                .select()
                .decodeList<CalendarEvent>()

            allEvents.filter { event ->
                val eventDate = LocalDate.parse(event.date)
                eventDate >= start && eventDate < end
            }
        }
    }
}