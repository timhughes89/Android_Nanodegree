package com.udacity.popularMovies_p1.controller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import com.udacity.a02_popularmovies_p1.R;
import com.udacity.popularMovies_p1.model.Movie;
import com.udacity.popularMovies_p1.ui.RoundedCornerImageView;
import com.udacity.popularMovies_p1.utils.MovieUtils;

public class MovieImagesAdapter extends RecyclerView.Adapter<MovieImagesAdapter.ViewHolder> {

    private Context mContext;
    private List<Movie> mSimilarList;

    public MovieImagesAdapter(Context context, List<Movie> similarList) {
        mContext = context;
        mSimilarList = similarList;
    }

    @NonNull
    @Override
    public MovieImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_image_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieImagesAdapter.ViewHolder viewHolder, int position) {
        Movie movie = mSimilarList.get(position);

        viewHolder.textViewSimilarTitle.setText(movie.getTitle());
        viewHolder.textViewSimilarRating.setText(movie.getVoteAverage().toString());

        final String posterUrl = MovieUtils.buildPosterUrl(MovieUtils.API_POSTER_SIZE_W500, movie.getPosterPath());

        if (movie.getPosterPath() != null) {
            Glide.with(mContext)
                    .load(posterUrl)
                    .apply(RequestOptions.fitCenterTransform())
                    .into(viewHolder.imageViewSimilarImage);
        }
    }

    @Override
    public int getItemCount() {
        return mSimilarList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundedCornerImageView imageViewSimilarImage;

        private TextView textViewSimilarTitle;
        private TextView textViewSimilarRating;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewSimilarImage = (RoundedCornerImageView) itemView.findViewById(R.id.iv_movie_image);
            textViewSimilarTitle = (TextView)itemView.findViewById(R.id.tv_movie_title);
            textViewSimilarRating = (TextView)itemView.findViewById(R.id.tv_movie_name);
        }
    }
}
