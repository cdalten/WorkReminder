package com.example.cd.workreminder;

import android.support.v4.app.NotificationCompat;

/*
  Class taken from...
  https://github.com/googlesamples/android-Notifications/blob/master/Application/src/main/java/com/example/android/wearable/wear/wearnotifications/GlobalNotificationBuilder.java

 */
public final class GlobalNotificationBuilder {
    private static NotificationCompat.Builder sGlobalNotificationCompatBuilder = null;

    /*
     * Empty constructor - We don't initialize builder because we rely on a null state to let us
     * know the Application's process was killed.
     */
    private GlobalNotificationBuilder() { }

    public static void setNotificationCompatBuilderInstance (NotificationCompat.Builder builder) {
        sGlobalNotificationCompatBuilder = builder;
    }

    public static NotificationCompat.Builder getNotificationCompatBuilderInstance() {
        return sGlobalNotificationCompatBuilder;
    }
}
