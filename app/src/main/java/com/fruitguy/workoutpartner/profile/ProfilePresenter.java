package com.fruitguy.workoutpartner.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import com.fruitguy.workoutpartner.data.FirebaseRepository;
import com.fruitguy.workoutpartner.data.User;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.inject.Inject;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.PROFILE_IMAGE_STORAGE;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.USER_IMAGE;

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
        retrieveUserInfo();
    }

    @Override
    public void takeView(ProfileContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
        mFirebaseRepo.shutdown();
    }

    private void retrieveUserInfo() {
        mFirebaseRepo.getCurrentUserInfo(data -> {
            User user = (User) data;
            mView.updateProfileUi(user);
        });
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
    public void handleCameraImageResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == ProfileActivity.CAMERA) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            uploadImageByBitmap(image);
            uploadThumbNail(image);
        }
    }

    @Override
    public void uploadImageByUri(Uri uri) {
        String userId = mFirebaseRepo.getCurrentUser().getUid();
        FirebaseRepository.UploadCallBack callback = new FirebaseRepository.UploadCallBack() {
            @Override
            public void onSuccess(Object data) {
                if(data instanceof Uri) {
                    String imageUrl = data.toString();
                    mFirebaseRepo.updateImageUrl(imageUrl, USER_IMAGE);
                }
            }

            @Override
            public void onFailure() {
                mView.showSnackBar("Upload Failed");
            }
        };
        FirebaseRepository.UploadImageContainer container
                = new FirebaseRepository.UploadImageContainer(uri, userId, PROFILE_IMAGE_STORAGE, callback);
        mFirebaseRepo.uploadImage(container);
    }

    @Override
    public void uploadImageByBitmap(Bitmap bitmap) {
        byte[] dataArray = convertToByteArray(bitmap, 100);
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

    @Override
    public void uploadThumbNail(Bitmap bitmap) {
        byte[] dataArray = convertToByteArray(bitmap, 65);
        mFirebaseRepo.uploadThumbNail(dataArray, new FirebaseRepository.UploadCallBack() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure() {
                mView.showSnackBar("Upload Failed");
            }
        });
    }

    private byte[] convertToByteArray(Bitmap bitmap, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        byte[] dataArray = baos.toByteArray();
        return dataArray;
    }
}
