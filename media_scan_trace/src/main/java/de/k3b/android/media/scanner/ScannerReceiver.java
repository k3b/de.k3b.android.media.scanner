package de.k3b.android.media.scanner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ScannerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String message = intent + "\n\t" + MediaScannerTools.getText(context, intent);
        Log.i(Global.TAG, "onReceive " + message);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        MediaScanTraceActivity.onMediaEvent(intent);
    }
}
