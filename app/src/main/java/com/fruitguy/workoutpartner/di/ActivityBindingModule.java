package com.fruitguy.workoutpartner.di;

import com.fruitguy.workoutpartner.chat.ChatActivity;
import com.fruitguy.workoutpartner.chat.ChatModule;
import com.fruitguy.workoutpartner.main.MainActivity;
import com.fruitguy.workoutpartner.main.MainModule;
import com.fruitguy.workoutpartner.profile.ProfileActivity;
import com.fruitguy.workoutpartner.profile.ProfileModule;
import com.fruitguy.workoutpartner.user.UserActivity;
import com.fruitguy.workoutpartner.user.UserModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = ProfileModule.class)
    abstract ProfileActivity profileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = UserModule.class)
    abstract UserActivity userActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ChatModule.class)
    abstract ChatActivity chatActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();
}
