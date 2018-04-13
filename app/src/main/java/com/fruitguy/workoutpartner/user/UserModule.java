package com.fruitguy.workoutpartner.user;

import com.fruitguy.workoutpartner.data.FirebaseRepository;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class UserModule {
    @Provides
    static UserContract.Presenter provideUserPresenter(FirebaseRepository repo) {
        return new UserPresenter(repo);
    }
}
