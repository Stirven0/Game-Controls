package com.aa.crash_handler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final SimpleDateFormat DATE_FORMAT = 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static CrashHandler sInstance;

    private final Context mContext;
    private final Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler(Context context) {
        this.mContext = context.getApplicationContext();
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    public static void registerGlobal(Context context) {
        if (sInstance == null) {
            sInstance = new CrashHandler(context);
            Thread.setDefaultUncaughtExceptionHandler(sInstance);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            String log = buildLog(throwable);
            Intent intent = new Intent(mContext, CrashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, log);
            mContext.startActivity(intent);
            //Thread.sleep(3000);
        } catch (Exception e) {
            Log.e("CrashHandler", "Error handling crash", e);
        } finally {
            //android.os.Process.killProcess(android.os.Process.myPid());
            if(mDefaultHandler != null) {
            	mDefaultHandler.uncaughtException(thread,throwable);
            }
        }
    }

    private String buildLog(Throwable throwable) {
        String versionName = "unknown";
        long versionCode = 0;
        
        try {
            PackageInfo packageInfo = mContext.getPackageManager()
                .getPackageInfo(mContext.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.getLongVersionCode();
        } catch (Exception ignored) {}

        LinkedHashMap<String, String> info = new LinkedHashMap<>();
        info.put("Timestamp", DATE_FORMAT.format(new Date()));
        info.put("Device", Build.MANUFACTURER + " " + Build.MODEL);
        info.put("Android", Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")");
        info.put("App Version", versionName + " (" + versionCode + ")");
        //info.put("Kernel", getKernel());
        info.put("Support Abis", Arrays.toString(Build.SUPPORTED_ABIS));
        info.put("Fingerprint", Build.FINGERPRINT);

        StringBuilder builder = new StringBuilder();
        for (String key : info.keySet()) {
            builder.append("• ").append(key).append(": ").append(info.get(key)).append("\n");
        }
        
        builder.append("\nSTACK TRACE:\n");
        builder.append(Log.getStackTraceString(throwable));
        
        return builder.toString();
    }

}