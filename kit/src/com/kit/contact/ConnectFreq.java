package com.kit.contact;

/**
 * Created with IntelliJ IDEA.
 * User: sushil
 * Date: 11/27/12
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ConnectFreq {

    DAILY("1d",1),
    WEEKY("1w",7),
    BIWEEKLY("2w",14),
    MONTHLY("1m",30),
    QUARTERLY("3m",90),
    HALFYEARLY("6m",180),
    YEARLY("1y",365);

    private String txt;
    private int days;

    ConnectFreq(String text, int days) {
        this.txt = text;
        this.days = days;
    }

    public String txt() {
        return txt;
    }

    public static ConnectFreq locate(String string) {
        for (ConnectFreq f : ConnectFreq.values()) {
            if (f.txt().equals(string)) return f;
        }
        return WEEKY;
    }

    public int days() {
        return days;
    }
}
