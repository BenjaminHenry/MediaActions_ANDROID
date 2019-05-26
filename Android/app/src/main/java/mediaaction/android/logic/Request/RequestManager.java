package mediaaction.android.logic.Request;

import android.content.Context;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import mediaaction.android.logic.Gallery.ImageDTO;
import mediaaction.android.logic.RestManager;
import mediaaction.android.logic.ResultDTO;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RequestManager {

	private Context context;
	private RestManager restManager = new RestManager();

	public RequestManager(Context _context) {
		context = _context;
	}

	public Single<List<RequestDTO>> getRequests() {
		return restManager.getRequests();
	}

	public Single<ImageDTO> uploadImageRequest(String image, String mimetype, String title, String description, Integer price, String userid) {

		HashMap<String, RequestBody> map = new HashMap<>();
		map.put("file", RequestBody.create(MediaType.parse(mimetype), image));
		map.put("mimetype", RequestBody.create(okhttp3.MultipartBody.FORM, mimetype));
		map.put("title", RequestBody.create(okhttp3.MultipartBody.FORM, title));
		map.put("description", RequestBody.create(okhttp3.MultipartBody.FORM, description));
		map.put("price", RequestBody.create(okhttp3.MultipartBody.FORM, price.toString()));
		map.put("tags", RequestBody.create(okhttp3.MultipartBody.FORM, "tags"));
		map.put("currency", RequestBody.create(okhttp3.MultipartBody.FORM, "EUR"));
		map.put("userid", RequestBody.create(okhttp3.MultipartBody.FORM, userid));
		map.put("geoloc", RequestBody.create(okhttp3.MultipartBody.FORM, "geoloc"));
		map.put("datatype", RequestBody.create(okhttp3.MultipartBody.FORM, "base64"));

		return restManager.uploadImageRequest(map);
	}

	public Single<ResultDTO> answerRequest(String requestId, String answerUserId, String imageId) {
		return restManager.answerRequest(requestId, answerUserId, imageId);
	}
}
