package com.example.demoliveproject.Repository


import com.example.demoliveproject.model.NewsResponse
import com.example.demoliveproject.model.NewsArticle
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class NewsRepository {

    suspend fun fetchNews(): List<NewsArticle> {
        return withContext(Dispatchers.IO) {
            val json = URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json").readText()
            val newsResponse = Gson().fromJson(json, NewsResponse::class.java)
            newsResponse.articles
        }
    }
}

