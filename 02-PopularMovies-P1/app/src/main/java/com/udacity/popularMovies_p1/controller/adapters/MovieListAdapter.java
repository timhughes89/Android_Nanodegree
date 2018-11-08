package com.udacity.popularMovies_p1.controller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import com.udacity.a02_popularmovies_p1.R;
import com.udacity.popularMovies_p1.ui.OnItemClickListener;
import com.udacity.popularMovies_p1.model.Movie;
import com.udacity.popularMovies_p1.utils.MovieUtils;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private Context mContext;
    private List<Movie> mMovieList;
    private OnItemClickListener mOnItemClickListener;

    public MovieListAdapter(Context context, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_movie_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieListAdapter.ViewHolder holder, int position) {
        final Movie movie = mMovieList.get(position);

        holder.movieTitle.setText(movie.getTitle());
        final String posterUrl = MovieUtils.buildPosterUrl(MovieUtils.API_POSTER_SIZE_W185, movie.getPosterPath());

        Glide.with(mContext)
                .load(posterUrl)
                .apply(RequestOptions.centerInsideTransform())
                .into(holder.movieImage);

        ViewCompat.setTransitionName(holder.movieImage, movie.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.getAdapterPosition(), holder.movieImage, movie);
            }
        });
        holder.itemView.setTag(movie);
    }


    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList = movieList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImage;
        private TextView movieTitle;

        ViewHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.tv_movie_name);
            movieImage = (ImageView) itemView.findViewById(R.id.iv_movie_image);

        }
    }
}
