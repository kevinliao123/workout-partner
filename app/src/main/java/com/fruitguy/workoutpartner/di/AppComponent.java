package com.fruitguy.workoutpartner.di;

import android.app.Application;

import com.fruitguy.workoutpartner.WorkoutApplication;
import com.fruitguy.workoutpartner.chat.ChatModule;
import com.fruitguy.workoutpartner.data.ChatRepository;
import com.fruitguy.workoutpartner.data.DataModule;
import com.fruitguy.workoutpartner.data.FirebaseRepository;
import com.fruitguy.workoutpartner.profile.ProfileModule;
import com.fruitguy.workoutpartner.user.UserModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules = {
        DataModule.class
        , AppModule.class
        , ActivityBindingModule.class
        , AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<WorkoutApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
