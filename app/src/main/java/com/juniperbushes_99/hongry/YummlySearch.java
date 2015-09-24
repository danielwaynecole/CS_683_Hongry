package com.juniperbushes_99.hongry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucoleda on 9/23/15.
 */
public class YummlySearch extends Search {

    public List<Recipe> search(String keyword){
        List<Recipe> recipes = getRecipesFromYummly();

        return recipes;
    }

    private List<Recipe> getRecipesFromYummly(){
        List<Recipe> recipes = new ArrayList<Recipe>();
        // TODO implement call to Yelp API

        return recipes;
    }
}

