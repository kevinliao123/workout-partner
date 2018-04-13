package com.fruitguy.workoutpartner.di;

import com.fruitguy.workoutpartner.profile.ProfileActivity;
import com.fruitguy.workoutpartner.profile.ProfileModule;
import com.fruitguy.workoutpartner.user.UserActivity;
import com.fruitguy.workoutpartner.user.UserModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = ProfileModule.class)
    abstract ProfileActivity profileActivity();

    @ContributesAndroidInjector(modules = UserModule.class)
    abstract UserActivity userActivity();

}
