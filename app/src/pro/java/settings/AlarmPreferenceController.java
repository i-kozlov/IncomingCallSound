package settings;

import android.content.Context;

import com.baybaka.incomingcallsound.settings.PhonePreferenceController;

import javax.inject.Inject;

public class AlarmPreferenceController extends PhonePreferenceController {

    @Inject
    public AlarmPreferenceController(Context appContext) {
        super(appContext);
        prefix = "alarm_";
    }
}
