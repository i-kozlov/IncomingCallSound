package com.baybaka.incomingcallsound.ui.main;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baybaka.incomingcallsound.BuildConfig;
import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.ui.drawer.MyNavView;
import com.baybaka.incomingcallsound.ui.tabs.TabsFragmentFactory;
import com.baybaka.incomingcallsound.ui.testpage.TestPageFragment;
import com.baybaka.increasingring.utils.PermissionChecker;
import com.baybaka.incomingcallsound.utils.RateApp;
import com.baybaka.increasingring.service.ServiceStarter;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            // Initializing Drawer Layout and ActionBarToggle
            DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

            //Initializing NavigationView
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(new MyNavView(drawerLayout, this));
            View header = navigationView.getHeaderView(0);
            TextView textView = (TextView) header.findViewById(R.id.app_version);
            textView.setText(String.format(getStr(), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));

            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.openDrawer, R.string.closeDrawer){

                @Override
                public void onDrawerClosed(View drawerView) {
                    // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                    super.onDrawerOpened(drawerView);
                }
            };

            //Setting the actionbarToggle to drawer layout
            drawerLayout.setDrawerListener(actionBarDrawerToggle);

            //calling sync state is necessay or else your hamburger icon wont show up
            actionBarDrawerToggle.syncState();
        }

        if (savedInstanceState == null) {
            Fragment fragment = getBaseFragment();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment, "fragmentt");
            transaction.commit();
        }

        new PermissionChecker(this).checkMain();

        //start if not running?
        ServiceStarter.startServiceWithCondition(this);
    }

    private String getStr() {
        return getResources().getString(R.string.drawer_app_version_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.open_test_page) {
            getSupportFragmentManager()
                    .beginTransaction().
                    replace(R.id.container, new TestPageFragment())
                    .commit();
            setTitle(getResources().getString(R.string.test_page));
        }

        if (item.getItemId() == R.id.rate_app) {
            RateApp.open(this);
        }

        return super.onOptionsItemSelected(item);
    }

    public Fragment getBaseFragment(){
        Fragment fragment;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            fragment = new TabsFragmentFactory.OldList();
        } else {
            fragment = new SlidingTabsBasicFragment();
        }

        return fragment;
    }



}
