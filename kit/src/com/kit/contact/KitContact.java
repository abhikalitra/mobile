package com.kit.contact;

import java.util.Date;

public class KitContact {

    private long id;
    private String contactId;
    private String name;
    private ConnectFreq freq;
    private Date lastCall;
    private boolean enabled;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public ConnectFreq getFreq() {
        return freq;
    }

    public void setFreq(ConnectFreq freq) {
        this.freq = freq;
    }

    public Date getLastCall() {
        return lastCall;
    }

    public void setLastCall(Date lastCall) {
        this.lastCall = lastCall;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public CharSequence getLastCallString() {

        if (lastCall == null) return "Never";
        return KitContactDatabase.sdf.format(lastCall);
    }

}
