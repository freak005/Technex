package com.facebook.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.Session;

import java.util.ArrayList;
import java.util.Collection;

public class FriendPickerSampleActivity extends FragmentActivity {
	private static final int PICK_FRIENDS_ACTIVITY = 1;
	private Button pickFriendsButton;
	private TextView resultsTextView;
	private UiLifecycleHelper lifecycleHelper;
	boolean pickFriendsWhenSessionOpened;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picker);

		resultsTextView = (TextView) findViewById(R.id.resultsTextView);
		pickFriendsButton = (Button) findViewById(R.id.pickFriendsButton);
		Log.d("Facebook-Example", "reached friendactivity");
		pickFriendsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				Log.d("Facebook-Example", "inside onclick");
				onClickPickFriends();
			}
		});

		lifecycleHelper = new UiLifecycleHelper(this,
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChanged(session, state, exception);
					}
				});
		lifecycleHelper.onCreate(savedInstanceState);

		ensureOpenSession();
	}

//	@Override
//	protected void onStart() {
//		super.onStart();
//
//		// Update the display every time we are started.
//		displaySelectedFriends(RESULT_OK);
//	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_FRIENDS_ACTIVITY:
			displaySelectedFriends(resultCode);
			break;
		default:
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
			break;
		}
	}

	private boolean ensureOpenSession() {
		if (Session.getActiveSession() == null
				|| !Session.getActiveSession().isOpened()) {
			Session.openActiveSession(this, true, new Session.StatusCallback() {
				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					onSessionStateChanged(session, state, exception);
				}
			});
			return false;
		}
		return true;
	}

	private void onSessionStateChanged(Session session, SessionState state,
			Exception exception) {
		if (pickFriendsWhenSessionOpened && state.isOpened()) {
			pickFriendsWhenSessionOpened = false;

			startPickFriendsActivity();
		}
	}

	private void displaySelectedFriends(int resultCode) {
		String results = "";
		FriendPickerApplication application = (FriendPickerApplication) getApplication();

		Collection<GraphUser> selection = application.getSelectedUsers();
		if (selection != null && selection.size() > 0) {
			ArrayList<String> names = new ArrayList<String>();
			for (GraphUser user : selection) {
				names.add(user.getName());
			}
			results = TextUtils.join(", ", names);
		} else {
			results = "<No friends selected>";
		}

		resultsTextView.setText(results);
	}

	private void onClickPickFriends() {
		startPickFriendsActivity();
	}

	private void startPickFriendsActivity() {
		//if (ensureOpenSession()) {
			Log.d("Facebook-Example", "inside startpick");
			FriendPickerApplication application = (FriendPickerApplication) getApplication();
			Log.d("Facebook-Example", "inside startpick1");
			application.setSelectedUsers(null);
			Log.d("Facebook-Example", "inside startpick2");

			Intent intent = new Intent(this, PickFriendsActivity.class);
			Log.d("Facebook-Example", "inside startpick3");
//			Bundle tokenBundle = new Bundle();
//	        tokenBundle.putString("token", facebook.getAccessToken());
//			// Note: The following line is optional, as multi-select behavior is
			// the default for
			// FriendPickerFragment. It is here to demonstrate how parameters
			// could be passed to the
			// friend picker if single-select functionality was desired, or if a
			// different user ID was
			// desired (for instance, to see friends of a friend).
			PickFriendsActivity.populateParameters(intent, null, true, true);
			startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
			Log.d("Facebook-Example", "inside startpick4");
//		} else {
//			pickFriendsWhenSessionOpened = true;
//		}
	}
}
