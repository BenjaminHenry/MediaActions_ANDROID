package mediaaction.android.logic;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import mediaaction.android.core.EnumUtils;
import mediaaction.android.core.Rest.ErrorCode;
import mediaaction.android.core.Rest.KnownRestException;
import mediaaction.android.core.Rest.RestException;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class RestHelper {

	public <T> ResponseTransformer<T> responseTransformer() {
		return new ResponseTransformer<>();
	}

	public class ResponseTransformer<T> implements SingleTransformer<T, T> {
		@Override
		public SingleSource<T> apply(Single<T> upstream) {
			return upstream
					.onErrorResumeNext(throwable -> {
						if (throwable instanceof HttpException) {
							HttpException httpException = (HttpException) throwable;

							Response response = httpException.response();
							ResponseBody responseBody = response.errorBody();

							if (responseBody != null) {
								try {
									TypeToken<ResultDTO> typeToken = new TypeToken<ResultDTO>() {
									};
									ResultDTO resultDTO = new GsonBuilder().create().fromJson(responseBody.charStream(), typeToken.getType());

									ErrorCode errorCode = EnumUtils.fromKey(ErrorCode.values(), resultDTO.error, ErrorCode.ERROR_UNKNOWN_ERROR);
									return Single.error(new KnownRestException(errorCode, resultDTO.statusCode));
								} catch (Exception ignored) {
									// The response body of the HTTP error couldn't be parse. Nothing to do here
								}
							}
						}
						return Single.error(new RestException(throwable));
					})
					.flatMap(x -> {
						return Single.just(x);
					});
		}
	}
}
