package com.udacity.popularMovies_p1.controller.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.udacity.popularMovies_p1.model.Cast;
import com.udacity.popularMovies_p1.utils.MovieUtils;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private Context mContext;
    private List<Cast> mCastList;

    public CastAdapter(Context context, List<Cast> castList) {
        mContext = context;
        mCastList = castList;
    }

    @NonNull
    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_cast_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.ViewHolder viewHolder, int position) {
        Cast cast = mCastList.get(position);

        viewHolder.mCastName.setText(cast.getName());
        viewHolder.mCharacterName.setText(cast.getCharacter());

        final String posterUrl = MovieUtils.buildPosterUrl(MovieUtils.API_POSTER_SIZE_W500, cast.getProfile_path());

        Glide.with(mContext)
                .load(posterUrl)
                .apply(RequestOptions.fitCenterTransform())
                .into(viewHolder.mCastImage);
    }

    @Override
    public int getItemCount() {
        int maxNumber = 10;

        if (mCastList.size() > maxNumber){
            return maxNumber;
        } else {
            return mCastList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mCastImage;
        private TextView mCastName;
        private TextView mCharacterName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mCastImage = (ImageView)itemView.findViewById(R.id.iv_movie_image);
            mCastName = (TextView) itemView.findViewById(R.id.tv_movie_name);
            mCharacterName = (TextView)itemView.findViewById(R.id.tv_cast_character);
        }
    }
}
