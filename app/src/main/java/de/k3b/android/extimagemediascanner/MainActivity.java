package de.k3b.android.extimagemediascanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends Activity {
    private static final String SETTINGS_KEY = "Test-";

    private static MainActivity instance = null;
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

        Intent scanTestIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File("/does/not/exist.jpg")));
        Log.i(Global.TAG,"sendBroadcast " + scanTestIntent);
        this.sendBroadcast(scanTestIntent);

        scanTestIntent = new Intent("de.k3b.test.MEDIA_SCANNER_SCAN_FILE",
                Uri.fromFile(new File("/does/not/exist.jpg")));
        Log.i(Global.TAG,"sendBroadcast " + scanTestIntent);
        this.sendBroadcast(scanTestIntent);
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    public static void onMediaEvent(Intent intent) {
        if (instance != null) {
            instance.showEvent(intent);
        }
    }

    private void showEvent(Intent intent) {
        if (intent != null) {
            log.append(MediaScannerTools.getText(this, intent));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(SETTINGS_KEY, log.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }

}
