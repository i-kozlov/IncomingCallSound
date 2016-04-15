package com.baybaka.incomingcallsound.ui.drawer;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.baybaka.incomingcallsound.R;
import com.baybaka.incomingcallsound.log.logsender.EmailIntentCreator;
import com.baybaka.incomingcallsound.ui.main.MainActivity;
import com.baybaka.incomingcallsound.ui.tabs.TabsFragmentFactory;
import com.baybaka.incomingcallsound.ui.testpage.TestPageFragment;
import com.baybaka.incomingcallsound.utils.RateApp;

public class MyNavView implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private AppCompatActivity context;

    public MyNavView(DrawerLayout drawerLayout, AppCompatActivity context) {
        this.drawerLayout = drawerLayout;
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);

        drawerLayout.closeDrawers();
        Fragment fragment;
        FragmentTransaction fragmentTransaction;

        switch (menuItem.getItemId()) {

            case R.id.phone_config_page:
                fragment = new MainActivity().getBaseFragment();
                fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                context.setTitle(context.getResources().getString(R.string.app_name));
                return true;

            case R.id.phone_yes_config_page:
                fragment = new TabsFragmentFactory.YesCardsTab();
                fragmentTransaction = (context).getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                context.setTitle(context.getResources().getString(R.string.app_name));
                return true;

            case R.id.open_test_page:

                fragment = new TestPageFragment();
                fragmentTransaction = context.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.commit();
                context.setTitle(context.getResources().getString(R.string.test_page));
                return true;

            case R.id.navigation_rate_app:
                RateApp.open(context);
                return true;

            case R.id.navigation_feedback:
                try {
                    context.startActivity(EmailIntentCreator.getSendEmailIntent("From navbar", ""));
                } catch (Exception e) {
                    Toast.makeText(context, R.string.error_noapp_tosend_email, Toast.LENGTH_LONG).show();
                }
                return true;

//            case R.id.navigation_about:
//
//                return true;
            default:
                Toast.makeText(context.getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                return true;
        }
    }
}
