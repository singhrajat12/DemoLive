package com.example.demoliveproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoliveproject.Adapter.NewsAdapter
import com.example.demoliveproject.model.NewsArticle
import com.example.demoliveproject.model.NewsResponse
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private var newsList: List<NewsArticle> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NewsAdapter(newsList) { article ->
            openArticleInBrowser(article.url)
        }

//        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val token = task.result
//                // Handle token if needed
//                println("FCM Token: $token")
//            } else {
//                println("Failed to retrieve FCM token: ${task.exception?.message}")
//            }
//        }


        recyclerView.adapter = adapter

        fetchData()

    }

    private fun fetchData() {
        val url = "https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json"
        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body()?.string()
                responseData?.let {
                    val gson = Gson()
                    val newsResponse = gson.fromJson(it, NewsResponse::class.java)

                    runOnUiThread {
                        newsList = newsResponse.articles
                        adapter.updateList(newsList)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle network failure here
                Log.e("API Call", "Failed to fetch data: ${e.message}")
            }
        })
    }


    private fun openArticleInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


}



