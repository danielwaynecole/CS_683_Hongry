package com.juniperbushes_99.hongry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ucoleda on 9/23/15.
 */
public class YelpSearch extends Search<List> {
    public List<Restaurant> search(String keyword){
        List<Restaurant> restaurants = getRestaurantsFromYelp();

        return restaurants;
    }

    private List<Restaurant> getRestaurantsFromYelp(){
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        // TODO implement call to Yelp API

        return restaurants;
    }
}
