package com.timsimonhughes.popularmovies_p2.controller.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timsimonhughes.popularmovies_p2.R;
import com.timsimonhughes.popularmovies_p2.model.Review;


import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Review> mReviewList;

    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.mReviewList = reviewList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_review_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder (final MyViewHolder holder, int position) {
        Review review = mReviewList.get(position);

        holder.mTextViewReviewAuthor.setText(review.getAuthor());
        holder.mTextViewReviewBody.setText(review.getContent());

    }

    @Override
    public int getItemCount(){
        return mReviewList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewReviewAuthor, mTextViewReviewBody;

        MyViewHolder(View view){
            super(view);

            mTextViewReviewAuthor = view.findViewById(R.id.tv_review_author);
            mTextViewReviewBody = view.findViewById(R.id.tv_review_body);
        }
    }

}
