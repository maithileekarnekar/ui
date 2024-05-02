package com.androidtime.rinzify.homescreen.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidtime.rinzify.R
import com.androidtime.rinzify.databinding.ReviewsViewBinding
import com.androidtime.rinzify.homescreen.models.Review

class ReviewsAdpater(
    private val reviewsList: ArrayList<Review>,
) : RecyclerView.Adapter<ReviewsAdpater.ReviewsViewHolder>() {

    inner class ReviewsViewHolder(
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        val reviewsViewBinding: ReviewsViewBinding

        init {
            reviewsViewBinding = ReviewsViewBinding.bind(view)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ReviewsViewHolder {
        return ReviewsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.reviews_view, parent, false
            )
        )
    }

    override fun getItemCount(): Int = reviewsList.size

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {

        holder.reviewsViewBinding.review = reviewsList[position]

    }
}