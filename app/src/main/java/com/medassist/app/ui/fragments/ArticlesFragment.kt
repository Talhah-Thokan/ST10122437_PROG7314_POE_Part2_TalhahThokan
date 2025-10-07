package com.medassist.app.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medassist.app.R
import com.medassist.app.data.firebase.FirebaseRepository
import com.medassist.app.data.api.Article as ApiArticle
import com.medassist.app.ui.screens.articles.ArticleDetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Articles Fragment - Displays health articles with modern card layout
 */
class ArticlesFragment : Fragment() {
    
    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var articlesAdapter: ArticlesAdapter
    private lateinit var firebaseRepository: FirebaseRepository
    
    companion object {
        private const val TAG = "ArticlesFragment"
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_articles, container, false)
        
        firebaseRepository = FirebaseRepository()
        setupViews(view)
        loadArticles()
        
        Log.d(TAG, "ArticlesFragment created")
        
        return view
    }
    
    private fun setupViews(view: View) {
        articlesRecyclerView = view.findViewById(R.id.articlesRecyclerView)
    }
    
    private fun loadArticles() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = firebaseRepository.getArticles()
                result.onSuccess { articles ->
                    setupRecyclerView(articles)
                    Log.d(TAG, "Loaded ${articles.size} articles from Firebase")
                }.onFailure { exception ->
                    Log.e(TAG, "Failed to load articles", exception)
                    Toast.makeText(requireContext(), "Failed to load articles", Toast.LENGTH_SHORT).show()
                    // Fallback to sample articles
                    setupRecyclerView(getSampleArticles())
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading articles", e)
                // Fallback to sample articles
                setupRecyclerView(getSampleArticles())
            }
        }
    }
    
    private fun setupRecyclerView(articles: List<ApiArticle>) {
        articlesAdapter = ArticlesAdapter(articles) { article ->
            // Navigate to article detail
            val intent = Intent(requireContext(), ArticleDetailActivity::class.java)
            intent.putExtra("ARTICLE_TITLE", article.title)
            intent.putExtra("ARTICLE_AUTHOR", article.author)
            intent.putExtra("ARTICLE_CONTENT", article.content)
            intent.putExtra("ARTICLE_IMAGE", article.imageUrl)
            startActivity(intent)
        }
        
        articlesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        articlesRecyclerView.adapter = articlesAdapter
    }
    
    private fun getSampleArticles(): List<ApiArticle> {
        return listOf(
            ApiArticle(
                id = "1",
                title = "5 Essential Tips for Better Sleep",
                author = "Dr. Sarah Johnson",
                summary = "Sleep is crucial for overall health and well-being. Learn these proven techniques to improve your sleep quality...",
                content = "Learn essential techniques to improve your sleep quality and overall health. This comprehensive guide covers everything from sleep hygiene to relaxation techniques that can help you get the rest you need.",
                imageUrl = "https://via.placeholder.com/300x200/008B8B/FFFFFF?text=Sleep+Tips",
                date = "2025-01-15"
            ),
            ApiArticle(
                id = "2",
                title = "Understanding Heart Health",
                author = "Dr. Michael Chen",
                summary = "Your heart is your most important muscle. Discover the key factors that contribute to cardiovascular health...",
                content = "Everything you need to know about maintaining a healthy heart and preventing cardiovascular diseases. This article covers diet, exercise, and lifestyle changes that can significantly improve your heart health.",
                imageUrl = "https://via.placeholder.com/300x200/20B2AA/FFFFFF?text=Heart+Health",
                date = "2025-01-14"
            ),
            ApiArticle(
                id = "3",
                title = "Mental Health and Wellness",
                author = "Dr. Emily Rodriguez",
                summary = "Mental health is just as important as physical health. Explore practical strategies for managing stress...",
                content = "Practical strategies for managing stress and maintaining good mental health. Learn about mindfulness, meditation, and other techniques that can help you maintain emotional well-being.",
                imageUrl = "https://via.placeholder.com/300x200/006666/FFFFFF?text=Mental+Health",
                date = "2025-01-13"
            ),
            ApiArticle(
                id = "4",
                title = "Nutrition for Active Living",
                author = "Dr. James Wilson",
                summary = "Fuel your body with the right nutrients to support an active lifestyle. Discover the best foods...",
                content = "Discover the best foods to fuel your active lifestyle and maintain optimal health. This guide covers macronutrients, micronutrients, and meal planning strategies.",
                imageUrl = "https://via.placeholder.com/300x200/008B8B/FFFFFF?text=Nutrition",
                date = "2025-01-12"
            ),
            ApiArticle(
                id = "5",
                title = "Preventing Common Cold",
                author = "Dr. Lisa Thompson",
                summary = "Boost your immunity and avoid seasonal illnesses with these simple but effective prevention strategies...",
                content = "Simple steps to boost your immunity and avoid seasonal illnesses. Learn about vitamins, hygiene practices, and lifestyle changes that can help you stay healthy year-round.",
                imageUrl = "https://via.placeholder.com/300x200/20B2AA/FFFFFF?text=Cold+Prevention",
                date = "2025-01-11"
            )
        )
    }
}

/**
 * Data class for articles
 */
data class ApiArticle(
    val title: String,
    val author: String,
    val summary: String,
    val content: String,
    val imageUrl: String,
    val date: String
)

/**
 * Adapter for articles RecyclerView with modern card design
 */
class ArticlesAdapter(
    private val articles: List<ApiArticle>,
    private val onItemClick: (ApiArticle) -> Unit
) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>() {
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: android.widget.TextView = view.findViewById(R.id.articleTitle)
        val authorText: android.widget.TextView = view.findViewById(R.id.articleAuthor)
        val summaryText: android.widget.TextView = view.findViewById(R.id.articleSummary)
        val dateText: android.widget.TextView = view.findViewById(R.id.articleDate)
        val imageView: android.widget.ImageView = view.findViewById(R.id.articleImage)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article_modern, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        
        holder.titleText.text = article.title
        holder.authorText.text = "By ${article.author}"
        holder.summaryText.text = article.summary
        holder.dateText.text = article.date
        
        // TODO: Load image using Glide or Picasso
        // For now, we'll use a placeholder
        holder.imageView.setImageResource(R.drawable.ic_article_placeholder)
        
        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }
    
    override fun getItemCount(): Int = articles.size
}

