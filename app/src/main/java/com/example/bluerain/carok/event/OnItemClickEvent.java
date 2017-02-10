package com.example.bluerain.carok.event;

/**
 * Created by bluerain on 17-2-9.
 */

public class OnItemClickEvent {
    public String key;
    public Object data;

    public OnItemClickEvent(String key, Object data) {
        this.key = key;
        this.data = data;
    }
}
