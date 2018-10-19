package mediaaction.android.logic.Account;

import android.content.Context;

import io.reactivex.Single;
import mediaaction.android.core.SessionManager;
import mediaaction.android.core.UserDTO;
import mediaaction.android.logic.RestManager;

public class AccountManager {

	private RestManager restManager = new RestManager();
	private Context context;
	private SessionManager sessionManager;

	public AccountManager(Context _context) {
		context = _context;
	}

	public Single<UserDTO> login(String username, String password) {
		return restManager.login(username, password);
//				.compose(SessionUtils.saveSession(context, new SessionManager(context), username, password));
	}

	public Single<UserDTO> register(String mail, String username, String password, String passwordConf) {
		return restManager.register(mail, username, password, passwordConf);
		//				.compose(SessionUtils.saveSession(context, new SessionManager(context), username, password));
	}
}
