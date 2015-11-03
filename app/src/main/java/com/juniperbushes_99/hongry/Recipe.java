package com.juniperbushes_99.hongry;

import java.util.HashMap;

/**
 * Created by ucoleda on 9/23/15.
 */
public class Recipe {

    private String title;
    private String id;

    public Recipe(String id){
        this.setId(id);
    }

    public Recipe(HashMap<String, String> hmap){
        this.setTitle(hmap.get("title"));
        this.setId(hmap.get("id"));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return getTitle();
    }
}
