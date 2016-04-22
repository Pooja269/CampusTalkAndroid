package com.campustalk.developer.campustalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

        LinearLayout llMyProfile = (LinearLayout) headerview.findViewById(R.id.ll_myprofile);
        llMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationActivity.this,MyProfileActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.config_settings),MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String department = sharedPreferences.getString("department","");

        ((TextView) headerview.findViewById(R.id.tv_name)).setText(name);
        ((TextView) headerview.findViewById(R.id.tv_department)).setText(department+" Engineering");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new DashboardFragment();
        fragmentManager.beginTransaction().replace(R.id.frame,fragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
         if(id == R.id.nav_dashboard){
             Fragment fragment = new DashboardFragment();
             fragmentManager.beginTransaction().replace(R.id.frame,fragment).commit();
         }
        else if (id == R.id.nav_documents) {
            // Handle the notice action
             Fragment fragment = new DocumentsDownloadFragment();
             fragmentManager.beginTransaction().replace(R.id.frame,fragment).commit();
             actionBar.setTitle("My Documents");

        }  else if (id == R.id.nav_feedbackForm) {
            Fragment fragment=new FeedbackFormFragment();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).commit();
             actionBar.setTitle("Feedback");

        } else if (id == R.id.nav_complainForm) {
            Fragment fragment = new ComplainFormFragment();
            fragmentManager.beginTransaction().replace(R.id.frame,fragment).commit();
             actionBar.setTitle("Complain");


        }
        else if (id == R.id.nav_logout){
             startActivity(new Intent(this,LoginActivity.class));
         }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
