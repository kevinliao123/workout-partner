package com.fruitguy.workoutpartner.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.fruitguy.workoutpartner.BasePresenter;
import com.fruitguy.workoutpartner.BaseView;
import com.fruitguy.workoutpartner.data.User;

/**
 * Created by heliao on 2/21/18.
 */

public interface ProfileContract {
    interface Presenter extends BasePresenter<View> {
        void updateUserProfileInBackend(User user);

        void updateUserProfileOnUi(User user);

        void handleCameraImageResult(int requestCode, int resultCode, Intent data);

        void uploadImageByUri(Uri uri);

        void uploadImageByBitmap(Bitmap bitmap);

        void uploadThumbNail(Bitmap bitmap);
    }

    interface View extends BaseView<Presenter> {
        void updateProfileUi(User user);

        void showSnackBar(String message);

        void showProgressDialog();

        void dismissProgressDialog();

        Bitmap compressToThumbNail(Uri uri);
    }
}
