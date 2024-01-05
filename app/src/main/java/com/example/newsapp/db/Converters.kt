package com.example.newsapp.db

import androidx.room.TypeConverters
import com.example.newsapp.models.Source
import com.google.gson.Gson

class Converters {

    @TypeConverters
    fun fromSource(source: Source): String {
        return Gson().toJson(source)
    }

    @TypeConverters
    fun toSource(json: String): Source {
        return Gson().fromJson(json, Source::class.java)
    }
}