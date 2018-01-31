package com.fruitguy.workoutpartner;

import com.fruitguy.workoutpartner.search.NearbyMessageHandler;
import com.fruitguy.workoutpartner.search.SearchInteractor;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by heliao on 1/16/18.
 */

public class Injection {
    public static SearchInteractor provideSearchInteractor(GoogleApiClient googleApiClient) {
        return NearbyMessageHandler.getInstance(googleApiClient);
    }

    public static FirebaseUser provideFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
