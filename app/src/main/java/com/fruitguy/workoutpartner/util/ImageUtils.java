package com.fruitguy.workoutpartner.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ImageUtils {

    public static void loadImage(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context).load(url).into(imageView);
                    }
                });
    }
}
