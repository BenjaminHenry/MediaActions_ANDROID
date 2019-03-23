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

/**
 * Unit test class
 */
public void class KnownRestExceptionTests {
	@Test
	public void KRETest {
		ErrorCode ec = "Test";
		int sc		 = 420;
		final String message="Unit test"
		final Throwable t=null;

		KnownRestException KRE = new KnownRestException(ec, sc);
		ErrorCode ecr = KRE.getErrorCode();
		int scr = KRE.getStatusCode();

		assertEquals(ec, ecr);
		assertEquals(sc, scr);

		RestException RE=new KnownRestException(message);
		String result=super.Throwable.getMessage();
		assertEquals(message,result);

		RestException RE=new KnownRestException(message, t);
		String result=super.Throwable.getMessage();
		Throwable res=super.Throwable.getCause();
		assertEquals(message,result);
		assertEquals(t, res);

		RestException RE = new KnownRestException(t);
		String result = super.Throwable.getCause();
		assertEquals(t, result);
	}
}