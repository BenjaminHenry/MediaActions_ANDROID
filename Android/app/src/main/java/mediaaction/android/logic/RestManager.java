package mediaaction.android.logic;

import android.util.Log;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.core.RestService;
import mediaaction.android.core.UserDTO;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

	private OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();


	public RestManager() {
		// This permit to get log during retrofit call
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		okHttpClient.addInterceptor(logging);

	}

	private Retrofit retrofit = new Retrofit.Builder()
			.baseUrl("http://dev.media-actions.eu:8000/api/")
			.client(okHttpClient.build())
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
			.build();

	private RestService restService = retrofit.create(RestService.class);
	private RestHelper restHelper = new RestHelper();

	public Single<UserDTO> login(String username, String password) {
		Log.i("LOGIN", "username : " + username);
		return restService.login(username, password)
				.compose(restHelper.responseTransformer());
	}

	public Single<UserDTO> register(String mail, String username, String password, String passwordConf) {
		Log.i("REGISTER", "username : " + username);
		return restService.register(mail, username, password, passwordConf)
				.compose(restHelper.responseTransformer());
	}
}
