package edu.ucf.cop4331c.dishdriver.helpers;

/**
 * Created by copper on 4/3/17.
 */

public class MoneyFormatter {

    public enum Format {
        DollarsAndCents,
        JustDollars,
        JustCents
    }

    public static String format(int cents) {
        return format(Format.DollarsAndCents, cents);
    }

    public static String format(Format fmt, int cents) {
        switch (fmt) {
            case JustDollars:
                return String.format("%02d", dollarsOf(cents));
            case JustCents:
                return String.format("%02d", centsOf(cents));
            case DollarsAndCents:
            default:
                return String.format("%d.%02d", dollarsOf(cents), centsOf(cents));
        }
    }

    public static int dollarsOf(int cents) {
        return (int)Math.floor(cents / 100);
    }

    public static int centsOf(int cents) {
        return cents % 100;
    }
}
