package com.fruitguy.workoutpartner.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import com.fruitguy.workoutpartner.data.FirebaseRepository;
import com.fruitguy.workoutpartner.data.User;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by heliao on 2/21/18.
 */

public class ProfilePresenter implements ProfileContract.Presenter {
    
    private static final String TAG = ProfilePresenter.class.getSimpleName();
    private ProfileContract.View mView;
    private FirebaseRepository mFirebaseRepo;

    @Inject
    ProfilePresenter(FirebaseRepository firebaseRepository) {
        mFirebaseRepo = firebaseRepository;
    }

    @Override
    public void start() {
        mFirebaseRepo.init(data -> updateUserProfileOnUi((User) data));
    }

    @Override
    public void takeView(ProfileContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void updateUserProfileInBackend(User user) {
        mFirebaseRepo.updateUserProfileInfoToBackEnd(user);
    }

    @Override
    public void updateUserProfileOnUi(User user) {
        mView.updateProfileUi(user);
    }

    @Override
    public void handleCropImageResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            mFirebaseRepo.uploadImage(resultUri, new FirebaseRepository.UploadCallBack() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure() {
                    mView.showSnackBar("Upload Failed");
                }
            });
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @Override
    public void handleCameraImageResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ProfileActivity.CAMERA) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] dataArray = baos.toByteArray();
            mFirebaseRepo.uploadImage(dataArray, new FirebaseRepository.UploadCallBack() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure() {
                    mView.showSnackBar("Upload Failed");
                }
            });
        }
    }
}
