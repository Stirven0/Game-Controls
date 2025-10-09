package com.example.gamecontrolsproyect;

import android.app.Application;
import com.aa.crash_handler.CrashHandler;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.registerGlobal(this);
    }
}
