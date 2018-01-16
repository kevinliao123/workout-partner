package com.fruitguy.workoutpartner;

/**
 * Created by heliao on 11/6/17.
 */

import com.facebook.stetho.Stetho;

public class DebugApplication extends WorkoutApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
