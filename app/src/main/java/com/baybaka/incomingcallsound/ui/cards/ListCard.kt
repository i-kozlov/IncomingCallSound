package com.baybaka.incomingcallsound.ui.cards

import android.view.View
import com.baybaka.incomingcallsound.ui.rv.RVAdapter

interface ListCard {

    /*
    * values as used to pass params to RecyclerView
    * */
    val head: Int
    val layout: Int
    val description: Int
    val descriptionFull: Int
    fun isBetaFeature(): Boolean = false

    fun init(view: View)

    /**
     * to get activity context
     * @param rvAdapter
     */
    fun link(rvAdapter: RVAdapter)

    /**
     * to get update from recycler if needed
     */
    fun update()
}