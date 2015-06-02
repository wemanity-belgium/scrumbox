package com.wemanity.scrumbox.android.time;

import com.wemanity.scrumbox.android.BuildConfig;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,emulateSdk = 21)
public class TimeHelperTest {

    @Before
    public void setUp() throws Exception {
        // setup
    }

    @Test
    public void testFormatTimestamp_timeLabel() throws Exception {
        long timestamp = 1433253961;
        String formattedTimestamp = TimeHelper.formatTimestamp(timestamp, TimeHelper.timeLabel);
        Assert.assertEquals("+ 23887:33:961",formattedTimestamp);
    }

    @Test
    public void testFormatTimestamp_timeLabelWithoutSymbole() throws Exception {
        long timestamp = 1433253961;
        String formattedTimestamp = TimeHelper.formatTimestamp(timestamp, TimeHelper.timeLabelWithoutSymbole);
        Assert.assertEquals("23887:33:961", formattedTimestamp);
    }
}