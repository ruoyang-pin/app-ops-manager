package com.rich.util;

import android.annotation.SuppressLint;
import android.app.Application;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author rich
 * @date 2024/10/21
 * @description
 */
public class AppInjector {

    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    public static Application getApplicationByPackageName(String packageName) {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getMethod("currentActivityThread");
            Object activityThread = currentActivityThreadMethod.invoke(null);
            Field allApplicationsField = activityThreadClass.getDeclaredField("mAllApplications");
            allApplicationsField.setAccessible(true);
            List<?> allApplications = (List<?>) allApplicationsField.get(activityThread);
            if (allApplications != null) {
                for (Object application : allApplications) {
                    if (((Application) application).getPackageName().equals(packageName)) {
                        return (Application) application;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

