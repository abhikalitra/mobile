package com.kit.contact;

public class ListenerFactory {

    private static ListenerFactory listenerFactory = new ListenerFactory();
    private KitRefreshListener listener;

    public static ListenerFactory getInstance() {
         return listenerFactory;
    }

    public void setListener(KitRefreshListener listener) {
        this.listener = listener;
    }

    public void refresh() {
        if (listener != null) listener.refresh();
    }
}
