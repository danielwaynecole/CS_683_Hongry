package com.juniperbushes_99.hongry;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements RestFaveStart.OnFragmentInteractionListener,
        RecFaveStart.OnFragmentInteractionListener,
        RecNewStart.OnFragmentInteractionListener,
        RecStart.OnFragmentInteractionListener,
        RestStart.OnFragmentInteractionListener,
        RestNewStart.OnFragmentInteractionListener,
        RestaurantDetails.OnFragmentInteractionListener,
        HungryHome.OnFragmentInteractionListener,
        RecipeDetailsButtons.OnFragmentInteractionListener,
        RecList.OnFragmentInteractionListener,
        RestList.OnFragmentInteractionListener {
    private static final String TAG = "Home";
    private DrawerLayout mDrawer;
    private Menu menu;
    private ActionBarDrawerToggle mDrawerToggle;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set a Toolbar to replace the ActionBar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawer.setDrawerListener(mDrawerToggle);

        // Find our drawer view
        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);

        // Setup drawer view
        setupDrawerContent(nvDrawer);

        // Handle for menu
        menu = nvDrawer.getMenu();

        // Insert the home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragmentManager.beginTransaction().replace(R.id.flContent, HungryHome.class.newInstance()).commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar hamburger should open or close menu.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment for the content view
        // this is for when an item in the menu is clicked on
        Fragment fragment = null;

        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_fave_recipe:
                fragmentClass = RecFaveStart.class;
                break;
            case R.id.nav_fave_restuarant:
                fragmentClass = RestFaveStart.class;
                break;
            case R.id.nav_new_recipe:
                fragmentClass = RecNewStart.class;
                break;
            case R.id.nav_new_restuarant:
                fragmentClass = RestNewStart.class;
                break;
            default:
                fragmentClass = HungryHome.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onFragmentInteraction(String route, String data ) {
        // this changes the display in the main content view
        // or some sub fragment within a displayed fragement
        // it also changes the title, if necessary
        // and changes the display of a menu to selected (if it's an item you can navigate to from menu)
        Fragment fragment = null;
        MenuItem m;
        Class fragmentClass;
        Bundle args = new Bundle();
        FragmentTransaction transaction;
        int replaceID = R.id.flContent;
        boolean addToBS = true;
        // main router, indicates appropriate fragment
        switch (route) {
            case "eatInStart":
                fragmentClass = RecStart.class;
                setTitle(R.string.eatInStartTitle);
                break;
            case "eatOutStart":
                fragmentClass = RestStart.class;
                setTitle(R.string.eatOutStartTitle);
                break;
            case "recFaves":
                fragmentClass = RecFaveStart.class;
                m = menu.findItem(R.id.nav_fave_recipe);
                m.setChecked(true);
                setTitle(m.getTitle());
                break;
            case "recNew":
                fragmentClass = RecNewStart.class;
                m = menu.findItem(R.id.nav_new_recipe);
                m.setChecked(true);
                setTitle(m.getTitle());
                break;
            case "restFaves":
                fragmentClass = RestFaveStart.class;
                m = menu.findItem(R.id.nav_fave_restuarant);
                m.setChecked(true);
                setTitle(m.getTitle());
                break;
            case "restNew":
                fragmentClass = RestNewStart.class;
                m = menu.findItem(R.id.nav_new_recipe);
                m.setChecked(true);
                setTitle(m.getTitle());
                break;
            case "recDetails":
                fragmentClass = RecipeDetails.class;
                args.putString("recipeID", data);
                setTitle(R.string.recDetailsTitle);
                break;
            case "restDetails":
                fragmentClass = RestaurantDetails.class;
                args.putString("restaurantData", data);
                setTitle(R.string.restDetailsTitle);
                break;
            case "ingredients":
                fragmentClass = RecipeDetailsIngredients.class;
                replaceID = R.id.recipeDetailsBodyFragmentContainer;
                args.putString("ingredientList", data);
                addToBS = false;
                break;
            case "nutinfo":
                fragmentClass = RecipeDetailsNutritionalInfo.class;
                replaceID = R.id.recipeDetailsBodyFragmentContainer;
                args.putString("nutritionList", data);
                addToBS = false;
                break;
            case "recipeList":
                fragmentClass = RecList.class;
                args.putString("recipeList", data);
                setTitle(R.string.recListTitle);
                break;
            case "restList":
                fragmentClass = RestList.class;
                args.putString("restaurantList", data);
                setTitle(R.string.restListTitle);
                break;
            case "restMap":
                fragmentClass = RestMap.class;
                args.putString("coordinates", data);
                setTitle(R.string.restMapTitle);
                break;
            default:
                fragmentClass = HungryHome.class;
                setTitle(R.string.homeTitle);
        }
        // instatiate the indicated fragment
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        // add fragment view to back stack if indicated
        if(addToBS) {
            transaction.addToBackStack(null);
        }
        // load the fragment
        transaction.replace(replaceID, fragment).commit();
    }
}