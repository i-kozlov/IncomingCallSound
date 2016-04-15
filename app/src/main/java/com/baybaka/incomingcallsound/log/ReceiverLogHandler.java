package com.baybaka.incomingcallsound.log;

import com.baybaka.incomingcallsound.MyApp;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import pl.brightinventions.slf4android.MessageValueSupplier;

public class ReceiverLogHandler extends Handler {

    MessageValueSupplier messageValueSupplier = new MessageValueSupplier();

    @Override
    public void close() {
    }

    @Override
    public void flush() {

    }

    @Override
    public void publish(LogRecord record) {
        if (record != null && MyApp.get().getRunTimeChanges().isTestPageOpened()) {
            pl.brightinventions.slf4android.LogRecord logRecord = pl.brightinventions.slf4android.LogRecord.fromRecord(record);
            StringBuilder messageBuilder = new StringBuilder();
            messageValueSupplier.append(logRecord, messageBuilder);
            if (messageBuilder.toString().startsWith("Processing switch")) {
                messageBuilder.insert(0,"====================\n");
            }
            LogReceiver.sendBroadcastToLogReceiver(messageBuilder.toString());
        }
    }
}
