package com.akintel.familyshield;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.app.Notification;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

public class SenderNotificationListener extends NotificationListenerService {
    @Override public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null || sbn.getNotification() == null) return;
        String pkg = sbn.getPackageName();
        String app;
        if ("com.whatsapp".equals(pkg)) app = "WhatsApp";
        else if ("com.instagram.android".equals(pkg)) app = "Instagram";
        else return;

        // Deliberately read only EXTRA_TITLE; never read message body or messaging bundles.
        CharSequence title = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE);
        String label = title == null ? "" : title.toString().trim();
        if (label.isEmpty()) label = "Sender not shown";

        try {
            SharedPreferences prefs = getSharedPreferences("family_shield_local", MODE_PRIVATE);
            JSONArray old = new JSONArray(prefs.getString("events", "[]"));
            JSONArray next = new JSONArray();
            int start = Math.max(0, old.length() - 49);
            for (int i = start; i < old.length(); i++) next.put(old.get(i));
            JSONObject item = new JSONObject();
            item.put("app", app);
            item.put("label", label);
            item.put("time", System.currentTimeMillis());
            next.put(item);
            prefs.edit().putString("events", next.toString()).apply();
        } catch (Exception ignored) { }
    }
}
