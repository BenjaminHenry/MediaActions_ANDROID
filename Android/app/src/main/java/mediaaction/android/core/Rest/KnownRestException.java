package mediaaction.android.core.Rest;

/**
 * KnownRestException implements RestException's constructors that implement IOException's constructor.
 * It also sets two variables each time which are error code and satuts code.
 */
public class KnownRestException extends RestException {

	private final ErrorCode errorCode;
	private final int statusCode;

	public KnownRestException(ErrorCode errorCode, int statusCode) {
		this.errorCode = errorCode;
		this.statusCode = statusCode;
	}

	public KnownRestException(String message, ErrorCode errorCode, int statusCode) {
		super(message);
		this.errorCode = errorCode;
		this.statusCode = statusCode;
	}

	public KnownRestException(String message, Throwable throwable, ErrorCode errorCode, int statusCode) {
		super(message, throwable);
		this.errorCode = errorCode;
		this.statusCode = statusCode;
	}

	public KnownRestException(Throwable throwable, ErrorCode errorCode, int statusCode) {
		super(throwable);
		this.errorCode = errorCode;
		this.statusCode = statusCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public int getStatusCode() {
		return statusCode;
	}
}