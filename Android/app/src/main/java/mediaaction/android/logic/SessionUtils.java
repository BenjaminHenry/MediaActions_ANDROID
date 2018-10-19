package mediaaction.android.logic;

import android.content.Context;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import mediaaction.android.core.SessionManager;

public class SessionUtils {

	public static <T> SingleTransformer<T, T> saveSession(Context context, SessionManager sessionManager, String username, String password) {
		return new SingleTransformer<T, T>() {
			@Override
			public SingleSource<T> apply(Single<T> upstream) {
				return upstream
						.doOnSuccess(it -> sessionManager.storeUser(context, username, password));
			}
		};
	}
}