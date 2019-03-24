package mediaaction.android.core.Rest;

import java.io.IOException;

/**
 * RestException implements the different constructors of IOException
 * IOException signals that an I/O exception of some sort has occurred.
 * This class is the general class of exceptions produced by failed or interrupted I/O operations.
 */
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