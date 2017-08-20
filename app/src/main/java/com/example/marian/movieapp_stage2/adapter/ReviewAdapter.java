package com.example.marian.movieapp_stage2.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marian.movieapp_stage2.R;
import com.example.marian.movieapp_stage2.model.Review;

import java.util.List;
/**
 * Created by Marian on 6/12/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MovieViewHolder> {

    private static List<Review> reviews;

    private static Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder
    {
        TextView reviewDetails;


        public MovieViewHolder(View v)
        {
            super(v);
            reviewDetails = (TextView) v.findViewById(R.id.review_details);

        }
    }

    public ReviewAdapter(Context context, List<Review> reviews)
    {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position)
    {
        holder.reviewDetails.setText(reviews.get(position).getContent());


    }

    @Override
    public int getItemCount()
    {
        return reviews.size();

    }
}