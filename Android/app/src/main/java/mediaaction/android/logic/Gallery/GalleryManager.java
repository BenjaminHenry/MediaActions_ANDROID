package mediaaction.android.logic.Gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.File;

import io.reactivex.Single;
import mediaaction.android.core.ResultDTO;
import mediaaction.android.logic.RestManager;

public class GalleryManager {

	private RestManager restManager = new RestManager();

	public Single<ResultDTO> uploadImage(String file, String mimetype, String title, String description, Integer price, String userid) {
		return restManager.uploadImage(file, mimetype, title, description, price, userid);
	}

	public Single<Bitmap> getImage(String imageid) {
		return restManager.getImage(imageid).map(it -> BitmapFactory.decodeStream(it.byteStream()));
	}
}
