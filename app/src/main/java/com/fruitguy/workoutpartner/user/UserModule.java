package com.fruitguy.workoutpartner.user;

import com.fruitguy.workoutpartner.data.FirebaseRepository;
import com.fruitguy.workoutpartner.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class UserModule {

    @ActivityScoped
    @Binds
    abstract UserContract.Presenter userPresenter(UserPresenter presenter);
}
