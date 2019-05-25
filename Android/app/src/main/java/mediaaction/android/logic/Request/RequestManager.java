package mediaaction.android.logic.Request;

import android.content.Context;

import java.util.List;

import io.reactivex.Single;
import mediaaction.android.logic.RestManager;

public class RequestManager {

	private Context context;
	private RestManager restManager = new RestManager();

	public RequestManager(Context _context) {
		context = _context;
	}

	public Single<List<RequestDTO>> getRequests() {
		return restManager.getRequests();
	}
}
