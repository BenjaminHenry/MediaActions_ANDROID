package mediaaction.android.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionManager {

	private static final String PREFS_NAME = "Media_Actions";
	private static final String USER_NAME = "user_name";
	private static final String USER_PWD = "user_pwd";

	private Context context;
	private SharedPreferences sharedPreferences;

	public SessionManager(Context _context) {
		context = _context;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(_context);
		sharedPreferences = _context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
	}

	public void storeUser(String username, String password) {
		SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

		sharedPreferencesEditor.putString(USER_NAME, username);
		sharedPreferencesEditor.putString(USER_PWD, password);
		sharedPreferencesEditor.apply();
	}

	public void clearSession() {
		sharedPreferences
				.edit()
				.remove(USER_NAME)
				.remove(USER_PWD)
				.apply();
	}

	public String getUserName() {
		return sharedPreferences.getString(USER_NAME, null);
	}

	public String getUserPwd() {
		return sharedPreferences.getString(USER_PWD, null);
	}
}
