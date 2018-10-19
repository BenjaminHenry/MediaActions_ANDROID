package mediaaction.android.core;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestService {

	@POST("login")
	@FormUrlEncoded
	Single<UserDTO> login(@Field("username") String username, @Field("password") String password);

	@POST("register")
	@FormUrlEncoded
	Single<UserDTO> register(@Field("email") String mail, @Field("username") String username, @Field("password") String password, @Field("passwordConf") String passwordConf);
}
