package com.fruitguy.workoutpartner.nearby;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.conversation.ConvFragment;
import com.fruitguy.workoutpartner.data.UserMessage;
import com.fruitguy.workoutpartner.user.UserActivity;
import com.fruitguy.workoutpartner.util.ImageUtils;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.FRIEND_USER_ID;

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
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(FRIEND_USER_ID, mUserMessage.getUserToken());
        context.startActivity(intent);
    }

    public void setProfileImage(String imageUrl) {
        ImageUtils.loadImage(mContext, imageUrl, mProfileImage);
    }

    public void setMessage(String message) {
        this.mMessage.setText(message);
    }
}