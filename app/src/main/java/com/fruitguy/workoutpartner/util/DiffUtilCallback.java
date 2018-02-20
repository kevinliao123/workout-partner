package com.fruitguy.workoutpartner.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.fruitguy.workoutpartner.data.UserMessage;

import java.util.List;

/**
 * Created by heliao on 1/21/18.
 */

public class DiffUtilCallback extends DiffUtil.Callback {
    List<UserMessage> newList;
    List<UserMessage> oldList;

    public DiffUtilCallback(List<UserMessage> newList, List<UserMessage> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0 ;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0 ;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newList.get(newItemPosition).compareTo (oldList.get(oldItemPosition));
        if (result==0){
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        UserMessage newMessage = newList.get(newItemPosition);
        UserMessage oldMessage = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();
        if(!newMessage.getUserToken().equals(oldMessage.getUserToken())){
            diff.putString("token", newMessage.getUserToken());
        }

        if(!newMessage.getImageUrl().equals (oldMessage.getImageUrl())){
            diff.putString("image", newMessage.getImageUrl());
        }

        if(!newMessage.getMessageBody().equals (oldMessage.getMessageBody())){
            diff.putString("message_body", newMessage.getMessageBody());
        }

        if (diff.size()==0){
            return null;
        }
        return diff;
    }
}
