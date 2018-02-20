package com.fruitguy.workoutpartner.data;

import android.support.annotation.NonNull;

import com.google.android.gms.nearby.messages.Message;
import com.google.gson.Gson;

/**
 * Created by heliao on 1/15/18.
 */

public class UserMessage implements Comparable {

    private static Gson mGson= new Gson();
    private String mUserToken;
    private String mImageUrl;
    private String mMessage;

    public UserMessage(String token, String image, String message) {
        mUserToken = token;
        mImageUrl = image;
        mMessage = message;
    }

    public static Message newNearbyMessage(String token, String image, String message) {
        UserMessage deviceMessage = new UserMessage(token, image, message);
        return new Message(mGson.toJson(deviceMessage).getBytes());
    }

    public static Message newNearbyMessage(UserMessage message) {
        return new Message(mGson.toJson(message).getBytes());
    }

    public static UserMessage fromNearbyMessage(Message message) {
        String nearbyMessageString = new String(message.getContent()).trim();
        return mGson.fromJson(nearbyMessageString, UserMessage.class);
    }

    public String getUserToken() {
        return mUserToken;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getMessageBody() {
        return mMessage;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        UserMessage compare = (UserMessage) o;
        if (compare.getUserToken().equals(mUserToken)
                && compare.getImageUrl().equals(mImageUrl)
                && compare.getMessageBody().equals(mMessage)) {
            return 1;
        }

        return 0;
    }
}
