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
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

    private static final String NEWLINE = "\n";
    private static final String NEWLINE_TAB = NEWLINE + "\t";

    public static Uri SQL_TABLE_EXTERNAL_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    public static Uri SQL_TABLE_EXTERNAL_CONTENT_URI_FILE = MediaStore.Files.getContentUri("external");

    // iso: "yyyy-MM-dd'T'HH:mm:ss"
    private static final DateFormat MessageTimeStampDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ROOT);

    /** used for static {@link #showEventInGui(Intent)} to forwad to current visible gui  */
    private static MediaScanTraceActivity currentVisibleInstance = null;

    private TextView messages = null;

    /**
     * after media db change cached Directories must be recalculated
     */
    private final ContentObserver mMediaObserverDirectory = new ContentObserver(null) {

        // ignore version with 3rd param: int userId
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            addMessage("ContentObserver: ", "self=" + selfChange +
                    "; " + uri);
        }
    };

    private final ScannerReceiver mScanListener = new ScannerReceiver();

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
        this.getContentResolver().registerContentObserver(SQL_TABLE_EXTERNAL_CONTENT_URI, true, mMediaObserverDirectory);
        this.getContentResolver().registerContentObserver(SQL_TABLE_EXTERNAL_CONTENT_URI_FILE, true, mMediaObserverDirectory);

        IntentFilter f = new IntentFilter();
        f.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        f.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        f.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        f.addDataScheme("file");
        f.addDataScheme("content");
        registerReceiver(mScanListener, f);


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

        unregisterReceiver(mScanListener);

        this.getContentResolver().unregisterContentObserver(mMediaObserverDirectory);
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
            final String message = MediaScannerTools.formatLogMessage(this, broadcastIntent);
            addMessage(dbgContext, message);
        }
    }

    private void addMessage(String dbgContext, String message) {
        messages.append(MessageTimeStampDateFormat.format(new Date()) + " "
                + dbgContext + NEWLINE_TAB
                + message + NEWLINE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SETTINGS_KEY, messages.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

}
