/*
 * Copyright (c) 2017 by k3b.
 *
 * This file is part of "Extended jpg Media-db Scanner" (media_scan_trace).
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package de.k3b.android.media.scanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** While not destroyed collect and display media-scanner-related broadcasts
 * from {@link ScannerReceiver} */
public class MediaScanTraceActivity extends Activity {
    private static final String SETTINGS_KEY = "Test-";

    // iso: "yyyy-MM-dd'T'HH:mm:ss"
    private static final DateFormat MessageTimeStampDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);

    /** used for static {@link #showEventInGui(Intent)} to forwad to current visible gui  */
    private static MediaScanTraceActivity currentVisibleInstance = null;

    private TextView messages = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messages = (TextView) findViewById(R.id.messages);

        currentVisibleInstance = this;

        if (savedInstanceState != null) {
            // recreate (i.e. after screen rotation)
            messages.setText(savedInstanceState.getString(SETTINGS_KEY));
        } else {
            // only if not re-created
            messages.setText("");

            selfTest();
        }
    }

    /** to check that ACTION_MEDIA_SCANNER_SCAN_FILE really can be received */
    private void selfTest() {
        final Intent scanTestBroadcastIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File("/does/not/exist/self-test.jpg")));
        final String debugMessage = "selfTest-sendBroadcast ";
        Log.i(Global.TAG, debugMessage + scanTestBroadcastIntent);
        showEventInGui(debugMessage, scanTestBroadcastIntent);
        this.sendBroadcast(scanTestBroadcastIntent);
    }

    @Override
    protected void onResume() {
        currentVisibleInstance = this;
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        currentVisibleInstance = null;
        super.onDestroy();
    }

    /** called by broadcastReceiver {@link ScannerReceiver} to monitor reived events. */
    public static void showEventInGui(Intent broadcastIntent) {
        if (currentVisibleInstance != null) {
            // if gui is available
            currentVisibleInstance.showEventInGui("onReceive", broadcastIntent);
        }
    }

    private void showEventInGui(String dbgContext, Intent broadcastIntent) {
        if (broadcastIntent != null) {
            messages.append(MessageTimeStampDateFormat.format(new Date()) + " "
                    + dbgContext + "\n\t"
                    + MediaScannerTools.formatLogMessage(this, broadcastIntent));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SETTINGS_KEY, messages.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

}
