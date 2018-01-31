package com.fruitguy.workoutpartner.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fruitguy.workoutpartner.R;
import com.fruitguy.workoutpartner.authentication.AuthenticationActivity;
import com.fruitguy.workoutpartner.search.SearchActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainPageContract.View{
    MainPageContract.Presenter mMainPagePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMainPagePresenter = new MainPagePresenter(this);

        if(!mMainPagePresenter.doesUserExist()) {
            Intent loginIntent = new Intent(MainActivity.this, AuthenticationActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.getIdToken(true);
    }

    @Override
    public void setPresenter(MainPageContract.Presenter presenter) {
        mMainPagePresenter = presenter;
    }

    @OnClick(R.id.search_button)
    public void onSearchButtonClicked(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }
}
