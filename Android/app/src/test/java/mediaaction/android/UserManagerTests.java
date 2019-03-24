package mediaaction.android;

import org.junit.Test;

import mediaaction.android.logic.User.UserManager;

public class UserManagerTests {
    @Test
    public void UMTests () {
        UserManager um = new UserManager();
        um.getAvgSellsPrice("1");
        um.getSoldPhotos("1");
        um.getUserUploads("1");
    }
}
