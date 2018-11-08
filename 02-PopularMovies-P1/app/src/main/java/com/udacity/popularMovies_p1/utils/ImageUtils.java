package com.udacity.popularMovies_p1.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.udacity.a02_popularmovies_p1.R;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageUtils {

    public static void loadImage(View view, String imageUrl, ImageView imageView) {
        Glide.with(view)
                .load(imageUrl)
                .into(imageView);
    }

    public static void loadImageWithPalette(View view, String imageUrl, ImageView imageView, final ViewGroup viewGroup, final Context context) {

        Glide.with(view)
                .asBitmap()
                .load(imageUrl)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@Nullable Palette palette) {
                                applyPalette(palette, viewGroup, context);
                            }
                        });
                        return false;
                    }
                })
                .into(imageView);
    }

    private static void applyPalette(Palette palette, ViewGroup viewGroup, Context context) {
        int primaryDark = context.getResources().getColor(R.color.colorPrimaryDark);
        int primary = context.getResources().getColor(R.color.colorPrimary);
        viewGroup.setBackgroundColor(palette.getDominantColor(primary));


//        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
//        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
//        updateBackground((FloatingActionButton) findViewById(R.id.fab), palette);
    }

}
