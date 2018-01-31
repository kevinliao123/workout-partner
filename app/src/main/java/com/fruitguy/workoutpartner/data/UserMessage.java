package com.fruitguy.workoutpartner.data;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.fruitguy.workoutpartner.util.UriDeserializer;
import com.fruitguy.workoutpartner.util.UriSerializer;
import com.google.android.gms.nearby.messages.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by heliao on 1/15/18.
 */

public class UserMessage implements Comparable {

    private static final Gson SERIALIZE_GSON = new GsonBuilder()
            .registerTypeAdapter(Uri.class, new UriSerializer())
            .create();

    private static final Gson DESERIALIZE_GSON = new GsonBuilder()
            .registerTypeAdapter(Uri.class, new UriDeserializer())
            .create();


    private String mUserToken;
    private Uri mImage;
    private String mMessage;

    public UserMessage(String token, Uri image, String message) {
        mUserToken = token;
        mImage = image;
        mMessage = message;
    }

    public static Message newNearbyMessage(String token, Uri image, String message) {
        UserMessage deviceMessage = new UserMessage(token, image, message);
        return new Message(SERIALIZE_GSON.toJson(deviceMessage).getBytes());
    }

    public static Message newNearbyMessage(UserMessage message) {
        return new Message(SERIALIZE_GSON.toJson(message).getBytes());
    }

    public static UserMessage fromNearbyMessage(Message message) {
        String nearbyMessageString = new String(message.getContent()).trim();
        return DESERIALIZE_GSON.fromJson(nearbyMessageString, UserMessage.class);
    }

    public String getUserToken() {
        return mUserToken;
    }

    public Uri getImage() {
        return mImage;
    }

    public String getMessageBody() {
        return mMessage;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        UserMessage compare = (UserMessage) o;
        if (compare.getUserToken().equals(mUserToken)
                && compare.getImage().equals(mImage)
                && compare.getMessageBody().equals(mMessage)) {
            return 1;
        }

        return 0;
    }
}
