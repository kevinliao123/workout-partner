package com.fruitguy.workoutpartner.main;


import com.fruitguy.workoutpartner.conversation.ConvContract;
import com.fruitguy.workoutpartner.conversation.ConvFragment;
import com.fruitguy.workoutpartner.conversation.ConvPresenter;
import com.fruitguy.workoutpartner.di.ActivityScoped;
import com.fruitguy.workoutpartner.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract ConvFragment convFragment();

    @ActivityScoped
    @Binds
    abstract ConvContract.Presenter convPresenter(ConvPresenter presenter);


}
