package com.timsimonhughes.a04_bakingapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtils {

    public static void loadImage(Context context, String imagePath, ImageView imageView) {
        Glide.with(context)
                .load(imagePath)
                .into(imageView);
    }
}
