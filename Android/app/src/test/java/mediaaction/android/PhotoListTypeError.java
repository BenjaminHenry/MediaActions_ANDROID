package mediaaction.android;

import mediaaction.android.core.PhotoListType;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * PhotoListTypeError is the unit test assigned to PhotoListType
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
