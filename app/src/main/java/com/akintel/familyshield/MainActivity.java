package com.akintel.familyshield;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private LinearLayout list;
    private TextView status;
    private SharedPreferences prefs;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("family_shield_local", MODE_PRIVATE);
        buildUi();
        render();
    }

    private int dp(float n) { return (int)(n * getResources().getDisplayMetrics().density); }

    private TextView text(String value, int size) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextSize(size); t.setTextColor(0xFFEDF4FA);
        t.setPadding(dp(12), dp(10), dp(12), dp(10));
        return t;
    }

    private Button button(String label, Runnable action) {
        Button b = new Button(this); b.setText(label);
        b.setOnClickListener(v -> action.run()); return b;
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(18), dp(16), dp(18));
        root.setBackgroundColor(0xFF071426);
        scroll.addView(root);

        TextView title = text("AK INTEL  |  FAMILY SHIELD", 22);
        title.setGravity(Gravity.CENTER);
        root.addView(title);
        root.addView(text("Transparent family-safety prototype", 14));
        root.addView(text("This screen stores only notification app name, visible title/sender label (if supplied by Android), and time. It does NOT read or save message text, previews, images, or chats. Labels may be generic or unavailable.", 14));

        status = text("", 14);
        root.addView(status);
        root.addView(button("Open Android Notification Access", () ->
            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))));
        root.addView(button("Refresh local activity", this::render));
        root.addView(button("Delete all saved activity", () -> {
            prefs.edit().remove("events").apply();
            render();
        }));

        root.addView(text("Recent notification labels", 18));
        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        root.addView(list);
        root.addView(text("Privacy: access can be revoked at any time in Android Settings. Use only with the child's knowledge and agreement.", 13));
        setContentView(scroll);
    }

    private boolean accessEnabled() {
        String enabled = android.provider.Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        return enabled != null && enabled.contains(getPackageName());
    }

    private void render() {
        if (status != null) status.setText(accessEnabled()
            ? "Notification access: enabled on this device"
            : "Notification access: not enabled");
        if (list == null) return;
        list.removeAllViews();
        try {
            JSONArray arr = new JSONArray(prefs.getString("events", "[]"));
            if (arr.length() == 0) {
                list.addView(text("No notification activity saved yet.", 14));
                return;
            }
            for (int i = arr.length() - 1; i >= 0; i--) {
                JSONObject o = arr.getJSONObject(i);
                String app = o.optString("app", "App");
                String label = o.optString("label", "Sender not shown");
                long time = o.optLong("time", 0);
                list.addView(text(app + "  •  " + label + "\n" + android.text.format.DateFormat.format("dd MMM, hh:mm a", time), 14));
            }
        } catch (Exception e) {
            list.addView(text("Could not read local report.", 14));
        }
    }
}
