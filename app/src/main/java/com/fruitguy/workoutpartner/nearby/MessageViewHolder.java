package com.fruitguy.workoutpartner.nearby;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.chat.ConvFragment;
import com.fruitguy.workoutpartner.data.UserMessage;
import com.fruitguy.workoutpartner.util.ImageUtils;

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
        mMessage.setText(userMessage.getMessageBody());
        itemView.setOnClickListener(this);
        ImageUtils.loadImage(mContext, userMessage.getImageUrl().toString(), mProfileImage);
    }

    @Override
    public void onClick(View v) {
        Context context = itemView.getContext();
        Intent chatIntent = new Intent(context, ConvFragment.class);
        chatIntent.putExtra(context.getString(R.string.token_key), mUserMessage.getUserToken());
        context.startActivity(chatIntent);
    }

    public void setProfileImage(String imageUrl) {
        ImageUtils.loadImage(mContext, imageUrl, mProfileImage);
    }

    public void setMessage(String message) {
        this.mMessage.setText(message);
    }
}