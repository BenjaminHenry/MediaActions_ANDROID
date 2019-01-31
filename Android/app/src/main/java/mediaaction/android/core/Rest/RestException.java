package mediaaction.android.core.Rest;

import java.io.IOException;

public class RestException extends IOException {

	public RestException() {
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public RestException(Throwable throwable) {
		super(throwable);
	}

}