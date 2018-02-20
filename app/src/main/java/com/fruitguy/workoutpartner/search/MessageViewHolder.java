package com.fruitguy.workoutpartner.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.chat.ChatFragment;
import com.fruitguy.workoutpartner.data.UserMessage;
import com.squareup.picasso.Picasso;

/**
 * Created by heliao on 1/21/18.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String TAG = MessageViewHolder.class.getSimpleName();
    private ImageView mProfileImage;
    private TextView mMessage;
    private UserMessage mUserMessage;
    private Context mContext;

    public MessageViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mProfileImage = itemView.findViewById(R.id.profile_image);
        mMessage = itemView.findViewById(R.id.message);
    }

    public void bind(UserMessage userMessage) {
        mUserMessage = userMessage;
        Picasso.with(mContext).load(userMessage.getImageUrl().toString()).into(mProfileImage);
        mMessage.setText(userMessage.getMessageBody());
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Context context = itemView.getContext();
        Intent chatIntent = new Intent(context, ChatFragment.class);
        chatIntent.putExtra(context.getString(R.string.token_key), mUserMessage.getUserToken());
        context.startActivity(chatIntent);
    }

    public void setProfileImage(String imageUrl) {
        Picasso.with(mContext).load(imageUrl).into(mProfileImage);
    }

    public void setMessage(String message) {
        this.mMessage.setText(message);
    }
}