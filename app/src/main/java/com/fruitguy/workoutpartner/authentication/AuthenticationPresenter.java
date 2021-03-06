package com.fruitguy.workoutpartner.authentication;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.authentication.AuthenticationContract.View;
import com.fruitguy.workoutpartner.constant.FirebaseConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.fruitguy.workoutpartner.constant.FirebaseConstant.DEVICE_TOKEN;

/**
 * Created by heliao on 1/7/18.
 */

public class AuthenticationPresenter implements AuthenticationContract.Presenter {

    private static final int RC_SIGN_IN = 1001;
    private static final String TAG = AuthenticationPresenter.class.getSimpleName();

    private boolean mIsSignInSuccessful;
    View mView;
    DatabaseReference mDatabase;
    FirebaseAuth mUserAuthentication;

    AuthenticationPresenter(View view
            , FirebaseAuth userAuthentication
            , DatabaseReference database) {
        mView = view;
        mUserAuthentication = userAuthentication;
        mDatabase = database.child(FirebaseConstant.USER_DATABASE);
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        ((Activity) mView).startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false, true)
                        .setAvailableProviders(createProviderList())
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void takeView(Object view) {

    }

    @Override
    public void dropView() {

    }

    private List<AuthUI.IdpConfig> createProviderList() {
        return Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build());
    }

    @Override
    public void onAuthenticationResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == RESULT_OK) {
            addUser(mUserAuthentication.getCurrentUser());
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                mView.showSnackbar(R.string.sign_in_cancelled);
                mView.finishActivity();
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                mView.showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                mView.showSnackbar(R.string.unknown_error);
                return;
            }
        }

        mView.showSnackbar(R.string.unknown_sign_in_response);
    }

    private void addUser(FirebaseUser user) {
        HashMap<String, Object> userMap = getUserMap(user);
        mDatabase.child(user.getUid())
                .setValue(userMap).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                updateDeviceToken(user);
            } else {
                Log.e(TAG, "addUser unsuccessful");
            }
        });
    }

    private void updateDeviceToken(FirebaseUser user) {
        String deviceToken = FirebaseInstanceId.getInstance().getToken();
        mDatabase.child(user.getUid()).child(DEVICE_TOKEN)
                .setValue(deviceToken)
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful()) {
                        mView.startMainActivity();
                        mView.finishActivity();
                    } else {
                        Log.e(TAG, "updateDeviceToken unsuccessful");
                    }
                });

    }

    private HashMap<String, Object> getUserMap(FirebaseUser user) {
        return new HashMap<String, Object>() {
            {
                put(FirebaseConstant.USER_NAME, user.getDisplayName());
                put(FirebaseConstant.USER_GENDER, "male");
                put(FirebaseConstant.USER_AGE, 18);
                put(FirebaseConstant.USER_WEIGHT, 150);
                put(FirebaseConstant.USER_STATUS, "this is workout partner");
                put(FirebaseConstant.USER_IMAGE, user.getPhotoUrl().toString());
                put(FirebaseConstant.USER_THUMB_NAIL, user.getPhotoUrl().toString());
            }
        };
    }


    @Override
    public boolean isSignInSuccessful() {
        return mIsSignInSuccessful;
    }
}
