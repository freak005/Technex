package com.facebook.android;

import android.app.Application;
import android.util.Log;

import com.facebook.model.GraphUser;

import java.util.Collection;

// We use a custom Application class to store our minimal state data (which users have been selected).
// A real-world application will likely require a more robust data model.
public class FriendPickerApplication extends Application {
    private Collection<GraphUser> selectedUsers;

    public Collection<GraphUser> getSelectedUsers() {
    	Log.d("Facebook-Example", "inside getselected");
        return selectedUsers;
    }

    public void setSelectedUsers(Collection<GraphUser> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }
}
