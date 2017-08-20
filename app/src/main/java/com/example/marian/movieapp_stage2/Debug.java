package com.example.marian.movieapp_stage2;


import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
/**
 * Created by  Marian on 6/19/2017.
 */

public class Debug extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();
        Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);


        initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));

        Context context = this;
        initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(context));

        Stetho.Initializer initializer = initializerBuilder.build();
         Stetho.initialize(initializer);
    }
}



