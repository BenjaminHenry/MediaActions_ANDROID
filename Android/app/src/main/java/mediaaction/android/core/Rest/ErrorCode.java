package mediaaction.android.core.Rest;

import android.support.annotation.StringRes;

import mediaaction.android.R;
import mediaaction.android.core.EnumFromWS;

public enum ErrorCode implements EnumFromWS {
	ERROR_ACCESS_DENIED("Not found", R.string.error_access_denied),
	ERROR_UNKNOWN_ERROR("Unknown error", R.string.error_unknown_error);

	private final String key;
	private final int stringRes;

	ErrorCode(String key, @StringRes int stringRes) {
		this.key = key;
		this.stringRes = stringRes;
	}

	public String getKey() {
		return key;
	}

	@StringRes
	public int getStringRes() {
		return stringRes;
	}
}
