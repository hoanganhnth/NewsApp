package com.example.newsapp.db

import androidx.room.TypeConverter
import com.example.newsapp.models.Source
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(json: String): Source {
        return Gson().fromJson(json, Source::class.java)
    }
}