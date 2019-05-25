package mediaaction.android.logic;

import android.util.Log;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import mediaaction.android.core.Rest.RestService;
import mediaaction.android.logic.Gallery.ImageDTO;
import mediaaction.android.logic.Request.RequestDTO;
import mediaaction.android.logic.User.StatDTO;
import mediaaction.android.logic.User.UserDTO;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

	private OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

	private Retrofit retrofit;
	private RestService restService;

	public RestManager() {
		// This permit to get log during retrofit call
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);
		okHttpClient.interceptors().add(logging);
		retrofit = new Retrofit.Builder()
				.baseUrl("http://54.37.159.50:8568/")
				.client(okHttpClient.build())
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
				.build();
		restService = retrofit.create(RestService.class);
	}

	private RestHelper restHelper = new RestHelper();

	public Single<UserDTO> login(String username, String password) {
		Log.i("LOGIN", "username : " + username);
		return restService.login(username, password)
				.compose(restHelper.responseTransformer());
	}

	public Single<UserDTO> register(String mail, String username, String password) {
		Log.i("REGISTER", "username : " + username);
		return restService.register(mail, username, password, "SELLER")
				.compose(restHelper.responseTransformer());
	}

	public Single<ResultDTO> uploadImage(Map<String, RequestBody> body) {
		return restService.uploadImage(body)
				.compose(restHelper.responseTransformer());
	}

	public Single<StatDTO> getAvgSellsPrice(String userid) {
		return restService.getAvgSellsPrice(userid)
				.compose(restHelper.responseTransformer());
	}

	public Single<List<ImageDTO>> getSoldPhotos(String userid) {
		Log.i("GETSOLDPHOTOS", "userid : " + userid);
		return restService.getSoldPhotos(userid)
				.compose(restHelper.responseTransformer());
	}

	public Single<List<ImageDTO>> getUserUploads(String userid) {
		Log.i("GETUSERUPLOADS", "userid : " + userid);
		return restService.getUserUploads(userid)
				.compose(restHelper.responseTransformer());
	}
	//GALLERY

	public Single<ResponseBody> getImage(String filename) {
		Log.i("GETIMAGE", "filename : " + filename);
		return restService.getImage(filename)
				.compose(restHelper.responseTransformer());
	}

	//REQUESTS

	public Single<List<RequestDTO>> getRequests() {
		Log.i("GETREQUESTS", "");
		return restService.getRequests()
				.compose(restHelper.responseTransformer());
	}

}