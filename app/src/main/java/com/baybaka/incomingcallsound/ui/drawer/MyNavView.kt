package com.baybaka.incomingcallsound.ui.drawer

import android.content.Intent
import android.net.Uri
import android.support.annotation.StringRes
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.ahmedjazzar.rosetta.LanguageSwitcher
import com.baybaka.incomingcallsound.MyApp
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.log.logsender.EmailIntentCreator
import com.baybaka.incomingcallsound.ui.main.MainActivity
import com.baybaka.incomingcallsound.ui.tabs.TabsFragmentFactory
import com.baybaka.incomingcallsound.ui.testpage.TestPageFragment
import com.baybaka.incomingcallsound.utils.RateApp
import java.util.*


class MyNavView(private val drawerLayout: DrawerLayout,
                private val context: AppCompatActivity) : NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        //Checking if the item is in checked state or not, if not make it in checked state
        if (menuItem.isChecked)
            menuItem.isChecked = false
        else
            menuItem.isChecked = true

        val settings = (context.application as MyApp).setting

        drawerLayout.closeDrawers()


        fun replaceFragment(fragment: Fragment) {
            context.supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
        }

        val updateTitle: (id: Int) -> Unit = fun(@StringRes id: Int) { context.title = context.resources.getString(id) }

        when (menuItem.itemId) {

            R.id.phone_config_page -> {

                replaceFragment((context as MainActivity).baseFragment)
                updateTitle(R.string.app_name)
                return true
            }

            R.id.phone_yes_config_page -> {

                replaceFragment(TabsFragmentFactory.YesCardsTab())
                updateTitle(R.string.app_name)
                return true
            }

            R.id.nav_switch_mod -> {
                val modeToSet = !settings.useSimpleMode()
                settings.smartResetConfig()
                settings.setSimpleMode(modeToSet)
                replaceFragment((context as MainActivity).baseFragment)
                updateTitle(R.string.app_name)
                return true
            }

            R.id.open_test_page -> {
                replaceFragment(TestPageFragment())
                updateTitle(R.string.test_page)
                return true
            }

            R.id.navigation_rate_app -> {
                RateApp.open(context)
                return true
            }

            R.id.navigation_to_web_help -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getLink()))
                context.startActivity(browserIntent)
                return true
            }

            R.id.navigation_feedback -> {
                try {
                    context.startActivity(EmailIntentCreator.getSendEmailIntent("From navbar", "", context))
                } catch (e: Exception) {
                    Toast.makeText(context, R.string.error_noapp_tosend_email, Toast.LENGTH_LONG).show()
                }

                return true
            }
            R.id.lang ->{
                LanguageSwitcher(context).showChangeLanguageDialog(context)
                return true
            }

        //            case R.id.navigation_about:
        //
        //                return true;
            else -> {
                Toast.makeText(context.applicationContext, "Something is Wrong", Toast.LENGTH_SHORT).show()
                return true
            }
        }
    }

    private fun getLink(): String {

        return when (Locale.getDefault().displayLanguage) {
            "русский" -> "https://goo.gl/LHZBfZ"
            else -> "https://goo.gl/Z04Co2"
        }


    }
}
