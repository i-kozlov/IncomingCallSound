package com.baybaka.incomingcallsound.ui.cards

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.Toast
import butterknife.Bind
import butterknife.OnClick
import com.baybaka.incomingcallsound.MyApp
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.log.logsender.EmailIntentCreator
import com.baybaka.incomingcallsound.settings.AllSettings
import com.baybaka.incomingcallsound.ui.rv.RVAdapter

interface ListCard {

    /*
    * values as used to pass params to RecyclerView
    * */
    val head: Int
    val layout: Int
    val description: Int
    val descriptionFull: Int
    val isBetaFeature: Boolean

    fun init(view: View) {}

    /**
     * to get activity context
     * @param rvAdapter
     */
    fun link(rvAdapter: RVAdapter) {
    }

    /**
     * to get update from recycler if needed
     */
    fun update() {
    }
}


abstract class ListCardItem_v2 : ListCard {
    override val isBetaFeature: Boolean = false
    protected lateinit var rvAdapter: RVAdapter

    override fun link(rvAdapter: RVAdapter) {
        this.rvAdapter = rvAdapter
    }

    companion object {
        val mSharedPreferenceController: AllSettings = MyApp.get().listenerComponent.settings()
    }

    fun context(): Context = rvAdapter.context
    fun appContext() = rvAdapter.context.applicationContext as MyApp
    fun activity() = rvAdapter.context as? Activity
}

enum class Restrict {
    NONE,
    WARNING,
    ERROR
}


abstract class BetaCard_v2 : ListCardItem_v2() {

    override val isBetaFeature: Boolean = true

    @Bind(R.id.send_email_beta_good)
    internal lateinit var betaGood: Button

    @Bind(R.id.send_email_beta_bad)
    internal lateinit var betaBad: Button

    @OnClick(R.id.send_email_beta_good, R.id.send_email_beta_bad)
    fun sendEmail(button: Button) {

        val buttonName = when {
            button.id == R.id.send_email_beta_good -> "Good button"
            button.id == R.id.send_email_beta_bad -> "Bad button"
            else -> ""
        } + " for ${rvAdapter.context.getString(head)}"

        try {
            val intent = EmailIntentCreator.getSendEmailIntent("Beta review", buttonName, context())
            rvAdapter.context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context(), R.string.error_noapp_tosend_email, Toast.LENGTH_LONG).show()
        }

    }


}

