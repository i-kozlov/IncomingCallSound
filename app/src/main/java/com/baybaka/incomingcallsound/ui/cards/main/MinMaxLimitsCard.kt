package com.baybaka.incomingcallsound.ui.cards.main

import android.support.annotation.Nullable
import android.support.v7.widget.SwitchCompat
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import butterknife.Bind
import butterknife.ButterKnife
import butterknife.OnClick
import com.appyvet.rangebar.RangeBar
import com.baybaka.incomingcallsound.MyApp
import com.baybaka.incomingcallsound.R
import com.baybaka.incomingcallsound.ui.cards.ListCardItem_v2
import com.baybaka.incomingcallsound.utils.Description


class MinMaxLimitsCard : ListCardItem_v2() {

    override val head = R.string.card_description_config_min_max_head
    override val layout = R.layout.card_config_min_max
    override val description = R.string.card_description_config_min_max_short
    override val descriptionFull = R.string.card_description_config_min_max_full

    override fun update() {
        maxHardwareLevel = MyApp.get().maxVol
        rangebar?.let { reconfigureRangebar() }

    }

    private fun reconfigureRangebar() {
        initRange()

        val onRangeBarChangeListener = RangeBar.OnRangeBarChangeListener { _, _, _, lefttext, rightText ->
            val left = Integer.parseInt(lefttext)
            val right = Integer.parseInt(rightText)

            mSharedPreferenceController.minVolumeLimit = left
            if (minLimitSwitch.isChecked) {
                updateMinLimitText(getMinLimitText(left))
            }
            //minLimitChanged(left);

            mSharedPreferenceController.maxVolumeLimit = right
            if (maxLimitSwitch.isChecked) {
                updateMaxLimitText(getMaxLimitText(right))

            }
            //                maxLimChanged(right);
        }
        //!!! after init
        rangebar?.setOnRangeBarChangeListener(onRangeBarChangeListener)
    }

    private var maxHardwareLevel: Int = 0

    @Bind(R.id.set_min_limit_toggle)
    internal lateinit var minLimitSwitch: SwitchCompat
    @Bind(R.id.enable_max_lovume_limit_toggle)
    internal lateinit var maxLimitSwitch: SwitchCompat

    @Bind(R.id.do_not_ring)
    internal lateinit var doNotRindSwitch: SwitchCompat

    @Bind(R.id.start_from_min_description)
    internal lateinit var minLevelDescriptionTextview: TextView

    @Bind(R.id.show_device_max_volume_level)
    internal lateinit var maxLimitTextview: TextView

    @Bind(R.id.skip_ring_descr_text)
    internal lateinit var skipRingDescriptionText: TextView

    @Nullable @JvmField
    @Bind(R.id.rangebar)
    internal  var rangebar: RangeBar? = null

    override fun init(view: View) {
        super.init(view)
        ButterKnife.bind(this, view)

        maxHardwareLevel = MyApp.get().maxVol
        reconfigureRangebar()

        minLimitSwitch.isChecked = mSharedPreferenceController.isMinVolumeLimited
        maxLimitSwitch.isChecked = mSharedPreferenceController.isMaxVolumeLimited
        doNotRindSwitch.isChecked = mSharedPreferenceController.isSkipRing

        setMinLimitDescriptionAccordingToSwitch()
        setMaxLimitDescAccordingToSwitch()
        skipDescriptionUpdate()
    }

    private fun skipDescriptionUpdate() {
        if (mSharedPreferenceController.isSkipRing)
            skipRingDescriptionText.visibility = VISIBLE
        else skipRingDescriptionText.visibility = GONE
    }


    //    private final int RANGEBAR_MIN = -1;
    private val RANGEBAR_MIN = 1

    private fun initRange() {
        rangebar?.tickStart = RANGEBAR_MIN.toFloat()
        rangebar?.tickEnd = (maxHardwareLevel + 1).toFloat()
        loadRangebarValues()
    }

    private fun loadRangebarValues() {
        var min = mSharedPreferenceController.minVolumeLimit
        var max = mSharedPreferenceController.maxVolumeLimit

        if (min < RANGEBAR_MIN) min = RANGEBAR_MIN
        if (min > maxHardwareLevel + 1) min = maxHardwareLevel + 1

        if (max < RANGEBAR_MIN) max = RANGEBAR_MIN
        if (max > maxHardwareLevel + 1) max = maxHardwareLevel + 1

        rangebar?.setRangePinsByValue(min.toFloat(), max.toFloat())
    }

    private fun minLimitChanged(newValue: Int) {
        updateMinLimitText(getMinLimitText(newValue))
        mSharedPreferenceController.minVolumeLimit = newValue
    }

    private fun maxLimChanged(newValue: Int) {
        updateMaxLimitText(getMaxLimitText(newValue))
        mSharedPreferenceController.maxVolumeLimit = newValue
    }


    private fun updateMinLimitText(text: String) {
        minLevelDescriptionTextview.text = text
    }

    private fun updateMaxLimitText(text: String) {
        maxLimitTextview.text = text
    }

    ////////text
    private fun setMinLimitDescriptionAccordingToSwitch() {
        if (minLimitSwitch.isChecked) {
            updateMinLimitText(getMinLimitText(mSharedPreferenceController.minVolumeLimit))
        } else {
            minLevelDescriptionTextview.text = MyApp.getContext().getString(R.string.start_from_min_false_description)
        }
    }

    private fun setMaxLimitDescAccordingToSwitch() {
        if (maxLimitSwitch.isChecked) {
            updateMaxLimitText(getMaxLimitText(mSharedPreferenceController.maxVolumeLimit))
        } else {
            maxLimitTextview.text = MyApp.getContext().getString(R.string.max_sound_level_text_disabled)
        }
    }

    private fun getMaxLimitText(newValue: Int): String {
        return context().getString(R.string.max_sound_level_text, Description.getVolumeLevelText(newValue, maxHardwareLevel))
    }

    private fun getMinLimitText(newValue: Int): String {
        return context().getString(R.string.start_from_min_true_description, Description.getVolumeLevelText(newValue, maxHardwareLevel))
    }

    @OnClick(R.id.set_min_limit_toggle)
    fun switchMinLimit() {
        //        rangebar.setEnabled(true);
        mSharedPreferenceController.enableMinVolumeLimit(minLimitSwitch.isChecked)
        setMinLimitDescriptionAccordingToSwitch()
    }

    @OnClick(R.id.enable_max_lovume_limit_toggle)
    fun switchMaxLimit() {
        //        rangebar.setEnabled(true);
        mSharedPreferenceController.toggleMaxVolumeLimit(maxLimitSwitch.isChecked)
        setMaxLimitDescAccordingToSwitch()
    }

    @OnClick(R.id.do_not_ring)
    fun doNotRingSwitch() {
        mSharedPreferenceController.isSkipRing = doNotRindSwitch.isChecked
        skipDescriptionUpdate()
    }

}
