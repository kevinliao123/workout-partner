package com.fruitguy.workoutpartner.chat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fruitguy.workoutpartner.R;

import static com.fruitguy.workoutpartner.constant.FirebaseConstant.TEXT;

public class MessageAdapter extends FirebaseRecyclerAdapter<ChatMessage, ChatMessageViewHolder> {

    private static final String TAG = MessageAdapter.class.getSimpleName();
    String mFriendUserId;
    String mFriendThumb;
    Context mContext;

    public MessageAdapter(@NonNull FirebaseRecyclerOptions<ChatMessage> options
            , Context context
            , String friendUserId
            , String friendThumb) {
        super(options);
        mContext = context;
        mFriendUserId = friendUserId;
        mFriendThumb = friendThumb;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position, @NonNull ChatMessage model) {

        if(model.getType().equals(TEXT)) {
            holder.mUploadedImage.setVisibility(View.GONE);
            holder.mMessage.setVisibility(View.VISIBLE);
            holder.setMessage(model.getMessage());
            if (model.getFrom().equals(mFriendUserId)) {
                holder.mMessage.setBackground(mContext.getResources().getDrawable(R.drawable.friend_chat_rectangle));
                adjustPositionToLeft(holder, holder.mMessage);
            } else {
                holder.mMessage.setBackground(mContext.getResources().getDrawable(R.drawable.user_chat_rectangle));
                adjustPositionToRight(holder, holder.mMessage);
            }
        } else {
            holder.mUploadedImage.setVisibility(View.VISIBLE);
            holder.mMessage.setVisibility(View.GONE);
            holder.setUploadedImage(mContext, model.message);
            if (model.getFrom().equals(mFriendUserId)) {
                adjustPositionToLeft(holder, holder.mUploadedImage);
            } else {
                adjustPositionToRight(holder, holder.mUploadedImage);
            }
        }
    }

    private void adjustPositionToLeft(ChatMessageViewHolder holder, View target){
        holder.mUserImage.setVisibility(View.VISIBLE);
        holder.setUserImage(mContext, mFriendThumb);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) target.getLayoutParams();
        param.addRule(RelativeLayout.RIGHT_OF, R.id.profile_image);
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
        param.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        param.leftMargin = 20;
        holder.mMessage.setLayoutParams(param);
    }

    private void adjustPositionToRight(ChatMessageViewHolder holder, View target) {
        holder.mUserImage.setVisibility(View.GONE);
        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) target.getLayoutParams();
        param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param.rightMargin = 3;
        holder.mMessage.setLayoutParams(param);
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_layout, parent, false);
        return new ChatMessageViewHolder(view);
    }
}
