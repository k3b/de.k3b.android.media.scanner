package de.k3b.android.media.scanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class MediaScanTraceActivity extends Activity {
    private static final String SETTINGS_KEY = "Test-";

    private static MediaScanTraceActivity instance = null;
    private TextView log = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = (TextView) findViewById(R.id.text1);

        if (savedInstanceState != null) {
            log.setText(savedInstanceState.getString(SETTINGS_KEY));
        }

        instance = this;

        selfTest();
    }

    private void selfTest() {
        final Intent scanTestIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File("/does/not/exist/test.jpg")));
        final String debugMessage = "onCreate-selfTest-sendBroadcast ";
        Log.i(Global.TAG, debugMessage + scanTestIntent);
        showEvent(debugMessage, scanTestIntent);
        this.sendBroadcast(scanTestIntent);
    }

    @Override
    protected void onResume() {
        instance = this;
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    public static void onMediaEvent(Intent intent) {
        if (instance != null) {
            instance.showEvent("onReceive", intent);
        }
    }

    private void showEvent(String dbgContext, Intent intent) {
        if (intent != null) {
            log.append(dbgContext + "\n\t" + MediaScannerTools.getText(this, intent));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SETTINGS_KEY, log.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

}
