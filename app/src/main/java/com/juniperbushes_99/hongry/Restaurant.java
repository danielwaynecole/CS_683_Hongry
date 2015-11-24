package com.juniperbushes_99.hongry;

import java.util.HashMap;

/**
 * Created by ucoleda on 9/23/15.
 */
public class Restaurant {
    private String id;
    private String name;
    private String json;

    public Restaurant(HashMap<String, String> hmap){
        this.setName(hmap.get("title"));
        this.setId(hmap.get("id"));
        this.setJson(hmap.get("json"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
