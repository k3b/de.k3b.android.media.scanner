/*
 * Copyright (c) 2015-2019 by k3b.
 *
 * This file is part of AndroFotoFinder / #APhotoManager.
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

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.MediaStore;
/**
 * Created by k3b on 14.07.2015.
 */
public class MediaScanTraceApp extends Application {
    private final ContentObserver mMediaObserverDirectory = new ContentObserver(null) {

        // ignore version with 3rd param: int userId
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);

            if (MediaScanTraceActivity.currentVisibleInstance != null) {
                MediaScanTraceActivity.currentVisibleInstance.addMessage("App ContentObserver: ", "self=" + selfChange +
                        "; " + uri);
            }
        }
    };

    // private final ScannerReceiver mScanListener = new ScannerReceiver("App");
    @Override public void onCreate() {
        super.onCreate();
        this.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, mMediaObserverDirectory);
        this.getContentResolver().registerContentObserver(MediaStore.Files.getContentUri("external"), true, mMediaObserverDirectory);

        /*
        IntentFilter f = new IntentFilter();
        f.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        f.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        f.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        f.addDataScheme("file");
        f.addDataScheme("content");
        registerReceiver(mScanListener, f);
        */
    }

    /* gibt es nicht
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mScanListener);

        this.getContentResolver().unregisterContentObserver(mMediaObserverDirectory);
    }
*/
}
