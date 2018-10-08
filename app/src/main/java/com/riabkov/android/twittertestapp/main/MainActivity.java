package com.riabkov.android.twittertestapp.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.riabkov.android.twittertestapp.R;
import com.riabkov.android.twittertestapp.base.BaseActivity;
import com.riabkov.android.twittertestapp.favourite.FavoriteFragment;
import com.riabkov.android.twittertestapp.nearby.NearbyFragment;
import com.riabkov.android.twittertestapp.search.SearchFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentStarted(FragmentManager fm, Fragment f) {
                super.onFragmentStarted(fm, f);
                if(f instanceof SearchFragment){
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }, true);

        if(savedInstanceState == null){
            navigationView.getMenu().getItem(0).setChecked(true);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content, SearchFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onViewDestroy() {

    }

    @Override
    protected int onRequestLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitializeInjection() {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (item.getItemId()) {
            case  R.id.nav_search:
                fragmentClass = SearchFragment.class;
                break;
            case R.id.nav_nearby:
                fragmentClass = NearbyFragment.class;
                break;

            case R.id.nav_favourite:
                fragmentClass = FavoriteFragment.class;
                break;
        }
        if(fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);

        if(currentFragment.getClass() != fragmentClass){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, fragment)
                    .commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
