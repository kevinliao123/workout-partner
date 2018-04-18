package com.fruitguy.workoutpartner.constant;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by heliao on 2/26/18.
 */

public class FirebaseConstant {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({USER_NAME
            , USER_GENDER
            , USER_AGE
            , USER_WEIGHT
            , USER_STATUS
            , USER_IMAGE
            , USER_THUMB_NAIL
            , PROFILE_IMAGE_STORAGE})
    public @interface DatabaseField {
    }
    public static final String USER_DATABASE = "users";
    public static final String DEVICE_TOKEN = "device_token";
    public static final String USER_NAME = "name";
    public static final String USER_GENDER = "gender";
    public static final String USER_AGE = "age";
    public static final String USER_WEIGHT = "weight";
    public static final String USER_STATUS = "status";
    public static final String USER_IMAGE = "image";
    public static final String USER_THUMB_NAIL = "thumbNail";
    public static final String PROFILE_IMAGE_STORAGE = "profile_images";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({FRIEND_REQUEST
            , REQUEST_TYPE
            , SENT
            , RECEIVED
            , STATE_FRIENDS
            , STATE_NOT_FRIENDS
            , STATE_REQUEST_RECEIVE
            , STATE_REQUEST_SENT})
    public @interface FriendRequest {
    }
    public static final String FRIEND_REQUEST = "friend_request";
    public static final String REQUEST_TYPE = "request_type";
    public static final String SENT = "sent";
    public static final String RECEIVED = "received";
    public static final String STATE_NOT_FRIENDS = "not_friends";
    public static final String STATE_FRIENDS = "friends";
    public static final String STATE_REQUEST_SENT = "request_sent";
    public static final String STATE_REQUEST_RECEIVE = "request_receive";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({FRIENDS})
    public @interface Friends {
    }
    public static final String FRIENDS = "friends";
    public static final String DATE = "date";

    public static final String NOTIFICATION = "notification";
    public static final String NOTIFICATION_TYPE = "request";
    public static final String FROM_USER_ID = "from_user_id";
    public static final String FRIEND_USER_ID = "user_id";
}
