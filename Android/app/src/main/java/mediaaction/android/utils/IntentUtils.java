package mediaaction.android.utils;

import android.content.Intent;

public class IntentUtils {

	public static final int FLAGS_CLEAN = (Intent.FLAG_ACTIVITY_CLEAR_TOP |
			Intent.FLAG_ACTIVITY_SINGLE_TOP |
			Intent.FLAG_ACTIVITY_CLEAR_TASK |
			Intent.FLAG_ACTIVITY_NEW_TASK);
}
