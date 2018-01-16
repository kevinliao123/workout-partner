package com.fruitguy.workoutpartner.authentication;

import android.app.Activity;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.authentication.AuthenticationContract.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by heliao on 1/7/18.
 */

public class AuthenticationPresenter implements AuthenticationContract.Presenter {

    private static final int RC_SIGN_IN = 1001;

    private boolean mIsSignInSuccessful;
    View mView;

    AuthenticationPresenter(View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        ((Activity) mView).startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(createProviderList())
                        .build(),
                RC_SIGN_IN);
    }

    private List<AuthUI.IdpConfig> createProviderList() {
        return Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());
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
            mView.startMainActivity();
            mView.finishActivity();
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                mView.showSnackbar(R.string.sign_in_cancelled);
                mView.finishActivity();
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                mView.showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                mView.showSnackbar(R.string.unknown_error);
                return;
            }
        }

        mView.showSnackbar(R.string.unknown_sign_in_response);
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean isSignInSuccessful() {
        return mIsSignInSuccessful;
    }
}
