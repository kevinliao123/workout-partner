package com.fruitguy.workoutpartner.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.util.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    public View mView;

    @BindView(R.id.profile_image)
    CircleImageView mUserImage;

    @BindView(R.id.message)
    TextView mMessage;

    @BindView(R.id.upload_image)
    ImageView mUploadedImage;

    public ChatMessageViewHolder(View view) {
        super(view);
        mView = view;
        ButterKnife.bind(this, view);
    }

    public void setMessage(String message){
        mMessage.setText(message);
    }

    public void setUserImage(Context context, String imageUrl) {
        ImageUtils.loadImage(context, imageUrl, mUserImage);
    }

    public void setUploadedImage(Context context, String imageUrl) {
        ImageUtils.loadImage(context, imageUrl, mUploadedImage);
    }


}
