package mediaaction.android.core.Rest;

import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import mediaaction.android.core.ImageDTO;
import mediaaction.android.core.ResultDTO;
import mediaaction.android.core.StatDTO;
import mediaaction.android.core.UserDTO;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface RestService {

	// USER
	@POST("user/auth/login")
	@FormUrlEncoded
	Single<UserDTO> login(@Field("username") String username, @Field("password") String password);

	@POST("user/auth/register")
	@FormUrlEncoded
	Single<UserDTO> register(@Field("email") String mail, @Field("username") String username, @Field("password") String password, @Field("role") String role);

	@GET("user/{userid}/stats/avgsellsprice")
	Single<StatDTO> getAvgSellsPrice(@Path("userid") String userid);

	@GET("user/{userid}/uploads")
	Single<List<ImageDTO>> getUserUploads(@Path("userid") String userid);

	@GET("/user/{userid}/stats/sells")
	Single<List<ImageDTO>> getSoldPhotos(@Path("userid") String userid);

	// GALLERY
	@Multipart
	@POST("/gallery/upload")
	Single<ResultDTO> uploadImage(@PartMap Map<String, RequestBody> body);

	@GET("http://54.37.159.50:8568/{imageid}")
	Single<ResponseBody> getImage(@Path("imageid") String imageid);
}
