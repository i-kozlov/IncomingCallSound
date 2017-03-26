package com.baybaka.incomingcallsound.ui.main


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.baybaka.incomingcallsound.BuildConfig
import com.baybaka.incomingcallsound.MyApp
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.ui.drawer.MyNavView
import com.baybaka.incomingcallsound.ui.testpage.TestPageFragment
import com.baybaka.incomingcallsound.utils.RateApp
import com.baybaka.increasingring.service.ServiceStarter
import com.baybaka.increasingring.utils.PermissionChecker
import com.baybaka.increasingring.utils.PermissionChecker.Companion.dndReqCode


class MainActivity : AppCompatActivity() {

//    @Inject
//    private lateinit var  mSharedPreferenceController: AllSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.layout_activity_main)

        initNavBar()
        if (savedInstanceState == null) {
            val fragment = baseFragment

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment, "fragmentt")
            transaction.commit()
        }

        PermissionChecker(this).checkPhonePermissionsAndAsk()

        //start if not running?
        ServiceStarter.startServiceWithCondition(this)
    }

    private fun initNavBar() {

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        // Initializing Drawer Layout and ActionBarToggle
        val drawerLayout = findViewById(R.id.drawer) as DrawerLayout

        //naview implements listener and holds menu config
        val navView = MyNavView(drawerLayout, this)

        //Initializing NavigationView
        val navigationView = findViewById(R.id.navigation_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(navView)
        val header = navigationView.getHeaderView(0)
        val textView = header.findViewById(R.id.app_version) as TextView
        textView.text = String.format(str, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            override fun onDrawerClosed(drawerView: View?) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView)
            }

            override fun onDrawerOpened(drawerView: View?) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView)
            }
        }

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle)

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState()


    }

    private val str: String
        get() = resources.getString(R.string.drawer_app_version_text)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main_new, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.open_test_page -> {
                supportFragmentManager
                        .beginTransaction().replace(R.id.container, TestPageFragment())
                        .commit()
                title = resources.getString(R.string.test_page)
            }
            R.id.rate_app -> RateApp.open(this)
        }

        return super.onOptionsItemSelected(item)
    }


    val baseFragment: Fragment
        get() {
            val fragment: Fragment =
                    if ((application as MyApp).setting.useSimpleMode()) {
                        SimpleFragment()
                    } else {
                        SlidingTabsBasicFragment()
                    }

            return fragment
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == dndReqCode) {
            //todo this should be updating Fragment
            this.recreate()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        val update = grantResults.any { it == PackageManager.PERMISSION_GRANTED }
        if (update) {
            //todo this should be updating Fragment
            this.recreate()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
