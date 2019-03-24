package mediaaction.android;

import org.junit.*;

import mediaaction.android.core.EnumUtils;


public class EnumUtilsTests {

    @Test (expected = java.lang.IllegalAccessException.class)
    public void EUTest () throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class cls = Class.forName("mediaaction.android.core.EnumUtils");
        cls.newInstance();
    }

    @Test
    public void EU2Test () {
        EnumUtils.fromKey(null, null);
        EnumUtils.fromKey(null, null, null);
        EnumUtils.fromName(null, null);
        EnumUtils.fromName(null, null, null);
    }
}
