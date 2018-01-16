package com.fruitguy.workoutpartner.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heliao on 12/27/17.
 */

public class AuthenticationActivity extends AppCompatActivity implements AuthenticationContract.View {

    @BindView(R.id.root)
    View mRootView;

    private AuthenticationContract.Presenter mAuthenticationPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        mAuthenticationPresenter = new AuthenticationPresenter(this);
        mAuthenticationPresenter.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthenticationPresenter.onAuthenticationResponse(requestCode, resultCode, data);
    }

    @Override
    public void setPresenter(AuthenticationContract.Presenter presenter) {
        mAuthenticationPresenter = presenter;
    }

    @Override
    public void showSnackbar(int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
