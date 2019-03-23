package mediaaction.android.core.Rest;

import java.io.IOException;
import org.junit.Assert.*;

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

/**
 * Unit test class
 */
public final class RestExceptionTest {
	@Test
	public void RETest{
		final String message="Unit test"
		final Throwable t=null;

		RestException RE=new RestException(message);
		String result=super.Throwable.getMessage();
		assertEquals(message,result);

		RestException RE=new RestException(message, t);
		String result=super.Throwable.getMessage();
		Throwable res=super.Throwable.getCause();
		assertEquals(message,result);
		assertEquals(t, res);

		RestException RE = new RestException(t);
		String result = super.Throwable.getCause();
		assertEquals(t, result);
	}
}