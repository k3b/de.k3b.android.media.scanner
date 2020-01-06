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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/** receives and processes media scanner related Broadcasts */
public class ScannerReceiver extends BroadcastReceiver {

    private final String name;

    public ScannerReceiver() {
        this("Auto");
    }

    public ScannerReceiver(String name) {

        this.name = name;
    }

    @Override
    public void onReceive(Context context, Intent broadcastIntent) {
        final String message = name + ":" + broadcastIntent + "\n\t" + MediaScannerTools.formatLogMessage(context, broadcastIntent);
        Log.i(Global.TAG, "onReceive " + message);

        if (Global.showToasts) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
        MediaScanTraceActivity.showEventInCurrentGui(name, broadcastIntent);
    }
}
