package com.fruitguy.workoutpartner.profile;

import com.fruitguy.workoutpartner.data.FirebaseRepository;
import com.fruitguy.workoutpartner.di.ActivityScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by heliao on 2/28/18.
 */

@Module
public abstract class ProfileModule {

    @ActivityScoped
    @Binds
    abstract ProfileContract.Presenter profilePresenter(ProfilePresenter presenter);
}
