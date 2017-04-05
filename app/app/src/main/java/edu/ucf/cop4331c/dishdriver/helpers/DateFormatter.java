package edu.ucf.cop4331c.dishdriver.helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by copper on 4/3/17.
 */

public class DateFormatter {
    public static String forSQL(Date date) {
        return (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.mmm")).format(date);
    }
}
