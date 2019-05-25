package mediaaction.android.logic.Account;

import android.content.Context;

import io.reactivex.Single;
import mediaaction.android.core.SessionManager;
import mediaaction.android.logic.User.UserDTO;
import mediaaction.android.logic.RestManager;
import mediaaction.android.logic.SessionUtils;

public class AccountManager {

	private RestManager restManager = new RestManager();
	private Context context;
	private SessionManager sessionManager;

	public AccountManager(Context _context) {
		context = _context;
	}

	public Single<UserDTO> login(String username, String password) {
		return restManager.login(username, password)
				.compose(SessionUtils.saveSession(new SessionManager(context), username, password));
	}

	public Single<UserDTO> register(String mail, String username, String password) {
		return restManager.register(mail, username, password)
				.compose(SessionUtils.saveSession(new SessionManager(context), username, password));
	}
}
