package com.juniperbushes_99.hongry;

import java.util.List;

/**
 * Created by ucoleda on 9/23/15.
 */
public abstract class Search<T> {
    private String key;

    public abstract T search(String s);

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
