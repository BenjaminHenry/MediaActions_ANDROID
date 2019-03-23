package mediaaction.android.core;

import java.io.Serializable;
import org.junit.Assert.*

/**
 *  PhotoListType enulerates the different states of a picture
 *  These can be SOLD, UPLOADED or EMPTY
 */
public enum PhotoListType implements Serializable {
	SOLD,
	UPLOADED,
	EMPTY
}

/**
 * PhotoListTypeError is the unit test calss assigned to PhotoListType
 * It checks if every wanted value of the PhotoListType exist.
 */
public final class PhotoListTypeError {
	@Test
	public void PhotoError() {
		assertThat(PhotoListType.EMPTY, is(notNullValue()));
		assertThat(PhotoListType.UPLOADED, is(notNullValue()));
		assertThat(PhotoListType.SOLD, is(notNullValue()));
	}
}