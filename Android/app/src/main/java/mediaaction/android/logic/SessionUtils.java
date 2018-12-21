package mediaaction.android.logic;

import io.reactivex.SingleTransformer;
import mediaaction.android.core.SessionManager;

public class SessionUtils {

	public static <T> SingleTransformer<T, T> saveSession(SessionManager sessionManager, String username, String password) {
		return upstream -> upstream
				.doOnSuccess(it -> sessionManager.storeUser(username, password));
	}
}