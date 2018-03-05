package com.fruitguy.workoutpartner.profile;

import com.fruitguy.workoutpartner.data.FirebaseRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by heliao on 2/28/18.
 */

@Module
public abstract class ProfileModule {

    @Provides
    static ProfileContract.Presenter provideProfilePresenter(FirebaseRepository firebaseRepository) {
        return new ProfilePresenter(firebaseRepository);
    }
}
