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
    public @interface DatabaseField{};
    public static String USER_DATABASE = "users";
    public static final String USER_NAME = "name";
    public static final String USER_GENDER = "gender";
    public static final String USER_AGE = "age";
    public static final String USER_WEIGHT = "weight";
    public static final String USER_STATUS = "status";
    public static final String USER_IMAGE = "image";
    public static final String USER_THUMB_NAIL = "thumbNail";
    public static final String PROFILE_IMAGE_STORAGE = "profile_images";
}
