package com.example.newsapp.models

data class NewResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)