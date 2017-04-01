package edu.ucf.cop4331c.dishdriver.network;

import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * Created by copper on 4/1/17.
 */

public class NotificationService {

    public static void broadcast(String title, String body) {

        body  = body.replaceAll("'", "");
        title = title.replaceAll("'", "");

        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'" + body + "'}, 'headings': {'en': '" + title + "'}}"), null);
        } catch (Exception e) { }

    }

}
