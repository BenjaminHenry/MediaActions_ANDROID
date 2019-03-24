package mediaaction.android.logic.Gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;

import io.reactivex.Single;
import mediaaction.android.core.ProgressRequestBody;
import mediaaction.android.core.ResultDTO;
import mediaaction.android.logic.RestManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class GalleryManager {

	private Context context;
	private RestManager restManager = new RestManager();

	public GalleryManager(Context _context) {
		context = _context;
	}

	public Single<ResultDTO> uploadImage(byte[] bytes, String mimetype, String title, String description, Integer price, String userid) {


		HashMap<String, RequestBody> map = new HashMap<>();
		map.put("file", new ProgressRequestBody(MediaType.parse(mimetype), bytes, null));
		map.put("mimetype", RequestBody.create(okhttp3.MultipartBody.FORM, mimetype));
		map.put("title", RequestBody.create(okhttp3.MultipartBody.FORM, title));
		map.put("description", RequestBody.create(okhttp3.MultipartBody.FORM, description));
		map.put("price", RequestBody.create(okhttp3.MultipartBody.FORM, price.toString()));
		map.put("userid", RequestBody.create(okhttp3.MultipartBody.FORM, userid));

		return restManager.uploadImage(map);
	}

	public Single<Bitmap> getImage(String filename) {
		return restManager.getImage(filename).map(it -> BitmapFactory.decodeStream(it.byteStream()));
	}
}
