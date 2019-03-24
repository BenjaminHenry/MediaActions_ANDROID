package mediaaction.android;

import android.app.Application;
import android.content.Context;

import org.junit.Test;

import mediaaction.android.logic.Gallery.GalleryManager;

public class GalleryManagerTests {
    @Test
    public void GMTests () {
        byte arr[] = new byte[] {1, 6, 3};
        try {
            Application a = new Application();
            Context c = a.getApplicationContext();
            GalleryManager gm = new GalleryManager(c);
            gm.uploadImage(arr, "a", "a", "a", 10, "10");
            gm.getImage("1");
        } catch (Exception e) {

        }
    }
}
