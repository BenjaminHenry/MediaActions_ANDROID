package mediaaction.android.logic.User;

import java.util.List;

import io.reactivex.Single;
import mediaaction.android.core.ImageDTO;
import mediaaction.android.core.StatDTO;
import mediaaction.android.logic.RestManager;

public class UserManager {

	private RestManager restManager = new RestManager();

	public Single<StatDTO> getAvgSellsPrice(String userid) {
		return restManager.getAvgSellsPrice(userid);
	}

	public Single<List<ImageDTO>> getSoldPhotos(String userid) {
		return restManager.getSoldPhotos(userid);
	}

	public Single<List<ImageDTO>> getUserUploads(String userid) {
		return restManager.getUserUploads(userid);
	}
}
