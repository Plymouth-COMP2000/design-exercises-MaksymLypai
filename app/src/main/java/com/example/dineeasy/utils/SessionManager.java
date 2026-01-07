package com.example.dineeasy.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Save user session after login
    public void createLoginSession(String username, String usertype) {
        editor.putBoolean(Constants.KEY_IS_LOGGED_IN, true);
        editor.putString(Constants.KEY_USERNAME, username);
        editor.putString(Constants.KEY_USER_TYPE, usertype);
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return prefs.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
    }

    // Get logged-in username
    public String getUsername() {
        return prefs.getString(Constants.KEY_USERNAME, null);
    }

    // Get user type (guest or staff)
    public String getUserType() {
        return prefs.getString(Constants.KEY_USER_TYPE, Constants.USER_TYPE_GUEST);
    }

    // Check if user is staff
    public boolean isStaff() {
        return Constants.USER_TYPE_STAFF.equalsIgnoreCase(getUserType());
    }

    // Logout user
    public void logout() {
        editor.clear();
        editor.apply();
    }

    // Notification preferences
    public void setReservationNotificationsEnabled(boolean enabled) {
        editor.putBoolean("notif_reservations", enabled);
        editor.apply();
    }

    public boolean areReservationNotificationsEnabled() {
        return prefs.getBoolean("notif_reservations", true);
    }

    public void setMenuNotificationsEnabled(boolean enabled) {
        editor.putBoolean("notif_menu", enabled);
        editor.apply();
    }

    public boolean areMenuNotificationsEnabled() {
        return prefs.getBoolean("notif_menu", true);
    }
}
