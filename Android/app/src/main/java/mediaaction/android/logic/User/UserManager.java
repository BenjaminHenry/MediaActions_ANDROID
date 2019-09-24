package mediaaction.android.logic.User;

import java.util.List;

import io.reactivex.Single;
import mediaaction.android.logic.Gallery.ImageDTO;
import mediaaction.android.logic.RestManager;
import mediaaction.android.logic.ResultDTO;

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

	public Single<ResultDTO> generatePasswordToken(String userid, String email) {
		return restManager.generatePasswordToken(userid, email);
	}

	public Single<ResultDTO> resetPassword(String token, String userid, String password) {
		return restManager.resetPassword(token, userid, password);
	}
}
