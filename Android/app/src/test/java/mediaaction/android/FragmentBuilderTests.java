package mediaaction.android;

import org.junit.Test;

import mediaaction.android.logic.FragmentBuilder;

public class FragmentBuilderTests {
    @Test
    public void FBTests () {
        try {
            FragmentBuilder fb = new FragmentBuilder(null);
            fb.build();
            fb.put("key", "");
            fb.put("key", 12);
            fb.put("key", 'a');
            fb.put("key", true);
        } catch (Exception e) {}
    }
}
