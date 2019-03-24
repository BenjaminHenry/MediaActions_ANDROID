package mediaaction.android;

import org.junit.Test;

import mediaaction.android.core.Rest.RestException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test class
 */
public final class RestExceptionTest {
    @Test
    public void RETest () {

        RestException n = new RestException();
        assertNull(n.getMessage());
        final String message="Unit test";
        final Throwable t = null;

        RestException RE=new RestException(message);
        String result=RE.getMessage();
        assertEquals(message,result);

        RestException RE2=new RestException(message, t);
        String result2= RE2.getMessage();
        Throwable res=RE2.getCause();
        assertEquals(message,result2);
        assertEquals(t, res);

        RestException RE3 = new RestException(t);
        Throwable result3 = RE3.getCause();
        assertEquals(t, result3);
    }
}
