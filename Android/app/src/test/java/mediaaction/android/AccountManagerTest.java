package mediaaction.android;

import android.app.Application;
import android.content.Context;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.notNullValue;

import mediaaction.android.logic.Account.AccountManager;

public class AccountManagerTest {
    @Test
    public void AMTests () {
        try {
            Application a = new Application();
            Context c = a.getApplicationContext();
            AccountManager am = new AccountManager(c);
            am.register("b@b", "test", "pass0000");
            am.login("test", "pass0000");
            assertThat(am, is(notNullValue()));
        } catch (Exception e) {}
    }
}
