package edu.ucf.cop4331c.dishdriver;

import org.junit.Test;

import edu.ucf.cop4331c.dishdriver.network.NotificationService;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NotificationTests {

    @Test
    public void TestANotification() throws Exception {
        NotificationService.broadcast("NotificationTests.TestANotification", "'''' There should be no single quotes at the beginning of this message.");
    }

}