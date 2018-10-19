package mediaaction.android.logic;

import android.util.Log;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;

public class RestHelper {

	public <T> ResponseTransformer<T> responseTransformer() {
		return new ResponseTransformer<>();
	}

	public class ResponseTransformer<T> implements SingleTransformer<T, T> {
		@Override
		public SingleSource<T> apply(Single<T> upstream) {
			return upstream
					.onErrorResumeNext(throwable -> {
						return Single.error(new Exception(throwable));
			})
					.flatMap(x -> {
						return Single.just(x);
					});
		}
	}
}
