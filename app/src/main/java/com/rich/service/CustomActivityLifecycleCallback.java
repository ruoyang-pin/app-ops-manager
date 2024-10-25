package com.rich.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author rich
 * @date 2024/10/21
 * @description
 */
public class CustomActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    private static volatile CustomActivityLifecycleCallback instance;

    public static CustomActivityLifecycleCallback getInstance() {
        if (instance == null) {
            synchronized (CustomActivityLifecycleCallback.class) {
                if (instance == null) {
                    instance = new CustomActivityLifecycleCallback();
                }
            }
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("DiscouragedPrivateApi")
    public static void registerCustomActivityLifecycleCallback(Application application) {
        if (application == null) {
            return;
        }
        CustomActivityLifecycleCallback instance = getInstance();
        synchronized (CustomActivityLifecycleCallback.class) {
            try {
                Field callbacksField = application.getClass().getDeclaredField("mActivityLifecycleCallbacks");
                callbacksField.setAccessible(true);
                List<Application.ActivityLifecycleCallbacks> callbacksArrayList;
                callbacksArrayList = (List<Application.ActivityLifecycleCallbacks>) callbacksField.get(application);
                if (callbacksArrayList != null && !callbacksArrayList.contains(instance)) {
                    application.registerActivityLifecycleCallbacks(instance);
                }
            } catch (NoSuchFieldException e) {
                Log.e("CustomLifecycleCallback", "Field mActivityLifecycleCallbacks not found", e);
            } catch (IllegalAccessException e) {
                Log.e("CustomLifecycleCallback", "Access to mActivityLifecycleCallbacks denied", e);
            } catch (Exception e) {
                Log.e("CustomLifecycleCallback", "Unexpected error during registration", e);
            }
        }
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Intent intent = activity.getIntent();
        String action = intent.getAction();
        activity.finish();
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }


}
