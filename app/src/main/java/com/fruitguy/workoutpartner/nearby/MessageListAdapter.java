package com.fruitguy.workoutpartner.nearby;

import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.data.UserMessage;
import com.fruitguy.workoutpartner.util.DiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heliao on 1/21/18.
 */

public class MessageListAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    List<UserMessage> mMessageList = new ArrayList<>();

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(parent.getContext(),
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals("token")) {

                }

                if (key.equals("image")) {
                    holder.setProfileImage(mMessageList.get(position).getImageUrl());
                }

                if (key.equals("message_body")) {
                    holder.setMessage(mMessageList.get(position).getMessageBody());
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.bind(mMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void add(UserMessage message) {
        mMessageList.add(0, message);
    }

    public void remove(UserMessage message) {
        if (!mMessageList.contains(message)) {
            return;
        }
        mMessageList.remove(message);
    }

    public void clearAllMessage() {
        mMessageList.clear();
    }

    public void refresh() {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(mMessageList, mMessageList));
        diffResult.dispatchUpdatesTo(this);
    }
}
