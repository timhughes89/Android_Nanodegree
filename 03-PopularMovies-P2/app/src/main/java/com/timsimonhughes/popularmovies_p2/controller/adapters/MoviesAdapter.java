package com.timsimonhughes.popularmovies_p2.controller.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import com.timsimonhughes.popularmovies_p2.R;
import com.timsimonhughes.popularmovies_p2.model.Movie;
import com.timsimonhughes.popularmovies_p2.ui.OnItemClickListener;
import com.timsimonhughes.popularmovies_p2.utils.MovieUtils;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Movie> mMovieList;
    private OnItemClickListener mOnItemClickListener;

    public MoviesAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_movie_card, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MyViewHolder holder, final int position) {

        final Movie movie = mMovieList.get(position);

        String posterUrl = MovieUtils.buildPosterUrl(MovieUtils.API_POSTER_SIZE_W500, movie.getPosterpath());
        String vote = Double.toString(movie.getVoteAverage());

        holder.mTextViewMovieTitle.setText(mMovieList.get(position).getTitle());
        holder.mTextViewMovieRating.setText(vote);

        Glide.with(mContext)
                .load(posterUrl)
                .into(holder.mImageViewPoster);

        ViewCompat.setTransitionName(holder.mImageViewPoster, movie.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), holder.mImageViewPoster, movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList = movieList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewMovieTitle, mTextViewMovieRating;
        ImageView mImageViewPoster;

        MyViewHolder(View view) {
            super(view);
            mTextViewMovieTitle = (TextView) view.findViewById(R.id.tv_movie_title);
            mTextViewMovieRating = (TextView) view.findViewById(R.id.tv_vote_average);
            mImageViewPoster = (ImageView) view.findViewById(R.id.iv_movie_poster);
        }
    }
}
